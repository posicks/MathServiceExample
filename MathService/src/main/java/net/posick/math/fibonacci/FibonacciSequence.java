package net.posick.math.fibonacci;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Fibonacci sequence container class. Primarily used for serialization and Marshalling
 * 
 * @author Steve Posick
 */
@XmlRootElement(name="FibonacciSequence", namespace="http://posick.net/math")
public class FibonacciSequence implements Serializable
{
    private static final long serialVersionUID = 201511241050L;
    
    @XmlAttribute(name="start")
    private int start = 0;
    
    @XmlAttribute(name="length")
    private int length = 0;
    
    @XmlElement(name="Fibonacci", namespace="http://posick.net/math")
    private BigInteger[] fibonacci;
    
    
    public FibonacciSequence()
    {
    }
    
    
    public FibonacciSequence(int start, Number[] fibonacci, int expectedLength)
    {
        if (fibonacci != null)
        {
            BigInteger[] temp = new BigInteger[fibonacci.length];
            
            for (int index = 0; index < temp.length; index++)
            {
                temp[index] = BigInteger.valueOf(fibonacci[index].longValue());
            }
            this.fibonacci = temp;
        } else
        {
            this.fibonacci = null;
        }
        
        this.start = start;
        this.length = expectedLength;
    }
    
    
    public FibonacciSequence(int start, BigInteger[] fibonacci, int expectedLength)
    {
        this.fibonacci = fibonacci == null ? null : Arrays.copyOf(fibonacci, fibonacci.length);
        this.start = start;
        this.length = expectedLength;
    }
    
    
    public BigInteger[] getSequence()
    {
        return fibonacci.length <= this.length ? fibonacci : Arrays.copyOf(fibonacci, this.length);
    }
    
    
    public int getStart()
    {
        return start;
    }
    
    
    public int length()
    {
        return length;
    }
    
    
    @XmlAttribute(name="empty")
    public boolean isEmpty()
    {
        return fibonacci == null || fibonacci.length == 0;
    }
    
    
    @XmlAttribute(name="complete")
    public boolean isComplete()
    {
        return fibonacci != null && fibonacci.length >= length;
    }


    public FibonacciSequence append(FibonacciSequence seq)
    {
        if (!seq.isEmpty())
        {
            int thisLength = fibonacci.length;
            int thatLength = seq.getSequence().length;
            BigInteger[] temp = new BigInteger[thisLength + thatLength];
            
            System.arraycopy(fibonacci, 0, temp, 0, thisLength);
            System.arraycopy(seq.fibonacci, 0, temp, thisLength, thatLength);
            
            return new FibonacciSequence(start, temp, thisLength + seq.length());
        } else
        {
            return this;
        }
    }
}