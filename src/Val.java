/**
 * Represents a constant boolean value (True or False).
 * Implements the Expression interface.
 */
public class Val implements Expression {
    private boolean value;

    /**
     * Constructs a Val expression with the specified boolean value.
     * @param value The boolean value (true for T, false for F).
     */
    public Val(boolean value) {
        this.value = value;
    }

    /**
     * Copy constructor for Val.
     * @param val The Val object to copy.
     */
    public Val(Val val) {
        this.value = val.value;
    }

    /**
     * Evaluates the constant value. Since it's a constant, the assignment map is ignored.
     * @param assignment A map containing variable names and their boolean values (ignored).
     * @return The boolean value of this constant.
     * @throws Exception This method should not throw an exception as it has no variables.
     */
    @Override
    public Boolean evaluate(java.util.Map<String, Boolean> assignment) throws Exception {
        //no meaning no variables just return the value
        return this.evaluate();
    }

    /**
     * Evaluates the constant value.
     * @return The boolean value of this constant.
     * @throws Exception This method should not throw an exception as it has no variables.
     */
    @Override
    public Boolean evaluate() throws Exception {
        return this.value;
    }

    /**
     * Returns an empty list of variables, as a constant value has no variables.
     * @return An empty list.
     */
    @Override
    public java.util.List<String> getVariables() {
        return new java.util.ArrayList<>();
    }

    /**
     * Returns the string representation of the constant value ("T" for true, "F" for false).
     * @return "T" or "F".
     */
    @Override
    public String toString() {
        return this.value ? "T" : "F";
    }

    /**
     * Assigning a variable to a constant value does not change the constant itself.
     * @param var The name of the variable to replace (ignored).
     * @param expression The expression to substitute (ignored).
     * @return The current Val instance, as it is immutable by assignment.
     */
    @Override
    public Expression assign(String var, Expression expression) {
        return this;
    }

    /**
     * Converts the constant value into an equivalent expression using only NAND operations.
     * A constant value remains itself when nandified.
     * @return The current Val instance.
     */
    @Override
    public Expression nandify() {
        return new Val(this);
    }

    /**
     * Converts the constant value into an equivalent expression using only NOR operations.
     * A constant value remains itself when norified.
     * @return The current Val instance.
     */
    @Override
    public Expression norify() {
        return new Val(this);
    }

    /**
     * Simplifies the constant value. A constant value is already in its simplest form.
     * @return The current Val instance.
     */
    @Override
    public Expression simplify() {
        return new Val(this);
    }

    /**
     * Returns a new Not expression wrapping this constant value.
     * @return A new Not expression.
     */
    public Expression assignNot() {
        return new Not(this);
    }

}
