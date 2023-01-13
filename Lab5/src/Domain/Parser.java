package Domain;

import java.util.*;

public class Parser {

    public Grammar grammar;
    public Map<String, Set<String>> firstSet;
    public Map<String, Set<String>> followSet;

    public Parser(){
        this.grammar = new Grammar();
        this.firstSet = new HashMap<>();
        this.followSet = new HashMap<>();
        this.grammar.readGrammar("src/Resources/g4.txt");
        generateSets();
    }

    public void generateSets(){
        generateFirst();
        generateFollow();
        System.out.println(printFirst());
        System.out.println(printFollow());
    }

    public void generateFirst(){
        //parse every nonterminal
        for(String nonterminal : grammar.getNonterminals()){
            firstSet.put(nonterminal, new HashSet<>());
            List<Production> productionsForNonterminal = grammar.getProductionsForNonterminalLHS(nonterminal);
            //parse every production for that nonterminal
            for(Production production: productionsForNonterminal){
                //initialize first with the first terminal in the production or epsilon(if the right part of a production is epsilon)
                if(grammar.getTerminals().contains(production.rightPart.get(0)) || production.rightPart.get(0).equals("EPS"))
                    firstSet.get(nonterminal).add(production.rightPart.get(0));
            }
        }

        boolean isChanged = true;
        //while the columns are still diferent (when the column will not be modified anymore we can stop)
        while(isChanged){
            isChanged = false;
            HashMap<String, Set<String>> newColumn = new HashMap<>();
            //parse every nonterminal
            for(String nonterminal : grammar.getNonterminals()) {
                Set<String> toAdd = new HashSet<>(firstSet.get(nonterminal));
                List<Production> productionsForNonterminal = grammar.getProductionsForNonterminalLHS(nonterminal);
                //parse every production for that nonterminal
                for (Production production : productionsForNonterminal) {
                    List<String> nonTerminalsFromRHS = new ArrayList<>();
                    String terminalFromRHS = "";
                    //parse every symbol from the right-hand side and put in 2 variables all the nonterminals until a terminal, and the first terminal
                    for (String symbol : production.rightPart) {
                        if (this.grammar.getNonterminals().contains(symbol)) {
                            //put every nonterminal
                            nonTerminalsFromRHS.add(symbol);
                        } else {
                            //put first terminal
                            terminalFromRHS = symbol;
                            break;
                        }
                    }
                    //concatenate the nonterminals and the terminal from above and add it to the cell corresponding to the nonterminal we are at
                    toAdd.addAll(concatenation(nonTerminalsFromRHS, terminalFromRHS));
                }
                //if what we need to add is different that what we had before we need to continue the iterations
                if (!toAdd.equals(firstSet.get(nonterminal))) {
                    isChanged = true;
                }
                //put for every nonterminal the new cell
                newColumn.put(nonterminal, toAdd);
            }
            firstSet=newColumn;
        }

        for(String terminal: grammar.getTerminals()) {
            firstSet.put(terminal, Set.of(terminal));
        }
        firstSet.put("EPS", Set.of("EPS"));
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

    public void generateFollow(){
        HashMap<String, Set<String>> lastColumn = new HashMap<>();
        HashMap<String, Set<String>> currentColumn = new HashMap<>();
        //initialize every cell of the current column with the empty list
        for(String nonterminal: grammar.getNonterminals()){
            currentColumn.put(nonterminal, Set.of());
        }
        //initialize the starting nonterminal with epsilon
        currentColumn.put(grammar.getStart(), Set.of("EPS"));

        boolean isChanged = true;
        //while the currentColumn is different than the lastColumn
        while(isChanged){
            isChanged = false;
            //last column becomes the current one
            lastColumn = new HashMap<>(currentColumn);
            //current column gets reinitialized
            currentColumn = new HashMap<>();

            //parse through every nonterminal
            for(String nonterminal: grammar.getNonterminals()){
                var followCell = new HashSet<String>();
                //get all the productions where the nonterminal is present in the right hand side
                var productions = grammar.getProductionsForNonterminalRHS(nonterminal);

                //to the current cell we append what we had before in the cell from the last column
                followCell.addAll(lastColumn.get(nonterminal));

                //we parse every production with the nonterminal in the right hand side
                for(Production production: productions){
                    var nonterminalPosInProduction = production.rightPart.indexOf(nonterminal);
                    //if the nonterminal is not the last one in the production (ex for A: S -> AB)
                    if(nonterminalPosInProduction < production.rightPart.size()-1){
                        //we will parse the first table for the element after the current nonterminal
                        for(String symbol: firstSet.get(production.rightPart.get(nonterminalPosInProduction+1))){
                            //if between the symbols in the first table is epsilon we add to the current column the last
                            //column from the follow table from the nonterminal in the left hand side of the production
                            if(symbol.equals("EPS")){
                                followCell.addAll(lastColumn.get(production.leftPart.get(0)));
                            }
                            //if not, we just add the cell of the first table for the nonterminal after the current nonterminal
                            else{
                                followCell.addAll(firstSet.get(production.rightPart.get(nonterminalPosInProduction+1)));
                            }
                        }
                    }
                    //if the terminal is the last one in the production we add the cell from the last column for the nonterminal in the left part
                    else{
                        followCell.addAll(lastColumn.get(production.leftPart.get(0)));
                    }
                }
                //we put the new calculated cell into the current column
                currentColumn.put(nonterminal, followCell);
                //if the current column is the same as the last column the follow table is done and the set is complete
                if(!lastColumn.get(nonterminal).equals(currentColumn.get(nonterminal))){
                    isChanged = true;
                }
            }
        }
        followSet = currentColumn;
    }

    public String printFollow() {
        StringBuilder builder = new StringBuilder();
        followSet.forEach((k, v) -> {
            builder.append(k).append(": ").append(v).append("\n");
        });
        return builder.toString();
    }

}

