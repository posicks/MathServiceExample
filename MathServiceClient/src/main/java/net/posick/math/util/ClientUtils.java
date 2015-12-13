package net.posick.math.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic client utilities.
 * 
 * @author Steve Posick
 */
public class ClientUtils
{
    /**
     * Takes the contents of the provided InputStream and converts if to a String.
     * 
     * @param in The InputStream
     * @return The String value from the provided InputStream
     * @throws IOException
     */
    public static String getStringFromInputStream(InputStream in)
    throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(65535);
        final byte[] buffer = new byte[65535];
        
        int bytesRead = in.read(buffer);
        do
        {
            out.write(buffer, 0, bytesRead);
        } while ((bytesRead = in.read(buffer)) != -1);
        
        return out.toString();
    }
}
