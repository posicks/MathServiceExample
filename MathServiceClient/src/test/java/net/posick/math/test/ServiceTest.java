package net.posick.math.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.posick.math.util.ClientUtils;

/**
 * Basic client utilities.
 * 
 * @author Steve Posick
 */
public class ServiceTest
{
    protected static final String DEFAULT_BASE_URL = "http://localhost:8181/cxf/math/fibonacci";
    
    protected static final String KEY_BASE_URL = "service.url";
    
    private URL url;
    
    private Properties properties = new Properties();
    
    
    public ServiceTest()
    throws IOException
    {
        Class<?> clazz = getClass();
        URL resourceURL = clazz.getResource(clazz.getSimpleName() + ".properties");
        if (resourceURL != null)
        {
            InputStream in = resourceURL.openStream();
            properties.load(in);
            in.close();
        }
        this.url = new URL(properties.getProperty(KEY_BASE_URL, DEFAULT_BASE_URL));
    }
    
    
    public void testFibonacciXML()
    throws IOException
    {
        String results = httpGet(url, "application/xml");
        
        assert results.matches("^<\\?xml\\s.*>.*<FibonacciSequence((\\sxmlns=\".*\")|(\\sstart=\".*\")|(\\slength=\".*\")|(\\scomplete=\".*\")|(\\sempty=\".*\"))>(<Fibonacci>\\d+</Fibonacci>)+</FibonacciSequence>(\\s)*");
    }
    
    
    public void testFibonacciJSON()
    throws IOException
    {
        String results = httpGet(url, "application/json");
            
        assert results.matches("^\\{\"(math\\.)?FibonacciSequence\":\\{\"@start\":\"\\d+\",\"@length\":\"\\d+\",\"@complete\":\".*\",\"@empty\":\".*\",\"(math\\.)?Fibonacci\":\\[(\\d+,)*\\d+\\]}\\}");
    }
    
    
    static final String httpGet(URL url, String mediaType)
    throws IOException
    {
        System.out.println("Sending HTTP GET request to \"" + url + "\" for MediaType \"" + mediaType + "\"");
        URLConnection conn = null;
        InputStream in = null;
        try
        {
            // Execute GET request
            conn = url.openConnection();
            conn.setRequestProperty("Accept", mediaType);
            conn.connect();
            in = conn.getInputStream();
            String result = ClientUtils.getStringFromInputStream(in);
            System.out.println("Response -> \"" + result + "\"");
            return result;
        } finally
        {
            if (conn != null)
            {
                try
                {
                    in.close();
                } catch (Exception e)
                {
                    // ignore close exceptions.
                }
            }
        }
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        
    }
}
