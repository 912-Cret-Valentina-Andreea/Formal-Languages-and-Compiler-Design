package Domain;

import java.util.*;

public class Parser {

    private Grammar grammar;
    private Map<String, Set<String>> firstSet;
    private Map<String, Set<String>> followSet;

    public Parser(){
        this.grammar = new Grammar();
        this.firstSet = new HashMap<>();
        this.followSet = new HashMap<>();
        this.grammar.readGrammar("src/Resources/g3.txt");
        generateSets();
    }

    private void generateSets(){
        generateFirst();
     //   generateFollow();
        System.out.println(printFirst());
    }

    private void generateFirst(){
        for(String nonterminal : grammar.getNonterminals()){
            firstSet.put(nonterminal, new HashSet<>());
            List<Production> productionsForNonterminal = grammar.getProductionsForNonterminal(nonterminal);
            for(Production production: productionsForNonterminal){
                if(grammar.getTerminals().contains(production.rightPart.get(0)) || production.rightPart.get(0).equals("EPS"))
                    firstSet.get(nonterminal).add(production.rightPart.get(0));
            }
        }

        boolean isChanged = true;
        while(isChanged){
            isChanged = false;
            HashMap<String, Set<String>> newColumn = new HashMap<>();
            for(String nonterminal : grammar.getNonterminals()){
                Set<String> toAdd = new HashSet<>(firstSet.get(nonterminal));
                List<Production> productionsForNonterminal = grammar.getProductionsForNonterminal(nonterminal);
                for(Production production: productionsForNonterminal){
                    List<String> nonTerminalsFromRHS = new ArrayList<>();
                    String terminalFromRHS = "";
                    for(String symbol : production.rightPart){
                        if(this.grammar.getNonterminals().contains(symbol)){
                            nonTerminalsFromRHS.add(symbol);
                        }
                        else{
                            terminalFromRHS = symbol;
                            break;
                        }
                    }
                    toAdd.addAll(concatenation(nonTerminalsFromRHS,terminalFromRHS));
                }
                if (!toAdd.equals(firstSet.get(nonterminal))) {
                    isChanged = true;
                }
                newColumn.put(nonterminal,toAdd);
            }
            firstSet=newColumn;
        }
    }

    private Set<String> concatenation(List<String> nonterminals, String terminal){
        if (nonterminals.size() == 0)
            return new HashSet<>();
        if (nonterminals.size() == 1) {
            return firstSet.get(nonterminals.iterator().next());
        }

        Set<String> concatenation = new HashSet<>();
        var step = 0;
        var allEpsilon = true;

        for (String nonTerminal : nonterminals) {
            if (!firstSet.get(nonTerminal).contains("EPS")) {
                allEpsilon = false;
            }
        }
        if (allEpsilon) {
            concatenation.add(Objects.requireNonNullElse(terminal, "EPS"));
        }

        while (step < nonterminals.size()) {
            boolean thereIsOneEpsilon = false;
            for (String s : firstSet.get(nonterminals.get(step)))
                if (s.equals("EPS"))
                    thereIsOneEpsilon = true;
                else
                    concatenation.add(s);

            if (thereIsOneEpsilon)
                step++;
            else
                break;
        }
        return concatenation;
    }

    public String printFirst() {
        StringBuilder builder = new StringBuilder();
        firstSet.forEach((k, v) -> {
            builder.append(k).append(": ").append(v).append("\n");
        });
        return builder.toString();
    }
    public void generateFollow() {
        //initialization
        for (String nonterminal : grammar.getNonterminals()) {
            followSet.put(nonterminal, new HashSet<>());
        }
        followSet.get(grammar.getStart()).add("epsilon");

        //rest of iterations
        var isChanged = true;
        while (isChanged) {
            isChanged = false;
            HashMap<String, Set<String>> newColumn = new HashMap<>();

            for (String nonterminal : grammar.getNonterminals()) {
                newColumn.put(nonterminal, new HashSet<>());
                var productionsWithNonterminalInRhs = new HashMap<String, Set<List<String>>>();
                var allProductions = grammar.getProductionsForNonterminal(nonterminal);
                allProductions.forEach((k, v) -> {
                    for (var eachProduction : v) {
                        if (eachProduction.contains(nonterminal)) {
                            var key = k.iterator().next();
                            if (!productionsWithNonterminalInRhs.containsKey(key))
                                productionsWithNonterminalInRhs.put(key, new HashSet<>());
                            productionsWithNonterminalInRhs.get(key).add(eachProduction);
                        }
                    }
                });
                var toAdd = new HashSet<>(followSet.get(nonterminal));
                productionsWithNonterminalInRhs.forEach((k, v) -> {
                    for (var production : v) {
                        var productionList = (ArrayList<String>) production;
                        for (var indexOfNonterminal = 0; indexOfNonterminal < productionList.size(); ++indexOfNonterminal)
                            if (productionList.get(indexOfNonterminal).equals(nonterminal)) {
                                if (indexOfNonterminal + 1 == productionList.size()) {
                                    toAdd.addAll(followSet.get(k));
                                } else {
                                    var followSymbol = productionList.get(indexOfNonterminal + 1);
                                    if (grammar.getTerminals().contains(followSymbol))
                                        toAdd.add(followSymbol);
                                    else {
                                        for (var symbol : firstSet.get(followSymbol)) {
                                            if (symbol.equals("epsilon"))
                                                toAdd.addAll(followSet.get(k));
                                            else
                                                toAdd.addAll(firstSet.get(followSymbol));
                                        }
                                    }
                                }
                            }
                    }
                });
                if (!toAdd.equals(followSet.get(nonterminal))) {
                    isChanged = true;
                }
                newColumn.put(nonterminal, toAdd);
            }

            followSet = newColumn;

        }
    }
}

