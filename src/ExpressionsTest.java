import java.util.Map;
import java.util.TreeMap;

public class ExpressionsTest {
    public static void main(String[] args) {
        try {
            // 1. Create an expression with at least three variables: ((x & y) ^ z)
            Expression e = new Xor(new And(new Var("x"), new Var("y")), new Val(true));

            // 2. Print the expression
            System.out.println(e.toString());

            // 3. Print the value of the expression with an assignment to every variable
            Map<String, Boolean> assignment = new TreeMap<>();
            assignment.put("x", true);
            assignment.put("y", false);
            assignment.put("z", true);
            System.out.println(e.evaluate(assignment));

            // 4. Print the Nandified version of the expression
            System.out.println(e.nandify().toString());

            // 5. Print the Norified version of the expression
            System.out.println(e.norify().toString());

            // 6. Print the simplified version of the expression
            System.out.println(e.simplify().toString());

        } catch (Exception ex) {
            // Failsafe in case evaluate() or simplify() throws an unexpected exception
            System.err.println("An error occurred: " + ex.getMessage());
        }
    }
}
