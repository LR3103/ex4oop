/**
 * Represents a logical NAND (NOT AND) expression.
 * Extends BinaryExpression to handle two sub-expressions.
 */
public class Nand extends BinaryExpression {

    /**
     * Constructs a NAND expression with two given sub-expressions.
     * @param left The left-hand side expression.
     * @param right The right-hand side expression.
     */
    public Nand(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Implements the logical NAND function.
     * @param left The boolean value of the left expression.
     * @param right The boolean value of the right expression.
     * @return The result of the logical NAND operation.
     */
    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return !(left && right);
    }

    /**
     * Returns the string representation of the logical sign for NAND.
     * @return The string "A".
     */
    @Override
    public String getLogicalSign() {
        return "A";
    }

    /**
     * Reconstructs a new NAND expression with new left and right sub-expressions.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param newLeft The new left-hand side expression.
     * @param newRight The new right-hand side expression.
     * @return A new Nand expression.
     */
    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Nand(newLeft, newRight);
    }

    /**
     * Converts the current NAND expression into an equivalent expression using only NAND operations.
     * Since it's already a NAND gate, it just recursively nandifies its children.
     * @return An expression tree representing the original NAND expression using only NAND gates.
     */
    @Override
    public Expression nandify() {
        return reconstruct(getLeft().nandify(), getRight().nandify());
    }

    /**
     * Converts the current NAND expression into an equivalent expression using only NOR operations.
     * The formula for A NAND B using NOR is: NOR(NOR(A NOR A, B NOR B), NOR(A NOR A, B NOR B)).
     * @return An expression tree representing the original NAND expression using only NOR gates.
     */
    @Override
    public Expression norify() {
        // 1. Compute and recycle the deepest child sub-trees
        Expression left = new Nor(this.getLeft().norify(), this.getLeft().norify());
        Expression right = new Nor(this.getRight().norify(), this.getRight().norify());

        // 2. Compute the middle NOR layer exactly once to recycle it
        Expression middleNor = new Nor(left, right);

        // 3. Return the root node using the recycled middle layer
        return new Nor(middleNor, middleNor);
    }

    /**
     * Simplifies the current NAND expression based on logical identities.
     * Simplification rules include:
     * - If both sub-expressions are constants, evaluate to a constant.
     * - x A x = ~(x)
     * - x A T = ~(x)
     * - x A F = T
     * @return A simplified version of the NAND expression.
     */
    @Override
    public Expression simplifierLogic(Expression left, Expression right) {
        // Step 3: x A x = ~(x)
        if (left.toString().equals(right.toString())) {
            return left.assignNot();
        }

        // Step 4: x A T = ~(x)
        if (isTrue(left)) return right.assignNot();
        if (isTrue(right)) return left.assignNot();

        // Step 4.5: x A F = T (and commutative)
        // If ANY side is False, an And would be False, so a Nand is ALWAYS True
        if (isFalse(left)) return new Val(true);
        if (isFalse(right)) return new Val(true);

        // Step 5: Fallback if no rules apply
        return null;
    }
}
