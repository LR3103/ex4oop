/**
 * Represents a logical OR expression.
 * Extends BinaryExpression to handle two sub-expressions.
 */
public class Or extends BinaryExpression {

    /**
     * Constructs an OR expression with two given sub-expressions.
     * @param left The left-hand side expression.
     * @param right The right-hand side expression.
     */
    public Or(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Implements the logical OR function.
     * @param left The boolean value of the left expression.
     * @param right The boolean value of the right expression.
     * @return The result of the logical OR operation.
     */
    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return left || right;
    }

    /**
     * Returns the string representation of the logical sign for OR.
     * @return The string "|".
     */
    @Override
    public String getLogicalSign() {
        return "|";
    }

    /**
     * Reconstructs a new OR expression with new left and right sub-expressions.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param newLeft The new left-hand side expression.
     * @param newRight The new right-hand side expression.
     * @return A new Or expression.
     */
    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Or(newLeft, newRight);
    }

    /**
     * Converts the current OR expression into an equivalent expression using only NAND operations.
     * The formula for A OR B using NAND is: (NOT A) NAND (NOT B), which is ((A NAND A) NAND (B NAND B)).
     * @return An expression tree representing the original OR expression using only NAND gates.
     */
    @Override
    public Expression nandify() {
        // 1. Evaluate the sub-trees exactly once
        Expression nandLeft = this.getLeft().nandify();
        Expression nandRight = this.getRight().nandify();

        // 2. Recycle those instances into the final NAND structure
        return new Nand(
                new Nand(nandLeft, nandLeft),
                new Nand(nandRight, nandRight)
        );
    }

    /**
     * Converts the current OR expression into an equivalent expression using only NOR operations.
     * The formula for A OR B using NOR is: NOT(A NOR B), which is (A NOR B) NOR (A NOR B).
     * @return An expression tree representing the original OR expression using only NOR gates.
     */
    @Override
    public Expression norify() {
        // Formula: NOT(NOR(A, B)) -> which matches your implementation
        return new Nor(
                new Nor(this.getLeft().norify(), this.getRight().norify()),
                new Nor(this.getLeft().norify(), this.getRight().norify())
        );
    }

    /**
     * Simplifies the current OR expression based on logical identities.
     * Simplification rules include:
     * - If both sub-expressions are constants, evaluate to a constant.
     * - x | x = x
     * - x | T = T
     * - x | F = x
     * @return A simplified version of the OR expression.
     */
    @Override
    public Expression simplify() {
        // Step 1: Simplify children first
        Expression simLeft = this.getLeft().simplify();
        Expression simRight = this.getRight().simplify();

        // Step 2: No variables -> evaluate to its result
        if (simLeft.getVariables().isEmpty() && simRight.getVariables().isEmpty()) {
            try {
                // Evaluates purely constant expressions (e.g., T | F -> T)
                return new Val(new Or(simLeft, simRight).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through to Step 5
            }
        }

        // Step 3: x | x = x
        if (simLeft.toString().equals(simRight.toString())) {
            return simLeft;
        }

        // Step 4: x | T = T (and commutative)
        if (isTrue(simLeft)) return new Val(true);
        if (isTrue(simRight)) return new Val(true);

        // Step 4.5: x | F = x (and commutative)
        if (isFalse(simLeft)) return simRight;
        if (isFalse(simRight)) return simLeft;

        // Step 5: Fallback if no rules apply
        return new Or(simLeft, simRight);
    }
}
