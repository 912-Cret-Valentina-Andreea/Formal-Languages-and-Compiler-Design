package Domain;

import java.util.*;

public class ParseTable {

    HashMap<Pair<String, String>, Pair<List<String>, Integer>> parsingTable = new HashMap<>();
    Parser parser;
    Grammar grammar;

    public ParseTable(Parser parser){
        this.parser = parser;
        this.grammar = parser.grammar;
        generateParseTable();
    }
    public void generateParseTable(){
        for(String terminal : grammar.getTerminals()){
            parsingTable.put(new Pair<>(terminal, terminal), new Pair<>(List.of("POP"), -1));
        }

        parsingTable.put(new Pair<>("$", "$"), new Pair<>(List.of("ACC."), -1));

        for(String nonterminal: grammar.getNonterminals()){
            for(Production production: grammar.getProductionsForNonterminalLHS(nonterminal)){
                var concatenationFirstRHS = concatenateFirstOfProduction(production.rightPart);
                var hasEpsilon = false;
                for(String symbolFirst: concatenationFirstRHS){
                    if(!symbolFirst.equals("EPS")){
                        parsingTable.put(new Pair<>(nonterminal, symbolFirst), new Pair<>(production.rightPart, grammar.getProductionIndex(production)));
                    }
                    else{
                        hasEpsilon = true;
                    }
                }
                if(hasEpsilon){
                    for(String symbolFollow: parser.followSet.get(nonterminal)){
                        if(symbolFollow == "EPS"){
                            parsingTable.put(new Pair<>(nonterminal, "$"), new Pair<>(production.rightPart, grammar.getProductionIndex(production)));
                        }
                        else{
                            parsingTable.put(new Pair<>(nonterminal, symbolFollow), new Pair<>(production.rightPart, grammar.getProductionIndex(production)));
                        }
                    }
                }
            }
        }
    }
    private Set<String> concatenateFirstOfProduction(List<String> prodRHS){
        var concatenation = parser.firstSet.get(prodRHS.get(0));
        for(int i = 1; i<prodRHS.size(); i++){
            //concatenation = concatenationSizeOne(concatenation, parser.firstSet.get(prodRHS.get(i)));
        }
        return concatenation;
    }

}
