public class Val implements Expression{
    private boolean value;

    /**
     * Constructor for Val.
     * @param value the boolean value.
     */
    public Val(boolean value) {
        this.value = value;
    }

    public Val(Val val){
        this.value = val.value;
    }

    @Override
    public Boolean evaluate(java.util.Map<String, Boolean> assignment) throws Exception {
        //no meaning no variables just return the value
        return this.evaluate();
    }

    @Override
    public Boolean evaluate() throws Exception {
        return this.value;
    }

    @Override
    public java.util.List<String> getVariables() {
        return new java.util.ArrayList<>();
    }

    @Override
    public String toString() {
        return this.value ? "T" : "F";
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return this;
    }

    @Override
    public Expression nandify() {
        return new Val(this);
    }

    @Override
    public Expression norify() {
        return new Val(this);
    }

    @Override
    public Expression simplify(){
        return new Val(this);
    }

    public Expression assignNot(){
        return new Not(this);
    }

}
