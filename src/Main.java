import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("PHASE 1: BASIC EVALUATION & VARIABLE EXTRACTION");
        System.out.println("==================================================");

        Expression x = new Var("x");
        Expression y = new Var("y");
        Expression z = new Var("z");
        Expression t = new Val(true);
        Expression f = new Val(false);

        // Complex expression: ( (x | y) & (z ^ T) ) A (x | F)
        Expression expression = new Nand(
                new And(new Or(x, y), new Xor(z, t)),
                new Or(x, f)
        );

        System.out.println("Expression: " + expression);
        System.out.println("Variables extracted: " + expression.getVariables());

        Map<String, Boolean> assignment = new TreeMap<>();
        assignment.put("x", true);
        assignment.put("y", false);
        assignment.put("z", true);

        try {
            System.out.println("Evaluation with " + assignment + ": " + expression.evaluate(assignment));
        } catch (Exception e) {
            System.out.println("Evaluation failed: " + e.getMessage());
        }


        System.out.println("\n==================================================");
        System.out.println("PHASE 2: ERROR HANDLING (MISSING VARIABLES)");
        System.out.println("==================================================");

        Expression errorExpression = new Or(new Var("p"), new Var("q"));
        Map<String, Boolean> incompleteAssignment = new TreeMap<>();
        incompleteAssignment.put("p", true); // "q" is intentionally missing!

        System.out.println("Expression: " + errorExpression);
        System.out.println("Incomplete assignment: " + incompleteAssignment);
        try {
            System.out.println("Evaluation: " + errorExpression.evaluate(incompleteAssignment));
            System.out.println("FAIL: Exception was not thrown!");
        } catch (Exception e) {
            System.out.println("SUCCESS - Caught expected exception: " + e.getMessage());
        }


        System.out.println("\n==================================================");
        System.out.println("PHASE 3: NANDIFY & NORIFY EQUIVALENCE (TRUTH TABLE)");
        System.out.println("==================================================");

        Expression xorExpression = new Xor(new Var("a"), new Var("b"));
        Expression nandifiedXor = xorExpression.nandify();
        Expression norifiedXor = xorExpression.norify();

        System.out.println("Original Xor : " + xorExpression);
        System.out.println("Nandified    : " + nandifiedXor);
        System.out.println("Norified     : " + norifiedXor);
        System.out.println("\nTesting all 4 combinations (Original | Nandified | Norified):");

        boolean[][] truthTable = { {true, true}, {true, false}, {false, true}, {false, false} };
        Map<String, Boolean> tableMap = new TreeMap<>();

        try {
            for (boolean[] row : truthTable) {
                tableMap.put("a", row[0]);
                tableMap.put("b", row[1]);

                boolean origResult = xorExpression.evaluate(tableMap);
                boolean nandResult = nandifiedXor.evaluate(tableMap);
                boolean norResult = norifiedXor.evaluate(tableMap);

                System.out.println("a=" + row[0] + ", b=" + row[1] + " --> "
                        + origResult + " | " + nandResult + " | " + norResult);

                if (origResult != nandResult || origResult != norResult) {
                    System.out.println("WARNING: Equivalence failed for " + tableMap);
                }
            }
        } catch (Exception e) {
            System.out.println("Evaluation failed: " + e.getMessage());
        }


        System.out.println("\n==================================================");
        System.out.println("PHASE 4: SIMPLIFICATION STRESS TEST");
        System.out.println("==================================================");

        try {
            // Commutativity
            Expression t1 = new And(new Val(true), new Var("x"));
            System.out.println("T & x          => Expected: x          | Got: " + t1.simplify());

            Expression t3 = new Nand(new Val(false), new Var("x"));
            System.out.println("F A x          => Expected: T          | Got: " + t3.simplify());

            // Chain Reactions
            Expression c1 = new Or(new And(new Var("x"), new Var("x")), new Val(false));
            System.out.println("((x & x) | F)  => Expected: x          | Got: " + c1.simplify());

            Expression c2 = new Nand(new Xor(new Var("x"), new Val(true)), new Xor(new Var("x"), new Val(true)));
            System.out.println("(x^T) A (x^T)  => Expected: x          | Got: " + c2.simplify());

            // Constant Evaluation Sneak Attacks
            Expression subConst = new Xor(new Or(new Val(true), new Val(false)), new Nand(new Val(true), new Val(true)));
            Expression sneak1 = new And(new Var("x"), subConst);
            System.out.println("x & (DeepConst)=> Expected: x          | Got: " + sneak1.simplify());

            // Complex Structural Equivalence
            Expression leftTree = new And(new Or(new Var("x"), new Var("y")), new Var("z"));
            Expression rightTree = new And(new Or(new Var("x"), new Var("y")), new Var("z"));
            Expression eq1 = new Xor(leftTree, rightTree);
            System.out.println("(Tree) ^ (Tree)=> Expected: F          | Got: " + eq1.simplify());

            // Deep Negations
            Expression n2 = new Not(new Not(new Not(new Not(new Var("x")))));
            System.out.println("~(~(~(~(x))))  => Expected: x          | Got: " + n2.simplify());

            // Strict rules test (Xnor)
            Expression strict1 = new Xnor(new Var("x"), new Val(true));
            System.out.println("x # T          => Expected: (x # T)    | Got: " + strict1.simplify());

            // (((x))) = x
            Expression e17 = new Not(new Not(new Not(new Var("x"))));
            System.out.println("Original: " + e17 + ", Simplified: " + e17.simplify()); // Expected: (~(x))

        } catch (Exception e) {
            System.out.println("Simplification failed (Check your try-catches!): " + e.getMessage());
        }
    }
}