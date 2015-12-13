package net.posick.math.impl.fibonacci;

import java.math.BigInteger;

import net.posick.math.Matrix;

public class MatrixFibonacciGenerator extends SequentialFibonacciGenerator
{
    public static final Matrix<BigInteger> FIBONACCI_START_MATRIX = Matrix.newInstance(new BigInteger[][] 
    {
        {FIBONACCI_START[2], FIBONACCI_START[1]},
        {FIBONACCI_START[1], FIBONACCI_START[0]}
    });
    
    
    /* (non-Javadoc)
     * @see net.posick.math.impl.fibonacci.FibonacciGenerator#generateForN(int)
     */
    @Override
    protected BigInteger[] generateForN(int n)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("n must be greater than or equal to 0 for Matrix.pow(). (n >= 0)");
        } else if (n == 0)
        {
            return FIBONACCI_START;
        } else
        {
            Matrix<BigInteger> matrix = FIBONACCI_START_MATRIX.pow(n);
            
            /*
             * [1 1]^n = [F(n+1),   F(n)]
             * [1 0]     [  F(n), F(n-1)]
             */
            return new BigInteger[]
            {
                matrix.value(1, 1),
                matrix.value(0, 1),
                matrix.value(0, 0)
            };
        }
    }
}
