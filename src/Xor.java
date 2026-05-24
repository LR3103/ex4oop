/**
 * Represents a logical XOR (exclusive OR) expression.
 * Extends BinaryExpression to handle two sub-expressions.
 */
public class Xor extends BinaryExpression {

    /**
     * Constructs an XOR expression with two given sub-expressions.
     * @param left The left-hand side expression.
     * @param right The right-hand side expression.
     */
    public Xor(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * Implements the logical XOR function.
     * @param left The boolean value of the left expression.
     * @param right The boolean value of the right expression.
     * @return The result of the logical XOR operation.
     */
    @Override
    protected boolean logicalFunction(boolean left, boolean right) {
        return left ^ right;
    }

    /**
     * Returns the string representation of the logical sign for XOR.
     * @return The string "^".
     */
    @Override
    public String getLogicalSign() {
        return "^";
    }

    /**
     * Reconstructs a new XOR expression with new left and right sub-expressions.
     * Used primarily in assignment operations to maintain the expression tree structure.
     * @param newLeft The new left-hand side expression.
     * @param newRight The new right-hand side expression.
     * @return A new Xor expression.
     */
    @Override
    protected Expression reconstruct(Expression newLeft, Expression newRight) {
        return new Xor(newLeft, newRight);
    }

    /**
     * Converts the current XOR expression into an equivalent expression using only NAND operations.
     * The formula for A XOR B using NAND is: (A NAND (A NAND B)) NAND (B NAND (A NAND B)).
     * @return An expression tree representing the original XOR expression using only NAND gates.
     */
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

    /**
     * Converts the current XOR expression into an equivalent expression using only NOR operations.
     * The formula for A XOR B using NOR is: NOR(NOR(A, B), NOR(NOR(A, A), NOR(B, B))).
     * @return An expression tree representing the original XOR expression using only NOR gates.
     */
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

    /**
     * Simplifies the current XOR expression based on logical identities.
     * Simplification rules include:
     * - If both sub-expressions are constants, evaluate to a constant.
     * - x ^ x = F
     * - x ^ T = ~(x)
     * - x ^ F = x
     * @return A simplified version of the XOR expression.
     */
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
