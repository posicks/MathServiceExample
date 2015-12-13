package net.posick.math.impl.fibonacci.net.rest;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Variant;

import net.posick.math.fibonacci.Fibonacci;
import net.posick.math.fibonacci.FibonacciSequence;



/**
 * The FibonacciJAXRS bean provides a JAX-RS RESTful Web Service Endpoint for the  
 * Fibonacci service.
 * 
 * @author Steve Posick
 */
public class FibonacciJAXRS
{
    private static class CustomStatus implements StatusType
    {
        private int code;
        
        private Family family;
        
        private String reasonPhrase;
        
        
        public CustomStatus(int statusCode, Family family, String reasonPhrase)
        {
            this.code = statusCode;
            this.family = family;
            this.reasonPhrase = reasonPhrase;
        }
        
        
        @Override
        public int getStatusCode()
        {
            return code;
        }
        
        
        @Override
        public Family getFamily()
        {
            return family;
        }
        
        
        @Override
        public String getReasonPhrase()
        {
            return reasonPhrase;
        }
    }


    private static final int DEFAULT_CACHE_MAX_AGE = 1000;
    
    private Fibonacci fibonacci;

    private static final String[] SUPPORTED_ENCODINGS = new String[] {"gzip"};

    private int cacheMaxAge = DEFAULT_CACHE_MAX_AGE;
    
    
    public FibonacciJAXRS(Fibonacci fibonacci)
    {
        super();
        this.fibonacci = fibonacci;
    }
    
    
    /**
     * Sets the Maximum age for Cached Responses.
     * 
     * @param cacheMaxAge The Maximum age for Cached Responses
     */
    public void setCacheMaxAge(int cacheMaxAge)
    {
        this.cacheMaxAge  = cacheMaxAge;
    }
    
    
    /**
     * Gets the Maximum age for Cached Responses.
     * 
     * @return The Maximum age for Cached Responses
     */
    public int getCacheMaxAge()
    {
        return cacheMaxAge;
    }
    
    
    /**
     * RESTful Web Service endpoint for the Fibonacci service.
     * 
     * @param start The starting number
     * @param length The number of Fibonacci sequence numbers to generate. Fibonacci sequence length
     * 
     * @return A Fibonacci sequence of the specified length starting at the specified start number
     */
    @GET
    @Path("/fibonacci")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getFibonacciSequence(@QueryParam("start") @DefaultValue("0") int start, 
                                         @QueryParam("length") @DefaultValue("5") int length,
                                         @Context final Request request,
                                         @Context final UriInfo uriInfo)
    throws Exception
    {
        ResponseBuilder builder;
        try
        {
            if (start < 0)
            {
                throw new BadRequestException("Start parameter must be a positive integer (start >= 0)");
            } else if (length <= 0)
            {
                throw new BadRequestException("Length parameter must be an integer greater than 0 (length > 0)");
            }
        
            // Generating ETag from input parameters
            CacheControl cc = new CacheControl();
            cc.setMaxAge(cacheMaxAge);
             
            // Build list of Variants
            List<Variant> variantList = Variant.mediaTypes(MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_JSON_TYPE)
                                               .languages(Locale.getAvailableLocales())
                                               .encodings(SUPPORTED_ENCODINGS)
                                               .add()
                                               .build();
            
            Variant variant = request.selectVariant(variantList);
            EntityTag eTag = new EntityTag(variant.getMediaType() + ":" + variant.getEncoding() + ":" + variant.getLanguageString() + ":" + start + ":" + length);
            
            builder = request.evaluatePreconditions(eTag);
            if (builder != null)
            {
                // The preconditions have been met and the cache entry is valid
                builder.cacheControl(cc);
            } else
            {
                // The preconditions have been met and the cache is invalid
                FibonacciSequence sequence = fibonacci.generate(start, length);
                if (sequence == null)
                {
                    throw new Exception("Fibonacci service returned a null sequence.");
                }
                
                // Create ResponseBuilder with selected Variant
                builder = Response.ok()             // variant is not included as it prevents GZIP compression
                                  .entity(sequence)
                                  .type(variant.getMediaType())
                                  .cacheControl(cc) // Set the CacheControl
                                  .tag(eTag);       // Set the response EntityTag
            }
        } catch (BadRequestException e)
        {
            builder = Response.serverError()
                              .entity("Client Error: " + e.getMessage())
                              .variant(new Variant(MediaType.TEXT_PLAIN_TYPE, "en", null))
                              .status(new CustomStatus(400, Family.CLIENT_ERROR, e.getMessage()));
        } catch (IllegalArgumentException e)
        {
            builder = Response.serverError()
                              .entity("Client Error: " + e.getMessage())
                              .variant(new Variant(MediaType.TEXT_PLAIN_TYPE, "en", null))
                              .status(new CustomStatus(400, Family.CLIENT_ERROR, e.getMessage()));
        } catch (Exception e)
        {
            builder = Response.serverError()
                              .entity("Server Error: " + e.getMessage())
                              .variant(new Variant(MediaType.TEXT_PLAIN_TYPE, "en", null))
                              .status(new CustomStatus(500, Family.SERVER_ERROR, e.getMessage()));
        } finally
        {
        }
        
        return builder.build();
    }
}
