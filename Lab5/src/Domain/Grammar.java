package Domain;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class Grammar {
    String start;
    List<String> terminals;
    List<String> nonterminals;
    List<Production> productions;
    public Grammar(String file) {
        terminals = new ArrayList<>();
        nonterminals = new ArrayList<>();
        productions = new ArrayList<>();
        start = "";
        readGrammar(file);
    }

    public Grammar(){
        terminals = new ArrayList<>();
        nonterminals = new ArrayList<>();
        productions = new ArrayList<>();
        start = "";
    }

    public List<String> getTerminals(){
        return this.terminals;
    }

    public String getStart(){
        return this.start;
    }


    public List<String> getNonterminals(){
        return this.nonterminals;
    }

    public void readGrammar(String filename){
        File grammarFile = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(grammarFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(scanner.hasNextLine()){
            var line = scanner.nextLine();
            start = line;
        }
        if(scanner.hasNextLine()){
            var line = scanner.nextLine();
            terminals = List.of(line.split(","));
        }
        if(scanner.hasNextLine()){
            var line = scanner.nextLine();
            nonterminals = List.of(line.split(","));
        }

        while(scanner.hasNextLine()){
            var line = scanner.nextLine();
            List<String> productionline = Arrays.asList(line.split("->"));
            List<String> leftPart = Arrays.asList(productionline.get(0).split(","));
            List<String> allRightPart = Arrays.asList(productionline.get(1).split("\\|"));
            for(int i = 0; i< allRightPart.size(); i++){
                List<String> rightPart = Arrays.asList(allRightPart.get(i).split(","));
                productions.add(new Production(leftPart, rightPart));
            }
        }
    }

    public void menu(){
        System.out.println("1. Print set of nonterminals!");
        System.out.println("2. Print set of terminals!");
        System.out.println("3. Print set of productions!");
        System.out.println("4. Print the productions for a nonterminal!");
        System.out.println("5. CFG check!");
        System.out.println("0. Exit");
    }

    public void run(){
        boolean done = false;
        Scanner console = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        int option;
        while(!done) {
            menu();
            System.out.print("Enter option: ");
            option = console.nextInt();
            if(option == 0){
                return;
            }
            else if(option == 1){
                for(int i=0; i<nonterminals.size(); i++){
                    System.out.println(nonterminals.get(i));
                }
            }
            else if(option == 2){
                for(int i=0; i<terminals.size(); i++){
                    System.out.println(terminals.get(i));
                }
                System.out.println("\n");
            }
            else if(option == 3){
                for(int i=0; i<productions.size(); i++){
                    System.out.println(productions.get(i));
                }
                System.out.println("\n");
            }
            else if(option == 4){
                System.out.println("Enter a nonterminal: " + "\n");
                try {
                    var nonterminal = reader.readLine();
                    SOUTProductionsForNonterminal(nonterminal);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(option == 5){
                if(CFG()){
                    System.out.println("This grammar is CFG");
                }
                else{
                    System.out.println("This grammar is NOT CFG");
                }
            }
            else
                System.out.println("This option does not exist!");
        }
    }

    public void SOUTProductionsForNonterminal(String nonterminal) {
        for (Production production : productions) {
            if (production.leftPart.contains(nonterminal)) {
                System.out.println(production);
            }
        }
    }

    public List<Production> getProductionsForNonterminal(String nonterminal){
        List<Production> result = new ArrayList<>();
        for(Production production: productions) {
            if (production.leftPart.contains(nonterminal)) {
                result.add(production);
            }
        }
        return result;
    }

    public boolean CFG() {
        for (Production production : productions) {
            if (production.leftPart.size() > 1) {
                return false;
            }
        }
        return true;
    }
}
