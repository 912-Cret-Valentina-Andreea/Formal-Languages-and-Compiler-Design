package Domain;

import java.util.*;

public class ParseTable {

    HashMap<Pair<String, String>, Pair<List<String>, Integer>> parsingTable = new HashMap<>();
    Parser parser;
    Grammar grammar;

    public ParseTable(Parser parser){
        this.parser = parser;
        this.grammar = parser.grammar;
    }


}
