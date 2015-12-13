package net.posick.math.impl.fibonacci;

import java.math.BigInteger;

import net.posick.math.fibonacci.FibonacciSequence;

public class InMemoryFibonacciStore implements FibonacciStore
{
    private int DEFAULT_STORE_SIZE = 93; // Store n Fibonacci values in memory
    
    private BigInteger[] store;
    
    
    public InMemoryFibonacciStore()
    {
        setStoreSize(DEFAULT_STORE_SIZE);
    }
    
    
    public InMemoryFibonacciStore(int size)
    {
        setStoreSize(size);
    }
    
    
    public void setStoreSize(int size)
    {
        store = FibonacciGenerator.generateSequential(FibonacciGenerator.FIBONACCI_START[0], FibonacciGenerator.FIBONACCI_START[1], size);
    }
    
    
    @Override
    public int length()
    {
        return store.length;
    }
    
    
    /* (non-Javadoc)
     * @see net.posick.math.fibonacci.FibonacciSequenceGenerator#generate(int, int)
     */
    @Override
    public final FibonacciSequence getFibonacciSequence(int start, int end)
    throws IllegalArgumentException
    {
        if (start < 0)
        {
            throw new IllegalArgumentException("The starting index must be a positive integer");
        }
        
        if (end < start)
        {
            throw new IllegalArgumentException("The end must be a positive integer greater than the start");
        }
        
        BigInteger[] sequence = null;
        int length = end - start + 1;
        if (start < store.length && end < store.length)
        {
            sequence = new BigInteger[length];
            System.arraycopy(store, start, sequence, 0, length);
        } else if (start < store.length)
        {
            sequence = new BigInteger[store.length - start];
            System.arraycopy(store, start, sequence, 0, sequence.length);
        } else
        {
            return null;
        }
                
        return new FibonacciSequence(start, sequence, length);
    }


    @Override
    public BigInteger[] getForN(int n)
    throws IllegalArgumentException
    {
        if (n < store.length)
        {
            BigInteger[] result = new BigInteger[3];
            result[0] = n > 0 ? store[n - 1] : BigInteger.ZERO;
            result[1] = store[n];
            result[2] = n < store.length - 1 ? store[n + 1] : result[0].add(result[1]);
            return result;
        } else
        {
            return null;
        }
    }
}
