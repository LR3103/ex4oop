public class Xnor extends BinaryExpression {

    public Xnor(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return left == right;
    }

    @Override
    public String getLogicalSign() {
        return "#";
    }

    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Xnor(newLeft, newRight);
    }

    @Override
    public Expression nandify() {
        // 1. Compute child sub-trees exactly once
        Expression nandLeft = this.getLeft().nandify();
        Expression nandRight = this.getRight().nandify();

        // 2. Build the standard (A Nand B) structure once so it can be reused
        Expression standardNand = new Nand(nandLeft, nandRight);

        // 3. Assemble the final tree, recycling all components
        return new Nand(
                new Nand(
                        new Nand(nandLeft, nandLeft),
                        new Nand(nandRight, nandRight)
                ),
                standardNand
        );
    }

    @Override
    public Expression norify() {
        // 1. Compute norified child sub-trees exactly once
        Expression norLeft = this.getLeft().norify();
        Expression norRight = this.getRight().norify();

        // 2. The shared middle piece: NOR(A, B)
        Expression middleSharedNor = new Nor(norLeft, norRight);

        // 3. Build the final Xnor tree, recycling the shared middle piece
        return new Nor(
                new Nor(norLeft, middleSharedNor),
                new Nor(norRight, middleSharedNor)
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
                // Evaluates purely constant expressions (e.g., T # F -> F)
                return new Val(new Xnor(simLeft, simRight).evaluate());
            } catch (Exception ex) {
                // Do nothing, let it fall through
            }
        }

        // Step 3: x # x = T
        // If the two sides are identical, Xnor always returns True
        if (simLeft.toString().equals(simRight.toString())) {
            return new Val(true);
        }

        // Step 4: SKIPPED!
        // The assignment did NOT provide rules for x # T or x # F,
        // so we are not allowed to simplify them algebraically.

        // Step 5: Fallback if no explicit rules apply
        return new Xnor(simLeft, simRight);
    }

}
