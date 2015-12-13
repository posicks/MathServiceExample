package net.posick.math.impl.fibonacci;

import java.math.BigInteger;

import net.posick.math.fibonacci.Fibonacci;
import net.posick.math.fibonacci.FibonacciSequence;

/**
 * The FibonacciGenerator generates a Fibonacci sequence starting at the specified start index
 * of the specified length.
 * 
 * @author Steve Posick
 */
public abstract class FibonacciGenerator implements Fibonacci
{
    public static final BigInteger[] FIBONACCI_START = new BigInteger[] {BigInteger.ZERO, BigInteger.ONE, BigInteger.ONE};
    
    protected FibonacciStore store;
    
    
    /**
     * Generates the Fibonacci number for the specified value n and the values for n-1 and n + 1.
     * The resulting array shall contain 3 values containing the values: f(n-1), f(n) and f(n+1), 
     * where f is the Fibonacci function for index n. Negative values of n shall act as a 0 (Zero).
     * 
     * For example,
     *   generateForN(0) equals BigInteger[] {0, 0, 1};
     *   generateForN(1) equals BigInteger[] {0, 1, 1};
     *   generateForN(2) equals BigInteger[] {1, 1, 2};
     *   generateForN(3) equals BigInteger[] {1, 2, 3};
     *   generateForN(4) equals BigInteger[] {2, 3, 5};
     *   generateForN(5) equals BigInteger[] {3, 5, 8};
     * 
     * @param n The index into the Fibonacci sequence
     * @return The Fibonacci number for the specified value n and its two summands; [f(n-1), f(n), f(n+1)].
     */
    protected abstract BigInteger[] generateForN(int n);
    
    
    /**
     * Generates a Fibonacci sequence of the specified length starting with the values n and next,
     * n being the desired start and next being n+1 of the Fibonacci sequence
     * 
     * @param n The value for the Fibonacci index n
     * @param next The value for the Fibonacci index n + 1
     * @param length The total length of the Fibonacci sequence, starting with n and next
     * @return A Fibonacci sequence of the specified length, resuming from n and next
     */
    protected abstract BigInteger[] generate(BigInteger n, BigInteger next, int length);
    
    
    public void setFibonacciStore(FibonacciStore store)
    {
        this.store = store;
    }
    
    
    public FibonacciStore getFibonacciStore()
    {
        return store;
    }
    
    
    @Override
    public FibonacciSequence generate(int start, int length)
    throws IllegalArgumentException
    {
        if (start < 0)
        {
            throw new IllegalArgumentException("The starting number must be a positive integer");
        }
        
        if (length <= 0)
        {
            throw new IllegalArgumentException("The length must be a positive integer");
        }
        
        // Implement pre-calculated storage
        int end = start + length - 1;
        FibonacciSequence seq = null;
        if (store != null)
        {
            seq = store.getFibonacciSequence(start, end);
        }
        
        if (seq == null || seq.isEmpty())
        {
            return generateImpl(start, length);
        } else if (!seq.isComplete())
        {
            int seqLength = seq.getSequence().length;
            return seq.append(generateImpl(start + seqLength, length - seqLength));
        } else
        {
            return seq;
        }
    }
    
    
    private FibonacciSequence generateImpl(int start, int length)
    throws IllegalArgumentException
    {
        BigInteger[] startSeq;
        BigInteger n;
        BigInteger next;
        switch (start)
        {
            case 0:
                n = FIBONACCI_START[0];
                next = FIBONACCI_START[1];
                break;
            case 1:
                n = FIBONACCI_START[1];
                next = FIBONACCI_START[2];
                break;
            default:
                startSeq = generateForN(start);
                n = startSeq[1];
                next = startSeq[2];
                break;
        }
        return new FibonacciSequence(start, generate(n, next, length), length);
    }
    
    
    /* (non-Javadoc)
     * @see net.posick.math.impl.fibonacci.FibonacciGenerator#generate(java.math.BigInteger, java.math.BigInteger, int)
     */
    protected static BigInteger[] generateSequential(BigInteger n, BigInteger next, int length)
    {
        if (length < 0)
        {
            throw new IllegalArgumentException("The length must be a positive integer");
        }
        
        BigInteger[] seq = new BigInteger[length];
        seq[0] = n;
        if (length > 1)
        {
            seq[1] = next;
        }
        
        // Sequential Generation
        for (int index = 2; index < length; index++)
        {
            seq[index] = seq[index - 1].add(seq[index - 2]);
        }
        
        return seq;
    }
}
