package net.posick.math.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.posick.math.Matrix;

public class BigDecimalMatrix extends Matrix<BigDecimal>
{
    private static final long serialVersionUID = 201512031704L;
    

    protected BigDecimalMatrix()
    {
        super();
    }


    public BigDecimalMatrix(BigDecimal[][] matrix)
    {
        super(matrix);
    }


    @Override
    public Matrix<BigDecimal> multiply(Matrix<? extends Number> that)
    throws IllegalArgumentException
    {
        multiplicationCheck(this, that);
        
        TYPE type = that.type();
        
        int m1Rows = this.rows;
        int m1Columns = this.columns;
        int m2Columns = that.columns();
        
        BigDecimal[][] newData = new BigDecimal[m1Rows][m2Columns];
        
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
                    BigDecimal thisValue = ((thisValue = this.value(m1Row, index)) == null ? BigDecimal.ZERO : thisValue);
                    BigDecimal thatValue;
                    
                    switch (type)
                    {
                        case DECIMAL:
                            thatValue = (BigDecimal) that.value(index, m2Column);
                            thatValue = (thatValue == null ? BigDecimal.ZERO : thatValue);
                            break;
                        case INTEGER:
                            BigInteger bigInt = (BigInteger) that.value(index, m2Column);
                            thatValue = (bigInt == null ? BigDecimal.ZERO : new BigDecimal(bigInt));
                            break;
                        default:
                            thatValue = BigDecimal.ZERO;
                            break;
                    }
                    
                    BigDecimal value = ((value = newData[m1Row][m2Column]) == null ? BigDecimal.ZERO : value);
                    newData[m1Row][m2Column] = value.add(thisValue.multiply(thatValue));
                }
            }
        }
        
        return new BigDecimalMatrix(newData);
    }
    
    
    public static Matrix<BigDecimal> generateIdentityMatrix(int rows, int columns)
    {
        BigDecimal[][] identityMatrix = new BigDecimal[rows][columns];
        
        for (int row = 0; row < identityMatrix.length; row++)
        {
            for (int column = 0; column < identityMatrix[row].length; column++)
            {
                identityMatrix[row][column] = (row == column ? BigDecimal.ONE : BigDecimal.ZERO);
            }
        }
        
        return new BigDecimalMatrix(identityMatrix);
    }
}