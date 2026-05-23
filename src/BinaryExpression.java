import java.util.List;
import java.util.Map;

public abstract class BinaryExpression extends BaseExpression{
    private Expression left;
    private Expression right;

    BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    protected abstract boolean logicalFunction(boolean left, boolean right);
    protected abstract Expression reconstruct(Expression newLeft, Expression newRight);

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public Boolean evaluate() throws Exception {
        return logicalFunction(this.left.evaluate(), this.right.evaluate());
    }

    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return logicalFunction(this.left.evaluate(assignment), this.right.evaluate(assignment));
    }

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

    @Override
    public String toString() {
        return "(" + this.left.toString() + " " + this.getLogicalSign() + " " + this.right.toString() + ")";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return reconstruct(this.left.assign(var, expression), this.right.assign(var, expression));
    }
}
