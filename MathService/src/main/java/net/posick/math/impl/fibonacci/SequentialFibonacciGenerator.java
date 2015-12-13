package net.posick.math.impl.fibonacci;

import java.math.BigInteger;

public class SequentialFibonacciGenerator extends FibonacciGenerator
{
    private static final BigInteger[] FIBONACCI_SEED = new BigInteger[]
    {
        FIBONACCI_START[0],
        FIBONACCI_START[1],
        FIBONACCI_START[2],
        FIBONACCI_START[1].add(FIBONACCI_START[2]),
        FIBONACCI_START[1].add(FIBONACCI_START[2]).add(FIBONACCI_START[2])
    };
    
    /* (non-Javadoc)
     * @see net.posick.math.impl.fibonacci.FibonacciGenerator#generate(java.math.BigInteger, java.math.BigInteger, int)
     */
    @Override
    protected BigInteger[] generate(BigInteger n, BigInteger next, int length)
    {
        return generateSequential(n, next, length);
    }
    
    
    /* (non-Javadoc)
     * @see net.posick.math.impl.fibonacci.FibonacciGenerator#generateForN(int)
     */
    @Override
    protected BigInteger[] generateForN(int n)
    {
        switch (n)
        {
            case 0:
                return new BigInteger[] {BigInteger.ZERO, FIBONACCI_SEED[0], FIBONACCI_SEED[1]};
            case 1:
                return new BigInteger[] {FIBONACCI_SEED[0], FIBONACCI_SEED[1], FIBONACCI_SEED[2]};
            case 2:
                return new BigInteger[] {FIBONACCI_SEED[1], FIBONACCI_SEED[2], FIBONACCI_SEED[3]};
            default:
                BigInteger[] result = null;
                if (store != null && n < store.length())
                {
                    result = store.getForN(n);
                }
                
                if (result != null)
                {
                    return result;
                } else
                {
                    int start = store.length() - 1;
                    result = store.getForN(start);
                    for (int index = start; index < n; index++)
                    {
                        result[0] = result[1];
                        result[1] = result[2];
                        result[2] = result[0].add(result[1]);
                    }
                    return result;
                }
        }
    }
}
