import java.util.List;
import java.util.Map;

/**
 * An abstract base class for unary boolean expressions (expressions with one operand).
 * Extends BaseExpression and provides common functionality for single-operand operations.
 */
public abstract class UnaryExpression extends BaseExpression {
    private Expression expression;

    /**
     * Constructs a UnaryExpression with the given inner expression.
     * @param expression The single sub-expression this unary operation acts upon.
     */
    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    /**
     * Defines the specific logical function for the unary operation.
     * Subclasses must implement this to define their behavior (e.g., NOT).
     * @param val The boolean value of the inner expression.
     * @return The result of applying the unary logical function.
     */
    protected abstract boolean logicalFunction(boolean val);

    /**
     * Returns the inner expression of this unary operation.
     * @return The sub-expression.
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * Evaluates the unary expression using an empty assignment.
     * @return The boolean result of the expression.
     * @throws Exception If the inner expression contains variables.
     */
    @Override
    public Boolean evaluate() throws Exception {
        return logicalFunction(this.expression.evaluate());
    }

    /**
     * Evaluates the unary expression using the provided variable assignment.
     * @param assignment A map containing variable names and their boolean values.
     * @return The boolean result of the expression.
     * @throws Exception If a variable in the inner expression is not found in the assignment.
     */
    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return logicalFunction(this.expression.evaluate(assignment));
    }

    /**
     * Returns a list of the unique variables present in the inner expression.
     * @return A list of variable names.
     */
    @Override
    public List<String> getVariables() {
        return this.expression.getVariables();
    }

    /**
     * Returns a string representation of the unary expression.
     * The format is typically "(SIGN(expression))".
     * @return A string representing the unary expression.
     */
    @Override
    public String toString() {
        return "(" + this.getLogicalSign() + "(" + this.expression.toString() + ")" + ")";
    }

    /**
     * Reconstructs a new unary expression with a new inner expression.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param expression The new inner expression.
     * @return A new unary expression of the same type.
     */
    protected abstract Expression reconstruct(Expression expression);

    /**
     * Applies specific simplification logic for the unary expression.
     * This method is part of the Template Method pattern for simplification.
     * Subclasses should override this to apply their specific simplification rules.
     *
     * @param expression The simplified sub-expression.
     * @return A simplified expression if a rule applies, otherwise null.
     */
    @Override
    public Expression simplifierLogic(Expression expression) {
        return null;
    }

    /**
     * Simplifies the current unary expression based on logical identities.
     * This method uses the Template Method pattern, delegating specific simplification
     * rules to the `simplifierLogic` method in concrete subclasses.
     *
     * @return A simplified version of the unary expression.
     */
    @Override
    public Expression simplify() {
        // Step 1: Simplify the single child first
        Expression simChild = this.getExpression().simplify();
        // Step 2: No variables -> evaluate to its result
        if (simChild.getVariables().isEmpty()) {
            try {
                return new Val(reconstruct(simChild).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through
            }
        }
        Expression simplified = this.simplifierLogic(simChild);
        return (simplified != null) ? simplified : reconstruct(simChild);
    }


    /**
     * Returns a new expression in which all occurrences of the specified variable
     * in the inner expression are replaced with the provided expression.
     * @param var The name of the variable to replace.
     * @param expression The expression to substitute.
     * @return A new unary expression with the variable replaced in its inner part.
     */
    @Override
    public Expression assign(String var, Expression expression) {
        return reconstruct(this.expression.assign(var, expression));
    }

    /**
     * Applies a NOT operation to this unary expression.
     * By default, it wraps the current expression in a new Not gate.
     * @return A new Not expression wrapping this instance.
     */
    @Override
    public Expression assignNot() {
        return super.assignNot();
    }
}
