package javaexample17overflow;

public class JavaExample17Overflow {

    public static void main(String[] args) {

        // source: https://wiki.sei.cmu.edu/confluence/display/java/NUM00-J.+Detect+or+prevent+integer+overflow 
    }// end main()

    // =========================================================================
    // ==== Precondition Testing ===============================================
    static final int safeAdd(int left, int right) {
        if (right > 0 ? left > Integer.MAX_VALUE - right
                : left < Integer.MIN_VALUE - right) {
            throw new ArithmeticException("Integer overflow");
        }
        return left + right;
    }

    static final int safeSubtract(int left, int right) {
        if (right > 0 ? left < Integer.MIN_VALUE + right
                : left > Integer.MAX_VALUE + right) {
            throw new ArithmeticException("Integer overflow");
        }
        return left - right;
    }

    static final int safeMultiply(int left, int right) {
        if (right > 0 ? left > Integer.MAX_VALUE / right
                || left < Integer.MIN_VALUE / right
                : (right < -1 ? left > Integer.MIN_VALUE / right
                        || left < Integer.MAX_VALUE / right
                        : right == -1
                        && left == Integer.MIN_VALUE)) {
            throw new ArithmeticException("Integer overflow");
        }
        return left * right;
    }

    static final int safeDivide(int left, int right) {
        if ((left == Integer.MIN_VALUE) && (right == -1)) {
            throw new ArithmeticException("Integer overflow");
        }
        return left / right;
    }

    static final int safeNegate(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer overflow");
        }
        return -a;
    }

    static final int safeAbs(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer overflow");
        }
        return Math.abs(a);
    }
    //==========================================================================
    //==========================================================================

    //**************************************************************************
    //**************************************************************************
    // Compliant Solution (Java 8, Math.*Exact())
    // This compliant solution uses the addExact() and multiplyExact() methods defined in the Math class. 
    // These methods were added to Java as part of the Java 8 release, and they also either return a mathematically correct value or throw ArithmeticException. 
    // The Math class also provides SubtractExact() and negateExact() but does not provide any methods for safe division or absolute value.
    public static int multAccum(int oldAcc, int newVal, int scale) {
        return Math.addExact(oldAcc, Math.multiplyExact(newVal, scale));
    }
    //**************************************************************************
    //**************************************************************************

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Compliant Solution (Upcasting)
    // This compliant solution shows the implementation of a method 
    // for checking whether a value of type long falls within the representable range of an int using the upcasting technique. 
    // The implementations of range checks for the smaller primitive integer types are similar.
    public static long intRangeCheck(long value) {
        if ((value < Integer.MIN_VALUE) || (value > Integer.MAX_VALUE)) {
            throw new ArithmeticException("Integer overflow");
        }
        return value;
    }

    public static int multAccum2(int oldAcc, int newVal, int scale) {           // I put 2 in multAccum2
        final long res = intRangeCheck(
                ((long) oldAcc) + intRangeCheck((long) newVal * (long) scale)
        );
        return (int) res; // Safe downcast
    }
    // Note that this approach cannot be applied to values of type long because long is the largest primitive integral type. 
    // Use the BigInteger technique instead when the original variables are of type long.
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}

/*
(1)Precondition testing. Check the inputs to each arithmetic operator to ensure that overflow cannot occur. 
Throw an ArithmeticException when the operation would overflow if it were performed; otherwise, perform the operation.

(2)Upcasting. Cast the inputs to the next larger primitive integer type and perform the arithmetic in the larger size. 
Check each intermediate result for overflow of the original smaller type and throw an ArithmeticException if the range check fails. 
Note that the range check must be performed after each arithmetic operation; larger expressions without per-operation bounds checking can overflow the larger type. 
Downcast the final result to the original smaller type before assigning to a variable of the original smaller type. 
This approach cannot be used for type long because long is already the largest primitive integer type.

(3)BigInteger. Convert the inputs into objects of type BigInteger and perform all arithmetic using BigInteger methods. 
Type BigInteger is the standard arbitrary-precision integer type provided by the Java standard libraries. 
The arithmetic operations implemented as methods of this type cannot overflow; instead, they produce the numerically correct result. 
Consequently, compliant code performs only a single range check just before converting the final result to the original smaller type and throws an ArithmeticException
if the final result is outside the range of the original smaller type.

 */
 /*
Exceptions
(*) NUM00-J-EX0: Depending on circumstances, integer overflow could be benign. 
For example, many algorithms for computing hash codes use modular arithmetic, intentionally allowing overflow to occur. 
Such benign uses must be carefully documented.

(*) NUM00-J-EX1: Prevention of integer overflow is unnecessary for numeric fields that undergo bitwise operations 
and not arithmetic operations (see NUM01-J. Do not perform bitwise and arithmetic operations on the same data for more information).
 */
