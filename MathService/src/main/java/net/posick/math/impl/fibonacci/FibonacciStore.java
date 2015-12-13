package net.posick.math.impl.fibonacci;

import java.math.BigInteger;

import net.posick.math.fibonacci.FibonacciSequence;

/**
 * The FibonacciStore is a store of ordered Fibonacci values starting at index 0 and 
 * ending at position N, N being index equal to length - 1.
 * 
 * @author Steve Posick
 */
public interface FibonacciStore
{
    /**
     * Retrieves the cached portion of the FibonacciSequence from the Fibonacci Store.
     * Only the values contained within the store are returned. If the entire start/end
     * index is contained within the store a complete FibonacciSequence is returned. If 
     * all of the Fibonacci values between the start or end index values are is not 
     * contained within the store a partial FibonacciSequence is returned. If none of the
     * values are present and empty FibonacciSequence is returned.
     * 
     * @param start The start index
     * @param end The end index
     * @return The Fibonacci Sequence ranging from the start index to the end index, inclusive 
     * @throws IllegalArgumentException
     */
    public FibonacciSequence getFibonacciSequence(int start, int end)
    throws IllegalArgumentException;
    
    
    /**
     * Gets the Fibonacci number for the specified value n and the values for n-1 and n + 1.
     * The resulting array shall contain 3 values containing the values: f(n-1), f(n) and f(n+1), 
     * where f is the Fibonacci function for index n. Negative values of n shall act as a 0 (Zero).
     * 
     * For example,
     *   getForN(0) equals BigInteger[] {0, 0, 1};
     *   getForN(1) equals BigInteger[] {0, 1, 1};
     *   getForN(2) equals BigInteger[] {1, 1, 2};
     *   getForN(3) equals BigInteger[] {1, 2, 3};
     *   getForN(4) equals BigInteger[] {2, 3, 5};
     *   getForN(5) equals BigInteger[] {3, 5, 8};
     * 
     * @param n The index into the Fibonacci sequence
     * @return The Fibonacci number for the specified value n and its two summands; [f(n-1), f(n), f(n+1)].
     */
    public BigInteger[] getForN(int n)
    throws IllegalArgumentException;
    
    
    /**
     * The total length of the stored Fibonacci Sequence. The maximum end index is equal to length - 1.
     * 
     * @return The total length of the stored Fibonacci Sequence
     */
    public int length();
}
