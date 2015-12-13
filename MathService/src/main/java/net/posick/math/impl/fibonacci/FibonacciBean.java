package net.posick.math.impl.fibonacci;

import net.posick.math.fibonacci.Fibonacci;
import net.posick.math.fibonacci.FibonacciSequence;

/**
 * Generates and stores a Fibonacci sequence. Opted an iterative approach with the ability 
 * to have lower values stored for fixed time return.
 * 
 *  Opted this method over () algorithm, as floating point math implementations vary in precision
 *  and the result may not be accurate.
 * 
 * @author Steve Posick
 */
public class FibonacciBean implements Fibonacci
{
    private FibonacciGenerator generator;
    
    
    public FibonacciBean(FibonacciGenerator generator)
    {
        setFibonacciGenerator(generator);
    }
    
    
    public void setFibonacciGenerator(FibonacciGenerator generator)
    {
        this.generator = generator;
    }
    
    
    public FibonacciGenerator getFibonacciGenerator()
    {
        return generator;
    }
    
    
    /* (non-Javadoc)
     * @see net.posick.math.fibonacci.FibonacciSequenceGenerator#generate(int, int)
     */
    @Override
    public final FibonacciSequence generate(int start, int length)
    throws IllegalArgumentException
    {
        if (start < 0)
        {
            throw new IllegalArgumentException("The starting number must be a positive integer");
        }
        
        if (length < 0)
        {
            throw new IllegalArgumentException("The length must be a positive integer");
        }
        
        return generator.generate(start, length);
    }
}
