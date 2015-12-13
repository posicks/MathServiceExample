package net.posick.math.fibonacci;

/**
 * Interface describing the Fibonacci API 
 * 
 * @author Steve Posick
 */
public interface Fibonacci
{
    /**
     * Generates a Fibonacci sequence of the specified length starting at the specified start number.
     * 
     * @param start The starting number
     * @param length The number of Fibonacci sequence numbers to generate. Fibonacci sequence length
     * 
     * @return A Fibonacci sequence of the specified length starting at the specified start number
     */
    public FibonacciSequence generate(int start, int length)
    throws IllegalArgumentException;
}
