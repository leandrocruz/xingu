package xingu.email.impl.chimp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeUtility;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import xingu.container.Inject;
import xingu.email.Email;
import xingu.email.impl.EmailManagerSupport;
import xingu.http.HttpService;

public class MailChimpEmailManager
    extends EmailManagerSupport
{
    @Inject
    private HttpService http;
    
    private String apiKey;

    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        super.configure(conf);
        apiKey = conf.getChild("api").getAttribute("key");
    }

    @Override
    protected boolean send(Email email)
        throws Exception
    {
        String url = "http://us1.sts.mailchimp.com/1.0/SendEmail";
        HttpPost request = new HttpPost(url);
        HttpEntity entity = entityFrom(email);
        request.setEntity(entity);
        HttpResponse response = http.exec(request, new ResponseHandler<HttpResponse>(){

            @Override
            public HttpResponse handleResponse(HttpResponse response)
                    throws ClientProtocolException, IOException
            {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                //TODO: handle error response
                System.out.println(result);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() != 200) 
                {
                    System.out.println(statusLine.getStatusCode());
                    System.out.println(statusLine.getReasonPhrase());
                }
                return response;
            }
        });
        return true;
    }

    private HttpEntity entityFrom(Email email)
        throws UnsupportedEncodingException
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("apikey", apiKey));

        String html = render(email, email.getHtmlTemplate(), email.getHtmlLayoutTemplate());
        params.add(new BasicNameValuePair("message[html]", html));

        String txt = render(email, email.getTextTemplate(), email.getTextLayoutTemplate());
        params.add(new BasicNameValuePair("message[text]", txt));

        String subject = email.getSubject();
        subject = MimeUtility.encodeText(subject);
        params.add(new BasicNameValuePair("message[subject]", subject));
        params.add(new BasicNameValuePair("message[from_name]", email.getFromName()));
        params.add(new BasicNameValuePair("message[from_email]", email.getFromAddress()));
        params.add(new BasicNameValuePair("message[to_name][0]", email.getToName()));
        params.add(new BasicNameValuePair("message[to_email][0]", email.getToAddress()));
        params.add(new BasicNameValuePair("track_opens", "true"));
        params.add(new BasicNameValuePair("track_clicks", "true"));
        params.add(new BasicNameValuePair("tags[0]", "test"));
        
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        return entity;
    }
}