package net.posick.math;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import net.posick.math.util.ClientUtils;

public class SimpleHTTPClient
{
    protected static final String DEFAULT_BASE_URL = "http://localhost:8181/cxf/math/fibonacci";
    
    protected static enum URL_PARAMETER_DELIMITERS 
    {
        QUESTION_MARK('?'), AMPERSAND('&');
        
        private char delimiter;
        
        private URL_PARAMETER_DELIMITERS(char delimiter)
        {
            this.delimiter = delimiter;
        }
        
        
        protected static char getDelimiter(int count)
        {
            URL_PARAMETER_DELIMITERS[] values = URL_PARAMETER_DELIMITERS.values();
            
            return count >= values.length ? values[values.length - 1].delimiter : values[count].delimiter;
        }
    }
    
    
    protected static enum PARAMETER_TYPE 
    {
        PARAM('p', "param"), URL('u', "url");
        
        private char paramKey;
        
        private String paramName;
        
        
        private PARAMETER_TYPE(char key, String paramName)
        {
            this.paramKey = key;
            this.paramName = paramName;
        }
        
        
        protected String getParameterKey()
        {
            return paramName;
        }
        
        
        protected static PARAMETER_TYPE fromName(String name)
        {
            for (PARAMETER_TYPE type : PARAMETER_TYPE.values())
            {
                if (type.paramName.equalsIgnoreCase(name))
                {
                    return type;
                }
            }
            
            return null;
        }
        
        
        protected static PARAMETER_TYPE fromKey(char key)
        {
            for (PARAMETER_TYPE type : PARAMETER_TYPE.values())
            {
                if (type.paramKey == key)
                {
                    return type;
                }
            }
            
            return null;
        }
    }


    protected URL url;
    
    
    public SimpleHTTPClient(String[] args)
    throws IllegalArgumentException, MalformedURLException
    {
        List<String> urlParams = new LinkedList<String>();
        String urlParam = null;
        
        if (args.length > 0)
        {
            PARAMETER_TYPE paramType = null;
            for (int index = 0; index < args.length; index++)
            {
                String temp = args[index];
                if (temp.startsWith("-"))
                {
                    if (temp.startsWith("--"))
                    {
                        paramType = PARAMETER_TYPE.fromName(temp.substring(2));
                    } else if (temp.length() == 2)
                    {
                        paramType = PARAMETER_TYPE.fromKey(temp.charAt(1));
                    }
                    
                    if (paramType == null)
                    {
                        throw new IllegalArgumentException();
                    }
                    continue;
                }
                
                if (paramType == null || paramType == PARAMETER_TYPE.URL)
                {
                    if (urlParam == null)
                    {
                        urlParam = temp;
                    } else
                    {
                        throw new IllegalArgumentException();
                    }
                    paramType = null;
                } else if (paramType == PARAMETER_TYPE.PARAM)
                {
                    urlParams.add(args[index]);
                    paramType = null;
                }
            }
        }
        
        // Set base URL
        StringBuilder urlBuilder = new StringBuilder();
        if (urlParam != null)
        {
            urlBuilder.append(urlParam);
        } else
        {
            urlBuilder.append(DEFAULT_BASE_URL);
        }
        
        // Add URL Parameters
        int count = 0;
        for (String param : urlParams)
        {
            urlBuilder.append(URL_PARAMETER_DELIMITERS.getDelimiter(count++)).append(param);
        }
        
        this.url = new URL(urlBuilder.toString());
    }
    
    
    public String execute() throws IOException
    {
        System.out.println("Sending HTTP GET request to \"" + url + "\"");
        URLConnection conn = null;
        InputStream in = null;
        try
        {
            // Execute GET request
            conn = url.openConnection();
            in = url.openStream();
            return ClientUtils.getStringFromInputStream(in);
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
    
    
    public static void main(String[] args)
    throws Exception
    {
        try
        {
            SimpleHTTPClient client = new SimpleHTTPClient(args);
            
            System.out.println(client.execute());
            System.out.println("\n");
        } catch (IllegalArgumentException e)
        {
            displayHelpMessage(args);
            System.exit(-1);
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(-1);
        }

        System.exit(0);
    }
    
    
    protected static void displayHelpMessage(String[] args)
    {
        String commandLine = 
        "FibonacciSimpleHTTPClient:\n" +
        "    java net.posick.math.FibonacciSimpleHTTPClient [${url}] [-u | --url ${url}] [-p | --param ${url param}]\n" +
        "\n" +
        "        -u | --url    Specify the full or base URL\n" +
        "        -p | --param  Specify a URL parameter, key and value. ex. -p length=100\n";
        
        System.out.println(commandLine + "\n");
    }
}
