public class Var implements Expression {
    private String name;

    public Var(String varName) {
        this.name = varName;
    }

    @Override
    public Boolean evaluate(java.util.Map<String, Boolean> assignment) throws Exception {
        if (assignment.containsKey(this.name)) {
            return assignment.get(this.name);
        }
        throw new Exception("Variable " + this.name + " not found in assignment");
    }

    @Override
    public Boolean evaluate() throws Exception {
        throw new Exception("Evaluate was called with no assignment's variable for" + this.name +".");
    }

    @Override
    public java.util.List<String> getVariables() {
        java.util.List<String> list = new java.util.ArrayList<>();
        list.add(this.name);
        return list;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public Expression assign(String var, Expression expression) {
        if (this.name.equals(var)) {
            return expression;
        }
        return this;
    }

    @Override
    public Expression nandify() {
        return new Var(this.name);
    }

    @Override
    public Expression norify() {
        return new Var(this.name);
    }

    @Override
    public Expression simplify() {
        return new Var(this.name);
    }

    public Expression assignNot(){
        return new Not(this);
    }

}
