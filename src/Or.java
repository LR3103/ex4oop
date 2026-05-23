
public class Or extends BinaryExpression {

    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return left || right;
    }

    @Override
    public String getLogicalSign() {
        return "|";
    }

    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Or(newLeft, newRight);
    }

    @Override
    public Expression nandify() {
        // 1. Evaluate the sub-trees exactly once
        Expression nandLeft = this.getLeft().nandify();
        Expression nandRight = this.getRight().nandify();

        // 2. Recycle those instances into the final NAND structure
        return new Nand(
                new Nand(nandLeft, nandLeft),
                new Nand(nandRight, nandRight)
        );
    }

    @Override
    public Expression norify() {
        // Formula: NOT(NOR(A, B)) -> which matches your implementation
        return new Nor(
                new Nor(this.getLeft().norify(), this.getRight().norify()),
                new Nor(this.getLeft().norify(), this.getRight().norify())
        );
    }

    @Override
    public Expression simplify() {
        // Step 1: Simplify children first
        Expression simLeft = this.getLeft().simplify();
        Expression simRight = this.getRight().simplify();

        // Step 2: No variables -> evaluate to its result
        if (simLeft.getVariables().isEmpty() && simRight.getVariables().isEmpty()) {
            try {
                // Evaluates purely constant expressions (e.g., T | F -> T)
                return new Val(new Or(simLeft, simRight).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through to Step 5
            }
        }

        // Step 3: x | x = x
        if (simLeft.toString().equals(simRight.toString())) {
            return simLeft;
        }

        // Step 4: x | T = T (and commutative)
        if (isTrue(simLeft)) return new Val(true);
        if (isTrue(simRight)) return new Val(true);

        // Step 4.5: x | F = x (and commutative)
        if (isFalse(simLeft)) return simRight;
        if (isFalse(simRight)) return simLeft;

        // Step 5: Fallback if no rules apply
        return new Or(simLeft, simRight);
    }
}
