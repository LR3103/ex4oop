public class Nand extends BinaryExpression {

    public Nand(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return !(left && right);
    }

    @Override
    public String getLogicalSign() {
        return "A";
    }

    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Nand(newLeft, newRight);
    }

    @Override
    public Expression nandify() {
        return reconstruct(getLeft().nandify(), getRight().nandify());
    }

    @Override
    public Expression norify() {
        // 1. Compute and recycle the deepest child sub-trees
        Expression left = new Nor(this.getLeft().norify(), this.getLeft().norify());
        Expression right = new Nor(this.getRight().norify(), this.getRight().norify());

        // 2. Compute the middle NOR layer exactly once to recycle it
        Expression middleNor = new Nor(left, right);

        // 3. Return the root node using the recycled middle layer
        return new Nor(middleNor, middleNor);
    }

    @Override
    public Expression simplify() {
        // Step 1: Simplify children first
        Expression simLeft = this.getLeft().simplify();
        Expression simRight = this.getRight().simplify();

        // Step 2: No variables -> evaluate to its result
        if (simLeft.getVariables().isEmpty() && simRight.getVariables().isEmpty()) {
            try {
                // Evaluates purely constant expressions (e.g., T A F -> T)
                return new Val(new Nand(simLeft, simRight).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through
            }
        }

        // Step 3: x A x = ~(x)
        if (simLeft.toString().equals(simRight.toString())) {
            return new Not(simLeft).simplify(); // Add .simplify() here!
        }

        // Step 4: x A T = ~(x)
        if (isTrue(simLeft)) return new Not(simRight).simplify(); // And here!
        if (isTrue(simRight)) return new Not(simLeft).simplify(); // And here!

        // Step 4.5: x A F = T (and commutative)
        // If ANY side is False, an And would be False, so a Nand is ALWAYS True
        if (isFalse(simLeft)) return new Val(true);
        if (isFalse(simRight)) return new Val(true);

        // Step 5: Fallback if no rules apply
        return new Nand(simLeft, simRight);
    }
}
