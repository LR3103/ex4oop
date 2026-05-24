/**
 * Represents a logical AND expression.
 * Extends BinaryExpression to handle two sub-expressions.
 */
public class And extends BinaryExpression {

    /**
     * Constructs an AND expression with two given sub-expressions.
     * @param left The left-hand side expression.
     * @param right The right-hand side expression.
     */
    public And(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Implements the logical AND function.
     * @param left The boolean value of the left expression.
     * @param right The boolean value of the right expression.
     * @return The result of the logical AND operation.
     */
    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return left && right;
    }

    /**
     * Returns the string representation of the logical sign for AND.
     * @return The string "&".
     */
    @Override
    public String getLogicalSign() {
        return "&";
    }

    /**
     * Reconstructs a new AND expression with new left and right sub-expressions.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param newLeft The new left-hand side expression.
     * @param newRight The new right-hand side expression.
     * @return A new And expression.
     */
    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new And(newLeft, newRight);
    }

    /**
     * Converts the current AND expression into an equivalent expression using only NAND operations.
     * The formula for A AND B using NAND is: (A NAND B) NAND (A NAND B).
     * @return An expression tree representing the original AND expression using only NAND gates.
     */
    @Override
    public Expression nandify() {
        // Formula: (A Nand B) Nand (A Nand B)
        // Compute the sub-trees exactly once to recycle the object instances
        Expression nandChild = new Nand(this.getLeft().nandify(), this.getRight().nandify());

        // Pass the same instance to both sides of the parent Nand
        return new Nand(nandChild, nandChild);
    }

    /**
     * Converts the current AND expression into an equivalent expression using only NOR operations.
     * The formula for A AND B using NOR is: NOR(NOR(A, A), NOR(B, B)).
     * @return An expression tree representing the original AND expression using only NOR gates.
     */
    @Override
    public Expression norify() {
        // Formula: NOR(NOR(A, A), NOR(B, B))
        // Compute each side's norified tree once and recycle it
        Expression norLeft = this.getLeft().norify();
        Expression norRight = this.getRight().norify();

        return new Nor(
                new Nor(norLeft, norLeft),
                new Nor(norRight, norRight)
        );
    }

    /**
     * Simplifies the current AND expression based on logical identities.
     * Simplification rules include:
     * - If both sub-expressions are constants, evaluate to a constant.
     * - x & x = x
     * - x & T = x
     * - x & F = F
     * @return A simplified version of the AND expression.
     */
    @Override
    public Expression simplifierLogic(Expression left, Expression right) {
        // Step 3: x & x = x
        if (left.toString().equals(right.toString())) {
            return left;
        }

        // Step 4: x & T = x (and commutative)
        if (isTrue(left)) return right;
        if (isTrue(right)) return left;

        // Step 4.5: x & F = F (and commutative)
        if (isFalse(left)) return new Val(false);
        if (isFalse(right)) return new Val(false);

        // Step 5: Fallback
        return null;
    }
}
