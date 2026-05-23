
public class Not extends UnaryExpression {

    public Not(Expression expression) {
        super(expression);
    }

    @Override
    protected boolean logicalFunction(boolean val) {
        return !val;
    }

    @Override
    public String getLogicalSign() {
        return "~";
    }

    @Override
    protected Expression reconstruct(Expression expression) {
        return new Not(expression);
    }

    @Override
    public Expression assignNot() {
        // Polymorphism at its finest! Two NOTs cancel out, so we just return the inner child.
        return this.getExpression();
    }

    @Override
    public Expression nandify() {
        // Transform the inner expression exactly once
        Expression nandChild = this.getExpression().nandify();

        // Recycle the instance to create the NOT logic: (A NAND A)
        return new Nand(nandChild, nandChild);
    }

    @Override
    public Expression norify() {
        // Transform the inner expression exactly once
        Expression norChild = this.getExpression().norify();

        // Recycle the instance to create the NOT logic: (A NOR A)
        return new Nor(norChild, norChild);
    }

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
