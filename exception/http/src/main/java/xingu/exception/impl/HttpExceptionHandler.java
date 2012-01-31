package xingu.exception.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import xingu.codec.Codec;
import xingu.http.HttpService;
import xingu.time.Time;
import br.com.ibnetwork.xingu.container.Inject;

public class HttpExceptionHandler
    extends AsyncExceptionHandler
    implements Configurable
{
    @Inject
    private HttpService http;
    
    @Inject
    private Time time;
    
    @Inject
    private Codec codec;

    private String host;
    
    private int port;
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        super.configure(conf);
        host = conf.getChild("server").getAttribute("host", "localhost");
        port = conf.getChild("server").getAttributeAsInteger("port", 80);
        
    }

    @Override
    protected void process(ExceptionItem item)
        throws Exception
    {
        /*
         * use net.kidux.unicorn.commons.utils.http.impl.HttpClientSupport or
         * Hotpotato http://hotpotato.biasedbit.com/doc/0.8.0/userguide/html/index.html
         * to perfom the http request 
         */
        String url = baseUrl() + "/exec/error.Report";
        HttpPost request = new HttpPost(url);
        HttpEntity entity = entityFrom(item);
        request.setEntity(entity);
        HttpResponse response = http.exec(request, new ResponseHandler<HttpResponse>(){

            @Override
            public HttpResponse handleResponse(HttpResponse response)
                    throws ClientProtocolException, IOException
            {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() != 200) 
                {
                    System.out.println(statusLine.getStatusCode());
                    System.out.println(statusLine.getReasonPhrase());
                }
                return response;
            }
        });
    }

    /*
     * @see http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d4e186 (HTML Forms)
     */
    protected HttpEntity entityFrom(ExceptionItem item)
        throws UnsupportedEncodingException
    {
        Throwable error = item.throwable;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("stackTrace", ExceptionUtils.getFullStackTrace(error)));
        params.add(new BasicNameValuePair("throwable", error.getClass().getName()));
        params.add(new BasicNameValuePair("time", String.valueOf(time.now().time())));
        addParamsTo(params);
        if(error instanceof ExceptionWithData)
        {
            ExceptionWithData withData = (ExceptionWithData) error;
            Object data = withData.data();
            if(data != null)
            {
                params.add(new BasicNameValuePair("data", codec.encode(data)));
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        return entity;
    }

    protected void addParamsTo(List<NameValuePair> params)
    {
        //hook for subclasses
    }

    private String baseUrl()
    {
        return "http://"+host+":"+port;
    }
}
