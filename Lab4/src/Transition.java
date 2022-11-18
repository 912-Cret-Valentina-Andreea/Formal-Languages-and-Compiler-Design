public class Transition {
    String initialS;
    String finalS;
    String val;

    public Transition(String initialS, String finalS, String val) {
        this.initialS = initialS;
        this.finalS = finalS;
        this.val = val;
    }

    public String getInitialS() {
        return initialS;
    }

    public void setInitialS(String initialS) {
        this.initialS = initialS;
    }

    public String getFinalS() {
        return finalS;
    }

    public void setFinalS(String finalS) {
        this.finalS = finalS;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String toString(){
        String s="";
        s+=initialS + ", " + val + " -> " + finalS;
        return s;
    }
}
