public class Nor extends BinaryExpression {

    public Nor(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return !(left || right);
    }

    @Override
    public String getLogicalSign() {
        return "V";
    }

    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Nor(newLeft, newRight);
    }

    @Override
    public Expression nandify() {
        // 1. Compute the deep sub-trees exactly once
        Expression nandLeft = this.getLeft().nandify();
        Expression nandRight = this.getRight().nandify();

        // 2. Build the inner layer (equivalent to NOT A NAND NOT B)
        Expression innerNand = new Nand(
                new Nand(nandLeft, nandLeft),
                new Nand(nandRight, nandRight)
        );

        // 3. Recycle the entire inner structure for the parent node
        return new Nand(innerNand, innerNand);
    }

    @Override
    public Expression norify() {
        // Already a NOR gate, just recursively process the children
        return new Nor(this.getLeft().norify(), this.getRight().norify());
    }

    @Override
    public Expression simplify() {
        // Step 1: Simplify children first
        Expression simLeft = this.getLeft().simplify();
        Expression simRight = this.getRight().simplify();

        // Step 2: No variables -> evaluate to its result
        if (simLeft.getVariables().isEmpty() && simRight.getVariables().isEmpty()) {
            try {
                // Evaluates purely constant expressions (e.g., T V F -> F)
                return new Val(new Nor(simLeft, simRight).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through
            }
        }

        // Step 3: x V x = ~(x)
        // If the two sides are identical, Nor returns the NOT of that side
        if (simLeft.toString().equals(simRight.toString())) {
            return new Not(simLeft);
        }

        // Step 4: x V T = F (and commutative)
        // If ANY side is True, an Or would be True, so a Nor is ALWAYS False
        if (isTrue(simLeft)) return new Val(false);
        if (isTrue(simRight)) return new Val(false);

        // Step 4.5: x V F = ~(x) (and commutative)
        // If one side is False, it has no effect on the Or, so Nor just inverts the other side
        if (isFalse(simLeft)) return new Not(simRight);
        if (isFalse(simRight)) return new Not(simLeft);

        // Step 5: Fallback if no rules apply
        return new Nor(simLeft, simRight);
    }
}
