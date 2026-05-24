import java.util.List;
import java.util.Map;

/**
 * Represents a boolean expression.
 * This interface defines the contract for all types of boolean expressions
 * (variables, values, and logical operations).
 */
public interface Expression {
    /**
     * Evaluates the expression using the variable values provided in the assignment.
     *
     * @param assignment A map containing variable names and their boolean values.
     * @return The boolean result of the expression evaluation.
     * @throws Exception If the expression contains a variable which is not in the assignment.
     */
    Boolean evaluate(Map<String, Boolean> assignment) throws Exception;

    /**
     * A convenience method. Evaluates the expression using an empty assignment.
     * This method should only be used for expressions that contain no variables.
     *
     * @return The boolean result of the expression evaluation.
     * @throws Exception If the expression contains any variables.
     */
    Boolean evaluate() throws Exception;

    /**
     * Returns a sorted list of the unique variables in the expression.
     *
     * @return A list of variable names (Strings).
     */
    List<String> getVariables();

    /**
     * Returns a nice string representation of the expression.
     *
     * @return A string representing the expression.
     */
    String toString();

    /**
     * Returns a new expression in which all occurrences of the specified variable
     * are replaced with the provided expression.
     * This method does not modify the current expression.
     *
     * @param var The name of the variable to replace.
     * @param expression The expression to substitute in place of the variable.
     * @return A new expression with the variable replaced.
     */
    Expression assign(String var, Expression expression);

    /**
     * Returns the expression tree resulting from converting all the operations
     * to the logical NAND operation.
     *
     * @return A new expression tree composed entirely of NAND gates.
     */
    Expression nandify();

    /**
     * Returns the expression tree resulting from converting all the operations
     * to the logical NOR operation.
     *
     * @return A new expression tree composed entirely of NOR gates.
     */
    Expression norify();

    /**
     * Simplifies the current expression based on logical identities.
     *
     * @return A simplified version of the expression.
     */
    Expression simplify();

    /**
     * Returns a new expression that represents the logical NOT of this expression.
     * This method is used polymorphically for simplification, especially for double negation.
     *
     * @return A new expression representing NOT(this).
     */
    Expression assignNot();

}
