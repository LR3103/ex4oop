import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseExpression implements Expression {
    public abstract String getLogicalSign();
    // Inside BinaryExpression or BaseExpression
    protected boolean isTrue(Expression e) {
        // If it has variables, it definitely isn't a constant True
        if (!e.getVariables().isEmpty()) return false;

        try {
            return e.evaluate(); // Only evaluates if it's purely constants
        } catch (Exception ex) {
            return false; // Should never be reached
        }
    }

    protected boolean isFalse(Expression e) {
        if (!e.getVariables().isEmpty()) return false;

        try {
            return !e.evaluate();
        } catch (Exception ex) {
            return false; // Should never be reached
        }
    }

    public Expression assignNot() {
        // By default, most objects just wrap themselves in a Not gate.
        return new Not(this);
    }

}
