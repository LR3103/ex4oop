import java.util.List;

/**
 * Represents a logical NOR (NOT OR) expression.
 * Extends BinaryExpression to handle two sub-expressions.
 */
public class Nor extends BinaryExpression {

    /**
     * Constructs a NOR expression with two given sub-expressions.
     * @param left The left-hand side expression.
     * @param right The right-hand side expression.
     */
    public Nor(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Implements the logical NOR function.
     * @param left The boolean value of the left expression.
     * @param right The boolean value of the right expression.
     * @return The result of the logical NOR operation.
     */
    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return !(left || right);
    }

    /**
     * Returns the string representation of the logical sign for NOR.
     * @return The string "V".
     */
    @Override
    public String getLogicalSign() {
        return "V";
    }

    /**
     * Reconstructs a new NOR expression with new left and right sub-expressions.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param newLeft The new left-hand side expression.
     * @param newRight The new right-hand side expression.
     * @return A new Nor expression.
     */
    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Nor(newLeft, newRight);
    }

    /**
     * Converts the current NOR expression into an equivalent expression using only NAND operations.
     * The formula for A NOR B using NAND is: (NOT A NAND NOT B) NAND (NOT A NAND NOT B).
     * @return An expression tree representing the original NOR expression using only NAND gates.
     */
    @Override
    public Expression nandify() {
        // 1. Compute the deep sub-trees exactly once
        Expression nandLeft = this.getLeft().nandify();
        Expression nandRight = this.getRight().nandify();

        // 2. Build the inner layer (equivalent to NOT A NAND NOT B)
        Expression innerNand = new Nand(
                new Nand(nandLeft, nandLeft),
                new Nand(nandRight, nandRight)
        );

        // 3. Recycle the entire inner structure for the parent node
        return new Nand(innerNand, innerNand);
    }

    /**
     * Converts the current NOR expression into an equivalent expression using only NOR operations.
     * Since it's already a NOR gate, it just recursively norifies its children.
     * @return An expression tree representing the original NOR expression using only NOR gates.
     */
    @Override
    public Expression norify() {
        // Already a NOR gate, just recursively process the children
        return new Nor(this.getLeft().norify(), this.getRight().norify());
    }

    /**
     * Simplifies the current NOR expression based on logical identities.
     * Simplification rules include:
     * - If both sub-expressions are constants, evaluate to a constant.
     * - x V x = ~(x)
     * - x V T = F
     * - x V F = ~(x)
     * @param left The simplified left sub-expression.
     * @param right The simplified right sub-expression.
     * @return A simplified expression if a rule applies, otherwise null.
     */
    @Override
    public Expression simplifierLogic(Expression left, Expression right) {
        // Step 3: x V x = ~(x)
        // If the two sides are identical, Nor returns the NOT of that side
        if (left.toString().equals(right.toString())) {
            return new Not(left);
        }

        // Step 4: x V T = F (and commutative)
        // If ANY side is True, an Or would be True, so a Nor is ALWAYS False
        if (isTrue(left)) {
            return new Val(false);
        }
        if (isTrue(right)) {
            return new Val(false);
        }

        // Step 4.5: x V F = ~(x) (and commutative)
        // If one side is False, it has no effect on the Or, so Nor just inverts the other side
        if (isFalse(left)) {
            return right.assignNot();
        }
        if (isFalse(right)) {
            return left.assignNot();
        }

        // Step 5: Fallback if no rules apply
        return null;
    }

    /**
     * Simplifies the current NOR expression by recursively simplifying its sub-expressions
     * and applying specific NOR simplification rules.
     * @return A simplified version of the NOR expression.
     */
    @Override
    public Expression simplify() {
        return super.simplify();
    }

    /**
     * Applies a NOT operation to this NOR expression.
     * By default, it wraps the current expression in a new Not gate.
     * @return A new Not expression wrapping this instance.
     */
    @Override
    public Expression assignNot() {
        return super.assignNot();
    }

    /**
     * Returns a string representation of the NOR expression.
     * The format is typically "(left_expression V right_expression)".
     * @return A string representing the NOR expression.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Returns a list of the unique variables present in this NOR expression.
     * @return A list of variable names.
     */
    @Override
    public List<String> getVariables() {
        return super.getVariables();
    }
}
