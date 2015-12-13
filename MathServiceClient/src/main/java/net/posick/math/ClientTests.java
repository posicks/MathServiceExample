package net.posick.math;

/**
 * Runs a series of client tests using the two simple clients to verify the validity and security of the
 * Math services.
 * 
 * @author Steve Posick
 */
public class ClientTests
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            SimpleHTTPClient httpClient = new SimpleHTTPClient(args); 
            SimpleJSONClient jsonClient = new SimpleJSONClient(args); 
            
            System.out.println(httpClient.execute());
            System.out.println(jsonClient.execute());
            System.out.println("\n");
        } catch (IllegalArgumentException e)
        {
            SimpleHTTPClient.displayHelpMessage(args);
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
