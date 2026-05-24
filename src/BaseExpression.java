import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An abstract base class that provides common functionality for all boolean expressions.
 * Implements the Expression interface and offers helper methods for simplification.
 */
public abstract class BaseExpression implements Expression {

    /**
     * Returns the logical sign (operator symbol) of the expression.
     *
     * @return The string representation of the operator.
     */
    public abstract String getLogicalSign();

    /**
     * Checks if an expression evaluates to a constant true value.
     * This method assumes the expression has already been simplified to a point where
     * it either contains no variables or its constant nature is evident.
     *
     * @param e The expression to check.
     * @return true if the expression evaluates to true, false otherwise.
     */
    protected boolean isTrue(Expression e) {
        // If it has variables, it definitely isn't a constant True
        if (!e.getVariables().isEmpty()) return false;

        try {
            return e.evaluate(); // Only evaluates if it's purely constants
        } catch (Exception ex) {
            // This should ideally not be reached if getVariables().isEmpty() is true,
            // as evaluation of constants should not throw an exception.
            return false;
        }
    }

    /**
     * Checks if an expression evaluates to a constant false value.
     * This method assumes the expression has already been simplified to a point where
     * it either contains no variables or its constant nature is evident.
     *
     * @param e The expression to check.
     * @return true if the expression evaluates to false, false otherwise.
     */
    protected boolean isFalse(Expression e) {
        if (!e.getVariables().isEmpty()) return false;

        try {
            return !e.evaluate();
        } catch (Exception ex) {
            // This should ideally not be reached if getVariables().isEmpty() is true,
            // as evaluation of constants should not throw an exception.
            return false;
        }
    }

    /**
     * Default implementation for applying a NOT operation to this expression.
     * By default, most expressions are simply wrapped in a new Not gate.
     * This method is designed to be overridden by the Not class itself for double negation simplification.
     *
     * @return A new Not expression wrapping this instance.
     */
    public Expression assignNot() {
        // By default, most objects just wrap themselves in a Not gate.
        return new Not(this);
    }

}
