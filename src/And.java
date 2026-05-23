
public class And extends BinaryExpression {

    public And(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return left && right;
    }

    @Override
    public String getLogicalSign() {
        return "&";
    }

    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new And(newLeft, newRight);
    }

    @Override
    public Expression nandify() {
        // Formula: (A Nand B) Nand (A Nand B)
        // Compute the sub-trees exactly once to recycle the object instances
        Expression nandChild = new Nand(this.getLeft().nandify(), this.getRight().nandify());

        // Pass the same instance to both sides of the parent Nand
        return new Nand(nandChild, nandChild);
    }

    @Override
    public Expression norify() {
        // Formula: NOR(NOR(A, A), NOR(B, B))
        // Compute each side's norified tree once and recycle it
        Expression norLeft = this.getLeft().norify();
        Expression norRight = this.getRight().norify();

        return new Nor(
                new Nor(norLeft, norLeft),
                new Nor(norRight, norRight)
        );
    }

    @Override
    public Expression simplify() {
        // Step 1: Simplify children
        Expression simLeft = this.getLeft().simplify();
        Expression simRight = this.getRight().simplify();

        // Step 2: No variables -> evaluate to its result
        if (simLeft.getVariables().isEmpty() && simRight.getVariables().isEmpty()) {
            try {
                // Java compiler forces us to try-catch this, even though we know
                // it won't fail because there are no variables.
                return new Val(new And(simLeft, simRight).evaluate());
            } catch (Exception ex) {
                // Do nothing, just let it fall through to Step 5
            }
        }

        // Step 3: x & x = x
        if (simLeft.toString().equals(simRight.toString())) {
            return simLeft;
        }

        // Step 4: x & T = x (and commutative)
        if (isTrue(simLeft)) return simRight;
        if (isTrue(simRight)) return simLeft;

        // Step 4.5: x & F = F (and commutative)
        if (isFalse(simLeft)) return new Val(false);
        if (isFalse(simRight)) return new Val(false);

        // Step 5: Fallback
        return new And(simLeft, simRight);
    }
}
