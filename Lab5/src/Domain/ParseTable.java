package Domain;

import java.util.*;

public class ParseTable {

    private static final int SYMBOL = 0;
    private static final int FATHER = 1;
    private static final int SIBLING = 2;
    private int startIndex = 0;

    List<List<String>> table = new ArrayList<>();

    HashMap<Pair<String, String>, Pair<List<String>, Integer>> parsingTable = new HashMap<>();
    Parser parser;
    Grammar grammar;

    public ParseTable(Parser parser){
        this.parser = parser;
        this.grammar = parser.grammar;
        generateParseTable();
        isSequenceAccepted(List.of("i","b","=","2"));
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
        printParsingTable();
    }

    public void printParsingTable(){

        var maxLen = 0;
        for(Pair<String, String> key: parsingTable.keySet()){
            if(parsingTable.get(key).toString().length() > maxLen)
                maxLen = parsingTable.get(key).toString().length();
        }

        if(maxLen % 2 == 1)
            maxLen++;

        StringBuilder res = new StringBuilder();

        var l = new ArrayList<>(grammar.getNonterminals());
        l.addAll(grammar.getTerminals());
        l.add("$");
        var l2 = new ArrayList<>(grammar.getTerminals());
        l2.add("$");

        res.append(" ".repeat(5));
        for(String symbol: l2){
            var space = maxLen / 2;
            res.append(" ".repeat(Math.max(0, space)));
            res.append(symbol);
            res.append(" ".repeat(Math.max(0, space)));
        }
        res.append("\n").append("-".repeat(maxLen * l2.size())).append("\n");

        int i=0;
        for(String symbol: l){
            res.append(" ").append(l.get(i)).append(" |");
            i++;
            for(String terminal: l2){
                var key = new Pair<>(symbol, terminal);
                var isSet = false;
                for(Pair<String, String> tableKey: parsingTable.keySet()){
                    if(tableKey.getKey().equals(key.getKey()) && tableKey.getValue().equals(key.getValue())){
                        isSet = true;
                        key = tableKey;
                        break;
                    }
                }
                if(isSet){
                    var space = (maxLen - parsingTable.get(key).toString().length()) / 2;
                    res.append(" ".repeat(Math.max(0, space)));
                    res.append(parsingTable.get(key));
                    res.append(" ".repeat(Math.max(0, space)));
                }
                else {
                    res.append("-".repeat(maxLen));
                }
                res.append("|");
            }
            res.append("\n");
        }
        System.out.println(res);
    }

    private Set<String> concatenateFirstOfProduction(List<String> prodRHS){
        var concatenation = parser.firstSet.get(prodRHS.get(0));
        for(int i = 1; i<prodRHS.size(); i++){
            concatenation = concatenationSizeOne(concatenation, parser.firstSet.get(prodRHS.get(i)));
        }
        return concatenation;
    }

    private Set<String> concatenationSizeOne(Set<String> list1, Set<String> list2){
        if(list1 == null && list2 == null){
            return Set.of();
        }
        if(list1 == null){
            list2 = Set.of();
        }
        if(list2 == null){
            list1 = Set.of();
        }

        Set<String> concatenation = new HashSet<>();
        boolean l1ContainsEPS = false;

        for(String terminal: list1){
            if(!terminal.equals("EPS")){
                var term = terminal.charAt(0);
                concatenation.add(String.valueOf(term));
            }
            else{
                l1ContainsEPS = true;
            }
        }

        if(l1ContainsEPS){
            for(String terminal: list2){
                if(!terminal.equals("EPS")){
                    var term = terminal.charAt(0);
                    concatenation.add(String.valueOf(term));
                }
            }
        }
        return concatenation;
    }

    public boolean isSequenceAccepted(List<String> sequence){
        Stack<String> inputStack = new Stack<>();
        Stack<String> workStack = new Stack<>();
        List<Integer> output = new ArrayList<>();

        inputStack.push("$");
        workStack.push("$");
        workStack.push(grammar.getStart());

        for(int i = sequence.size()-1; i>=0; i--){
            inputStack.push(sequence.get(i));
        }

        while(true){
            var parsingTableCell = parsingTable.get(new Pair<>(workStack.peek(), inputStack.peek()));
            if(parsingTableCell != null){
                if(parsingTableCell.getKey().get(0).equals("POP")){
                    inputStack.pop();
                    workStack.pop();
                }
                else if(parsingTableCell.getKey().get(0).equals("ACC.")){
                    System.out.println("Sequence accepted");
                    System.out.println("output: " + output);
                    fatherSiblingTable(output);
                    return false;
                }
                else{
                    workStack.pop();
                    for(int i = parsingTableCell.getKey().size()-1; i >= 0; i--){
                        if(!parsingTableCell.getKey().get(0).equals("EPS")){
                            workStack.push(parsingTableCell.getKey().get(i));
                        }
                    }
                    output.add(parsingTableCell.getValue());
                }
            }
            else{
                System.out.println("Error at:");
                System.out.println(workStack.peek() + " " + inputStack.peek());
                return false;
            }
        }
    }
    private void addToTable(Stack<Integer> stack, List<String> prod){
        var ind = startIndex + prod.size();
        var fatherIndex = stack.pop();
        for(int i=prod.size() - 1; i>=0; i--){
            if(grammar.getNonterminals().contains(prod.get(i)))
                stack.push(ind);
            ind--;
        }
        for(String symbol: prod){
            startIndex++;
            table.get(SYMBOL).add(symbol);
            table.get(FATHER).add(String.valueOf(fatherIndex));
            table.get(SIBLING).add(String.valueOf(startIndex + 1));
        }
        table.get(SIBLING).remove(startIndex);
        table.get(SIBLING).add("-1");
    }

    public void fatherSiblingTable(List<Integer> output){

        Stack<Integer> stackTable = new Stack<>();
        table.add(new ArrayList<>());
        table.add(new ArrayList<>());
        table.add(new ArrayList<>());

        stackTable.add(0);
        table.get(SYMBOL).add(grammar.getStart());
        table.get(FATHER).add("-1");
        table.get(SIBLING).add("-1");

        for(int i = 0; i<output.size(); i++){
            var production = grammar.getProduction(output.get(i));
            addToTable(stackTable, production.rightPart);
        }

        System.out.println(table.get(SYMBOL));
        System.out.println(table.get(FATHER));
        System.out.println(table.get(SIBLING));
    }
}
