import java.util.List;

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
     * @param left The simplified left sub-expression.
     * @param right The simplified right sub-expression.
     * @return A simplified expression if a rule applies, otherwise null.
     */
    @Override
    public Expression simplifierLogic(Expression left, Expression right) {
        // Step 3: x | x = x
        if (left.toString().equals(right.toString())) {
            return left;
        }

        // Step 4: x | T = T (and commutative)
        if (isTrue(left)) {
            return new Val(true);
        }
        if (isTrue(right)) {
            return new Val(true);
        }

        // Step 4.5: x | F = x (and commutative)
        if (isFalse(left)) {
            return right;
        }
        if (isFalse(right)) {
            return left;
        }

        // Step 5: Fallback if no rules apply
        return null;
    }

    /**
     * Simplifies the current OR expression by recursively simplifying its sub-expressions
     * and applying specific OR simplification rules.
     * @return A simplified version of the OR expression.
     */
    @Override
    public Expression simplify() {
        return super.simplify();
    }

    /**
     * Applies a NOT operation to this OR expression.
     * By default, it wraps the current expression in a new Not gate.
     * @return A new Not expression wrapping this instance.
     */
    @Override
    public Expression assignNot() {
        return super.assignNot();
    }

    /**
     * Returns a string representation of the OR expression.
     * The format is typically "(left_expression | right_expression)".
     * @return A string representing the OR expression.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Returns a list of the unique variables present in this OR expression.
     * @return A list of variable names.
     */
    @Override
    public List<String> getVariables() {
        return super.getVariables();
    }
}
