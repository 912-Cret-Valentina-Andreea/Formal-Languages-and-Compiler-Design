package Domain;

import java.util.List;

public class Production {
    List<String> leftPart;
    List<String> rightPart;

    public Production(List<String> leftPart, List<String> rightPart){
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }

    @Override
    public String toString(){
        return this.leftPart + " -> " + this.rightPart;
    }
}
