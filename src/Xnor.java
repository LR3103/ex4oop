import java.util.List;

/**
 * Represents a logical XNOR (exclusive NOR) expression.
 * Extends BinaryExpression to handle two sub-expressions.
 */
public class Xnor extends BinaryExpression {

    /**
     * Constructs an XNOR expression with two given sub-expressions.
     * @param left The left-hand side expression.
     * @param right The right-hand side expression.
     */
    public Xnor(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Implements the logical XNOR function.
     * @param left The boolean value of the left expression.
     * @param right The boolean value of the right expression.
     * @return The result of the logical XNOR operation.
     */
    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return left == right;
    }

    /**
     * Returns the string representation of the logical sign for XNOR.
     * @return The string "#".
     */
    @Override
    public String getLogicalSign() {
        return "#";
    }

    /**
     * Reconstructs a new XNOR expression with new left and right sub-expressions.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param newLeft The new left-hand side expression.
     * @param newRight The new right-hand side expression.
     * @return A new Xnor expression.
     */
    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Xnor(newLeft, newRight);
    }

    /**
     * Converts the current XNOR expression into an equivalent expression using only NAND operations.
     * The formula for A XNOR B using NAND is: ( (A NAND A) NAND (B NAND B) ) NAND (A NAND B).
     * @return An expression tree representing the original XNOR expression using only NAND gates.
     */
    @Override
    public Expression nandify() {
        // 1. Compute child sub-trees exactly once
        Expression nandLeft = this.getLeft().nandify();
        Expression nandRight = this.getRight().nandify();

        // 2. Build the standard (A Nand B) structure once so it can be reused
        Expression standardNand = new Nand(nandLeft, nandRight);

        // 3. Assemble the final tree, recycling all components
        return new Nand(
                new Nand(
                        new Nand(nandLeft, nandLeft),
                        new Nand(nandRight, nandRight)
                ),
                standardNand
        );
    }

    /**
     * Converts the current XNOR expression into an equivalent expression using only NOR operations.
     * The formula for A XNOR B using NOR is: NOR(NOR(A, NOR(A, B)), NOR(B, NOR(A, B))).
     * @return An expression tree representing the original XNOR expression using only NOR gates.
     */
    @Override
    public Expression norify() {
        // 1. Compute norified child sub-trees exactly once
        Expression norLeft = this.getLeft().norify();
        Expression norRight = this.getRight().norify();

        // 2. The shared middle piece: NOR(A, B)
        Expression middleSharedNor = new Nor(norLeft, norRight);

        // 3. Build the final Xnor tree, recycling the shared middle piece
        return new Nor(
                new Nor(norLeft, middleSharedNor),
                new Nor(norRight, middleSharedNor)
        );
    }

    /**
     * Simplifies the current XNOR expression based on logical identities.
     * Simplification rules include:
     * - If both sub-expressions are constants, evaluate to a constant.
     * - x # x = T
     * Note: Rules for x # T and x # F are not implemented as per assignment.
     * @param left The simplified left sub-expression.
     * @param right The simplified right sub-expression.
     * @return A simplified expression if a rule applies, otherwise null.
     */
    @Override
    public Expression simplifierLogic(Expression left, Expression right) {
        // Step 3: x # x = T
        // If the two sides are identical, Xnor always returns True
        if (left.toString().equals(right.toString())) {
            return new Val(true);
        }

        return null;
    }

    /**
     * Simplifies the current XNOR expression by recursively simplifying its sub-expressions
     * and applying specific XNOR simplification rules.
     * @return A simplified version of the XNOR expression.
     */
    @Override
    public Expression simplify() {
        return super.simplify();
    }

    /**
     * Applies a NOT operation to this XNOR expression.
     * By default, it wraps the current expression in a new Not gate.
     * @return A new Not expression wrapping this instance.
     */
    @Override
    public Expression assignNot() {
        return super.assignNot();
    }

    /**
     * Returns a string representation of the XNOR expression.
     * The format is typically "(left_expression # right_expression)".
     * @return A string representing the XNOR expression.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Returns a list of the unique variables present in this XNOR expression.
     * @return A list of variable names.
     */
    @Override
    public List<String> getVariables() {
        return super.getVariables();
    }

}
