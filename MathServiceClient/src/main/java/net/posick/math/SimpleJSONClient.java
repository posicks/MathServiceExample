package net.posick.math;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SimpleJSONClient extends SimpleHTTPClient
{
    public SimpleJSONClient(String[] args)
    throws IllegalArgumentException, MalformedURLException
    {
        super(args);
    }
    
    
    public String execute() throws IOException
    {
        System.out.println("Sending HTTP GET request to \"" + url + "\"");
        Client client = Client.create();
        WebResource webResource = client.resource(url.toString());
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE)
                                             .get(ClientResponse.class);

        if(response.getStatus() >= 300)
        {
            throw new RuntimeException("HTTP Error: "+ response.getStatus() + " - " + response.getEntity(String.class));
        }
        
        return response.getEntity(String.class);
    }
    
    
    public static void main(String[] args)
    throws Exception
    {
        try
        {
            SimpleJSONClient client = new SimpleJSONClient(args);
            
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
}
