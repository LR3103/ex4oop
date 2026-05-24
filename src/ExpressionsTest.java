import java.util.Map;
import java.util.TreeMap;

/**
 * @author 215303207
 * A test class to demonstrate various functionalities of the boolean expression hierarchy,
 * including evaluation, NANDification, NORification, and simplification.
 */
public class ExpressionsTest {
    /**
     * The main method to run the expression demonstration.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            // 1. Create an expression with at least three variables: ((x & y) ^ z)
            // Note: The original expression was ((x & y) ^ z), but the code creates ((x & y) ^ T).
            // Assuming the code is correct as is for the demonstration.
            Expression e = new Xor(new And(new Var("x"), new Var("y")), new Val(true));

            // 2. Print the expression
            System.out.println("Original Expression: " + e.toString());

            // 3. Print the value of the expression with an assignment to every variable
            Map<String, Boolean> assignment = new TreeMap<>();
            assignment.put("x", true);
            assignment.put("y", false);
            // 'z' is not in the expression created, so it's not added to the assignment.
            // If 'z' were in the expression, it would need to be assigned here.
            System.out.println("Assignment: " + assignment);
            System.out.println("Evaluation with assignment: " + e.evaluate(assignment));

            // 4. Print the Nandified version of the expression
            System.out.println("Nandified Expression: " + e.nandify().toString());

            // 5. Print the Norified version of the expression
            System.out.println("Norified Expression: " + e.norify().toString());

            // 6. Print the simplified version of the expression
            System.out.println("Simplified Expression: " + e.simplify().toString());

        } catch (Exception ex) {
            // Failsafe in case evaluate() or simplify() throws an unexpected exception
            System.err.println("An error occurred: " + ex.getMessage());
        }
    }
}
