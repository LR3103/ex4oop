public abstract class UnaryExpression extends BaseExpression {
    private Expression expression;

    /**
     * Constructor for UnaryExpression.
     * @param expression the inner expression.
     */
    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    protected abstract boolean logicalFunction(boolean val);

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Boolean evaluate() throws Exception {
        return logicalFunction(this.expression.evaluate());
    }

    @Override
    public Boolean evaluate(java.util.Map<String, Boolean> assignment) throws Exception {
        return logicalFunction(this.expression.evaluate(assignment));
    }

    @Override
    public java.util.List<String> getVariables() {
        return this.expression.getVariables();
    }

    @Override
    public String toString() {
        return "(" + this.getLogicalSign() + "(" + this.expression.toString() + ")" + ")";
    }

    protected abstract Expression reconstruct(Expression expression);

    @Override
    public Expression assign(String var, Expression expression) {
        return reconstruct(this.expression.assign(var, expression));
    }

}
