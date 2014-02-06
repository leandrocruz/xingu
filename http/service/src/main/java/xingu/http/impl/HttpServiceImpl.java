package xingu.http.impl;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.utils.TimeUtils;

import xingu.http.HttpService;

public class HttpServiceImpl
    implements HttpService, Configurable, Startable
{
    protected HttpClient httpclient;
    
    protected BasicHttpParams defaultParams;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        long connectionTimeout = TimeUtils.toMillis(conf.getChild("http").getAttribute("connectionTimeout", "500ms"));
        long soTimeout = TimeUtils.toMillis(conf.getChild("http").getAttribute("soTimeout", "10s"));
        defaultParams = new BasicHttpParams();
        defaultParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, (int) soTimeout);
        defaultParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, (int) connectionTimeout);
        //defaultParams.setParameter(ConnManagerPNames.TIMEOUT, (long) 1500);
    }

    @Override
    public void start() 
        throws Exception
    {
        SchemeRegistry reg = new SchemeRegistry();
        reg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        reg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(defaultParams, reg);
        httpclient = new DefaultHttpClient(cm, defaultParams);
    }

    @Override
    public void stop() 
        throws Exception
    {
        httpclient.getConnectionManager().shutdown();
    }
    
    @Override
    public <T> T exec(HttpUriRequest request, ResponseHandler<T> handler)
    {
        try
        {
            return httpclient.execute(request, handler);
        }
        catch(SocketTimeoutException e)
        {
            logger.info("Socket Timeout on HTTP client: '{}'. Reason: {}", request.getURI(), e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            logger.info("Error on HTTP client: '{}'. Reason: {}", request.getURI(), e.getMessage());
            return null;
        }
    }

    @Override
    public HttpResponse request(String url)
    {
        return exec(new HttpHead(url), new ResponseHandler<HttpResponse>(){

            @Override
            public HttpResponse handleResponse(HttpResponse response)
                    throws ClientProtocolException, IOException
            {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() >= 300) 
                {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
                return response;
            }
        });
    }
}