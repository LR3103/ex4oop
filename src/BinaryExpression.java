import java.util.Map;

/**
 * An abstract base class for binary boolean expressions (expressions with two operands).
 * Extends BaseExpression and provides common functionality for two-operand logical operations.
 */
public abstract class BinaryExpression extends BaseExpression {
    private Expression left;
    private Expression right;

    /**
     * Constructs a BinaryExpression with two given sub-expressions.
     * @param left The left-hand side expression.
     * @param right The right-hand side expression.
     */
    BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Defines the specific logical function for the binary operation.
     * Subclasses must implement this to define their behavior (e.g., AND, OR, XOR).
     * @param left The boolean value of the left expression.
     * @param right The boolean value of the right expression.
     * @return The result of applying the binary logical function.
     */
    protected abstract boolean logicalFunction(boolean left, boolean right);

    /**
     * Reconstructs a new binary expression with new left and right sub-expressions.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param newLeft The new left-hand side expression.
     * @param newRight The new right-hand side expression.
     * @return A new binary expression of the same type.
     */
    protected abstract Expression reconstruct(Expression newLeft, Expression newRight);

    /**
     * Returns the left-hand side sub-expression.
     * @return The left expression.
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Returns the right-hand side sub-expression.
     * @return The right expression.
     */
    public Expression getRight() {
        return right;
    }

    /**
     * Evaluates the binary expression using an empty assignment.
     * @return The boolean result of the expression.
     * @throws Exception If either sub-expression contains variables.
     */
    @Override
    public Boolean evaluate() throws Exception {
        return logicalFunction(this.left.evaluate(), this.right.evaluate());
    }

    /**
     * Evaluates the binary expression using the provided variable assignment.
     * @param assignment A map containing variable names and their boolean values.
     * @return The boolean result of the expression.
     * @throws Exception If a variable in either sub-expression is not found in the assignment.
     */
    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return logicalFunction(this.left.evaluate(assignment), this.right.evaluate(assignment));
    }

    /**
     * Returns a list of the unique variables present in both the left and right sub-expressions.
     * @return A list of unique variable names.
     */
    @Override
    public java.util.List<String> getVariables() {
        // 1. Create a Set to automatically prevent duplicates
        java.util.Set<String> uniqueVars = new java.util.HashSet<>();

        // 2. Add all variables from the left and right children
        uniqueVars.addAll(this.left.getVariables());
        uniqueVars.addAll(this.right.getVariables());

        // 3. Convert the Set into a brand new List and return it
        return new java.util.ArrayList<>(uniqueVars);
    }

    /**
     * Applies specific simplification logic for the binary expression.
     * This method is part of the Template Method pattern for simplification.
     * Subclasses should override this to apply their specific simplification rules.
     *
     * @param left The simplified left sub-expression.
     * @param right The simplified right sub-expression.
     * @return A simplified expression if a rule applies, otherwise null.
     */
    @Override
    public Expression simplifierLogic(Expression left, Expression right) {
        return null;
    }

    /**
     * Returns a string representation of the binary expression.
     * The format is typically "(left_expression SIGN right_expression)".
     * @return A string representing the binary expression.
     */
    @Override
    public String toString() {
        return "(" + this.left.toString() + " " + this.getLogicalSign() + " " + this.right.toString() + ")";
    }

    /**
     * Simplifies the current binary expression based on logical identities.
     * This method uses the Template Method pattern, delegating specific simplification
     * rules to the `simplifierLogic` method in concrete subclasses.
     *
     * @return A simplified version of the binary expression.
     */
    @Override
    public Expression simplify() {
        // Step 1: Simplify children first
        Expression simLeft = this.getLeft().simplify();
        Expression simRight = this.getRight().simplify();
        // Step 2: No variables -> evaluate to its result
        if (simLeft.getVariables().isEmpty() && simRight.getVariables().isEmpty()) {
            try {
                // Evaluates purely constant expressions (e.g., T ^ T -> F)
                return new Val(reconstruct(simLeft, simRight).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through to Step 5
            }
        }
        Expression simplifiedExpression = this.simplifierLogic(simLeft, simRight);
        return (simplifiedExpression != null) ? simplifiedExpression : reconstruct(simLeft, simRight);
    }

    /**
     * Returns a new expression in which all occurrences of the specified variable
     * in both sub-expressions are replaced with the provided expression.
     * @param var The name of the variable to replace.
     * @param expression The expression to substitute.
     * @return A new binary expression with the variable replaced in both its left and right parts.
     */
    @Override
    public Expression assign(String var, Expression expression) {
        return reconstruct(this.left.assign(var, expression), this.right.assign(var, expression));
    }

    /**
     * Applies a NOT operation to this binary expression.
     * By default, it wraps the current expression in a new Not gate.
     * @return A new Not expression wrapping this instance.
     */
    @Override
    public Expression assignNot() {
        return super.assignNot();
    }
}
