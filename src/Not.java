/**
 * Represents a logical NOT expression.
 * Extends UnaryExpression to handle a single sub-expression.
 */
public class Not extends UnaryExpression {

    /**
     * Constructs a NOT expression with the given sub-expression.
     * @param expression The expression to negate.
     */
    public Not(Expression expression) {
        super(expression);
    }

    /**
     * Implements the logical NOT function.
     * @param val The boolean value of the inner expression.
     * @return The result of the logical NOT operation.
     */
    @Override
    protected boolean logicalFunction(boolean val) {
        return !val;
    }

    /**
     * Returns the string representation of the logical sign for NOT.
     * @return The string "~".
     */
    @Override
    public String getLogicalSign() {
        return "~";
    }

    /**
     * Reconstructs a new NOT expression with a new inner expression.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param expression The new inner expression.
     * @return A new Not expression.
     */
    @Override
    protected Expression reconstruct(Expression expression) {
        return new Not(expression);
    }

    /**
     * Overrides the default assignNot behavior to handle double negation.
     * If this is a Not expression, applying another Not cancels it out,
     * returning its inner expression.
     * @return The inner expression, effectively simplifying ~(~X) to X.
     */
    @Override
    public Expression assignNot() {
        // Polymorphism at its finest! Two NOTs cancel out, so we just return the inner child.
        return this.getExpression();
    }

    /**
     * Converts the current NOT expression into an equivalent expression using only NAND operations.
     * The formula for NOT A using NAND is: A NAND A.
     * @return An expression tree representing the original NOT expression using only NAND gates.
     */
    @Override
    public Expression nandify() {
        // Transform the inner expression exactly once
        Expression nandChild = this.getExpression().nandify();

        // Recycle the instance to create the NOT logic: (A NAND A)
        return new Nand(nandChild, nandChild);
    }

    /**
     * Converts the current NOT expression into an equivalent expression using only NOR operations.
     * The formula for NOT A using NOR is: A NOR A.
     * @return An expression tree representing the original NOT expression using only NOR gates.
     */
    @Override
    public Expression norify() {
        // Transform the inner expression exactly once
        Expression norChild = this.getExpression().norify();

        // Recycle the instance to create the NOT logic: (A NOR A)
        return new Nor(norChild, norChild);
    }

    /**
     * Simplifies the current NOT expression based on logical identities.
     * Simplification rules include:
     * - If the sub-expression is a constant, evaluate to a constant.
     * - ~(~X) = X (handled polymorphically by assignNot()).
     * @return A simplified version of the NOT expression.
     */
    @Override
    public Expression simplify() {
        // Step 1: Simplify the single child first
        Expression simChild = this.getExpression().simplify();

        // Step 2: No variables -> evaluate to its result
        if (simChild.getVariables().isEmpty()) {
            try {
                return new Val(new Not(simChild).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through
            }
        }

        // Step 3 & 4: Polymorphism!
        // We don't check if simChild is a Not gate. We just TELL it to negate itself.
        // If it's a Var/And/Or, it returns new Not(simChild).
        // If it is ALREADY a Not gate, it cancels out and returns its inner child!
        return simChild.assignNot();
    }
}
