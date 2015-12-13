package net.posick.math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import net.posick.math.impl.BigDecimalMatrix;
import net.posick.math.impl.BigIntegerMatrix;

public abstract class Matrix<T extends Number> implements Serializable
{
    private static final long serialVersionUID = 201512031704L;
    
    public static enum TYPE {INTEGER, DECIMAL};
    
    /**
     * The number of matrix rows.
     *  
     * @serial
     */
    protected int rows;
    
    /**
     * The number of matrix columns.
     * 
     * @serial
     */
    protected int columns;
    
    /**
     * The raw data of the matrix.
     *  
     * @serial
     */
    protected T[][] data;

    /**
     * The matrix data type.
     *  
     * @serial
     */
    protected TYPE type;
    
    
    protected Matrix()
    {
        super();
    }
    
    
    protected Matrix(T[][] matrix)
    {
        int rows = 0;
        int columns = 0;
        
        if ((rows = matrix.length) == 0)
        {
            throw new IllegalArgumentException("The Matrix must contain 1 or more rows.");
        }
        if ((columns = matrix[0].length) == 0)
        {
            throw new IllegalArgumentException("The Matrix must contain 1 or more columns.");
        }
        
        this.rows = rows;
        this.columns = columns;
        this.data = matrix;
        this.type =  determineComponentType(matrix);
    }
    
    
    /**
     * Returns the number of rows contained within this matrix.
     * 
     * @return the number of rows contained within this matrix
     */
    public int rows()
    {
        return rows;
    }
    
    
    /**
     * Returns the number of columns contained within this matrix.
     * 
     * @return the number of columns contained within this matrix
     */
    public int columns()
    {
        return columns;
    }
    
    
    /**
     * Returns the TYPE of number used within the Matrix.
     * 
     * @return The TYPE of number used within the Matrix
     * @see Matrix.TYPE
     */
    public TYPE type()
    {
        return type;
    }
    
    
    /**
     * Returns the value for the provided row and column. 
     * 
     * @param row The row
     * @param column The column
     * @return The value for the provided row and column
     */
    public T value(int row, int column)
    {
        return data[row][column];
    }
    
    
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                builder.append(String.format("%-11.10s", data[row][column]));
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    
    
    public abstract Matrix<T> multiply(Matrix<? extends Number> that)
    throws IllegalArgumentException;
    
    
    public Matrix<T> pow(int n)
    throws IllegalArgumentException
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("The exponent must be 0 or greater.");
        }
        Matrix<T> matrix = this;
        Matrix<T> result = generateIdentityMatrix(type, matrix.rows(), matrix.columns());
        
        while (n != 0)
        {
            // Exponentiation by squaring
            if ((n & 0x01) != 0)  // if (n % 2 != 0)
            {
                result = result.multiply(matrix);
            }
            n >>>= 1;  // n /= 2;
            matrix = matrix.multiply(matrix);
        }
        return result;
    }
    
    
    /**
     * Checks the two matrices to ensure that they can be multiplied. (m1.columns == m2.rows)
     * 
     * @param m1 Matrix 1
     * @param m2 Matrix 2
     * @throws IllegalArgumentException If the matrices cannot be multiplied together
     */
    protected static void multiplicationCheck(Matrix<? extends Number> m1, Matrix<? extends Number> m2)
    throws IllegalArgumentException
    {
        if (m1.columns != m2.rows)
        {
            throw new IllegalArgumentException("The number of rows in matrix m1 must match the number of columns in matrix m2.");
        }
    }
    
    
    /**
     * Determines the Component Type of an Array.
     * 
     * @param o The array or multidimensional array.
     * @return The arrays component class type
     * @see java.lang.Class#getComponentType()
     */
    protected static TYPE determineComponentType(Object o)
    {
        Class<?> clazz = o.getClass();
        while (clazz != null && clazz.isArray())
        {
            clazz = clazz.getComponentType();
        }
        
        if (clazz != null)
        {
            if (clazz == Byte.class || clazz == Byte.TYPE)
            {
                return TYPE.INTEGER;
            } else if (clazz == Short.class || clazz == Short.TYPE)
            {
                return TYPE.INTEGER;
            } else if (clazz == Integer.class || clazz == Integer.TYPE)
            {
                return TYPE.INTEGER;
            } else if (clazz == Long.class || clazz == Long.TYPE)
            {
                return TYPE.INTEGER;
            } else if (clazz == Float.class || clazz == Float.TYPE)
            {
                return TYPE.DECIMAL;
            } else if (clazz == Double.class || clazz == Double.TYPE)
            {
                return TYPE.DECIMAL;
            } else if (clazz == BigInteger.class)
            {
                return TYPE.INTEGER;
            } else if (clazz == BigDecimal.class)
            {
                return TYPE.DECIMAL;
            } else
            {
                return null;
            }
        } else
        {
            return null;
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public static <T extends Number> Matrix<T> generateIdentityMatrix(TYPE type, int rows, int columns)
    {
        switch (type)
        {
            case DECIMAL:
                return (Matrix<T>) BigDecimalMatrix.generateIdentityMatrix(rows, columns);
            case INTEGER:
                return (Matrix<T>) BigIntegerMatrix.generateIdentityMatrix(rows, columns);
        }
        return null;
    }

    
    
    /**
     * Creates a new immutable Matrix with the specified number of rows and columns 
     * populated with the specified data.
     * 
     * @param columns The number of columns
     * @param rows The number of rows
     * @param data The data that makes up the matrix
     * @return An immutable Matrix populated with the matrix data
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> Matrix<T> newInstance(T[][] data)
    throws IllegalArgumentException
    {
        switch (determineComponentType(data))
        {
            case DECIMAL:
                return (Matrix<T>) new BigDecimalMatrix((BigDecimal[][]) data);
            case INTEGER:
                return (Matrix<T>) new BigIntegerMatrix((BigInteger[][]) data);
            default:
                return (Matrix<T>) new BigIntegerMatrix((BigInteger[][]) data);
        }
    }
}
