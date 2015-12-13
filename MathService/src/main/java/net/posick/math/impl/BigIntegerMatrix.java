package net.posick.math.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.posick.math.Matrix;

public class BigIntegerMatrix extends Matrix<BigInteger>
{
    private static final long serialVersionUID = 201512031704L;


    protected BigIntegerMatrix()
    {
        super();
    }


    public BigIntegerMatrix(BigInteger[][] matrix)
    {
        super(matrix);
    }


    @Override
    public Matrix<BigInteger> multiply(Matrix<? extends Number> that)
    throws IllegalArgumentException
    {
        multiplicationCheck(this, that);
        
        TYPE type = that.type();
        
        int m1Rows = this.rows;
        int m1Columns = this.columns;
        int m2Columns = that.columns();
        
        BigInteger[][] newData = new BigInteger[m1Rows][m2Columns];
        
        // For each this.row
        // For each this.column and that.row
        //   Multiply this[this.row, this.column] by that[that.row, that.column]
        //   Add result[this.row]
        
        for (int m1Row = 0; m1Row < m1Rows; m1Row++)
        {
            for (int m2Column = 0; m2Column < m2Columns; m2Column++)
            {
                // index = m1Column and m2Row
                for (int index = 0; index < m1Columns; index++)
                {
                    BigInteger thisValue = ((thisValue = this.value(m1Row, index)) == null ? BigInteger.ZERO : thisValue);
                    BigInteger thatValue;
                    
                    switch (type)
                    {
                        case INTEGER:
                            thatValue = (BigInteger) that.value(index, m2Column);
                            thatValue = (thatValue == null ? BigInteger.ZERO : thatValue);
                            break;
                        case DECIMAL:
                            BigDecimal bigDec = (BigDecimal) that.value(index, m2Column);
                            thatValue = (bigDec == null ? BigInteger.ZERO : bigDec.toBigInteger());
                            break;
                        default:
                            thatValue = BigInteger.ZERO;
                            break;
                    }
                    
                    BigInteger value = ((value = newData[m1Row][m2Column]) == null ? BigInteger.ZERO : value);
                    newData[m1Row][m2Column] = value.add(thisValue.multiply(thatValue));
                }
            }
        }
        
        return new BigIntegerMatrix(newData);
    }
    
    
    public static Matrix<BigInteger> generateIdentityMatrix(int rows, int columns)
    {
        BigInteger[][] identityMatrix = new BigInteger[rows][columns];
        
        for (int row = 0; row < identityMatrix.length; row++)
        {
            for (int column = 0; column < identityMatrix[row].length; column++)
            {
                identityMatrix[row][column] = (row == column ? BigInteger.ONE : BigInteger.ZERO);
            }
        }
        
        return new BigIntegerMatrix(identityMatrix);
    }
}