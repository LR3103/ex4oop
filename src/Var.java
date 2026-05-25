import java.util.List;
import java.util.Map;

/**
 * Represents a variable in a boolean expression.
 * Implements the Expression interface.
 */
public class Var implements Expression {
    private String name;

    /**
     * Constructs a Var expression with the given variable name.
     * @param varName The name of the variable.
     */
    public Var(String varName) {
        this.name = varName;
    }

    /**
     * Evaluates the variable's value based on the provided assignment.
     * @param assignment A map containing variable names and their boolean values.
     * @return The boolean value assigned to this variable.
     * @throws Exception If the variable's name is not found in the assignment.
     */
    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        if (assignment.containsKey(this.name)) {
            return assignment.get(this.name);
        }
        throw new Exception("Variable " + this.name + " not found in assignment");
    }

    /**
     * Evaluates the variable's value without an assignment.
     * This method will always throw an exception as a variable cannot be evaluated without a value.
     * @return (Never returns)
     * @throws Exception Always throws an exception indicating a missing assignment.
     */
    @Override
    public Boolean evaluate() throws Exception {
        throw new Exception("Evaluate was called with no assignment's variable for" + this.name + ".");
    }

    /**
     * Returns a list containing only the name of this variable.
     * @return A list with one string, the variable's name.
     */
    @Override
    public List<String> getVariables() {
        java.util.List<String> list = new java.util.ArrayList<>();
        list.add(this.name);
        return list;
    }

    /**
     * Returns the string representation of the variable (its name).
     * @return The variable's name.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns a new expression where this variable is replaced by another expression
     * if its name matches the target variable. Otherwise, returns itself.
     * @param var The name of the variable to replace.
     * @param expression The expression to substitute.
     * @return The substituted expression if names match, otherwise the current Var instance.
     */
    @Override
    public Expression assign(String var, Expression expression) {
        if (this.name.equals(var)) {
            return expression;
        }
        return this;
    }

    /**
     * Converts the variable into an equivalent expression using only NAND operations.
     * A variable remains itself when nandified.
     * @return The current Var instance.
     */
    @Override
    public Expression nandify() {
        return new Var(this.name);
    }

    /**
     * Converts the variable into an equivalent expression using only NOR operations.
     * A variable remains itself when norified.
     * @return The current Var instance.
     */
    @Override
    public Expression norify() {
        return new Var(this.name);
    }

    /**
     * Simplifies the variable. A variable is already in its simplest form.
     * @return The current Var instance.
     */
    @Override
    public Expression simplify() {
        return new Var(this.name);
    }

    /**
     * Returns a new Not expression wrapping this variable.
     * @return A new Not expression.
     */
    public Expression assignNot() {
        return new Not(this);
    }

}
