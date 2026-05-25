import java.util.HashMap;
import java.util.Map;

/**
 * A test class to demonstrate the functionality of the boolean expressions.
 *
 * @author Your Name
 */
public class ExpressionsTest {

    /**
     * The main method that runs the tests.
     * It creates an expression, prints it, evaluates it, nandifies it,
     * norifies it, and simplifies it.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // 1. Create an expression with at least three variables: (x & y) | z
        Expression expression = new Or(new And(new Var("x"), new Var("y")), new Var("z"));

        // 2. Print the expression.
        System.out.println(expression.toString());

        // 3. Print the value of the expression with an assignment to every variable.
        Map<String, Boolean> assignment = new HashMap<>();
        assignment.put("x", true);
        assignment.put("y", false);
        assignment.put("z", true);
        try {
            System.out.println(expression.evaluate(assignment));
        } catch (Exception e) {
            System.out.println("Error evaluating expression: " + e.getMessage());
        }

        // 4. Print the Nandified version of the expression.
        System.out.println(expression.nandify().toString());

        // 5. Print the Norified version of the expression.
        System.out.println(expression.norify().toString());

        // 6. Print the simplified version of the expression.
        System.out.println(expression.simplify().toString());
    }
}
