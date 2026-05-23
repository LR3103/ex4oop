
public class Xor extends BinaryExpression {

    public Xor(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return left ^ right;
    }

    @Override
    public String getLogicalSign() {
        return "^";
    }

    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Xor(newLeft, newRight);
    }

    @Override
    public Expression nandify() {
        // 1. Compute child sub-trees exactly once
        Expression nandLeft = this.getLeft().nandify();
        Expression nandRight = this.getRight().nandify();

        // 2. Cache the shared middle piece: Nand(A, B)
        Expression sharedMiddleNand = new Nand(nandLeft, nandRight);

        // 3. Assemble the final XOR tree using the recycled components
        return new Nand(
                new Nand(nandLeft, sharedMiddleNand),
                new Nand(nandRight, sharedMiddleNand)
        );
    }

    @Override
    public Expression norify() {
        // To match the assignment's exact string output, we put the
        // massive complex chunk on the LEFT, and the simple chunk on the RIGHT.
        return new Nor(
                // 1. LEFT SIDE: The massive chunk ((A V A) V (B V B))
                new Nor(
                        new Nor(this.getLeft().norify(), this.getLeft().norify()),
                        new Nor(this.getRight().norify(), this.getRight().norify())
                ),

                // 2. RIGHT SIDE: The simple chunk (A V B)
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
                // Evaluates purely constant expressions (e.g., T ^ T -> F)
                return new Val(new Xor(simLeft, simRight).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through to Step 5
            }
        }

        // Step 3: x ^ x = F
        // If the two sides are identical, Xor always returns False
        if (simLeft.toString().equals(simRight.toString())) {
            return new Val(false);
        }

        // Step 4: x ^ T = ~(x) (and commutative)
        // If one side is True, return the NOT of the other side
        if (isTrue(simLeft)) return new Not(simRight);
        if (isTrue(simRight)) return new Not(simLeft);

        // Step 4.5: x ^ F = x (and commutative)
        // If one side is False, just return the other side exactly as is
        if (isFalse(simLeft)) return simRight;
        if (isFalse(simRight)) return simLeft;

        // Step 5: Fallback if no rules apply
        return new Xor(simLeft, simRight);
    }
}
