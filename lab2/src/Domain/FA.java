package Domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FA {
    List<String> states;
    List<String> alphabet;
    String initialState;
    List<String> finalStates;
    List<Transition> transitions;

    public FA(String file) {
        transitions = new ArrayList<>();
        states = new ArrayList<>();
        alphabet = new ArrayList<>();
        initialState = "";
        finalStates = new ArrayList<>();
        readFA(file);
    }

    public void readFA(String filename){
        File fa = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(fa);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(scanner.hasNextLine()){
            var line = scanner.nextLine();
            states = List.of(line.split(","));
        }
        if(scanner.hasNextLine()){
            var line = scanner.next();
            alphabet = List.of(line.split(","));
        }
        if(scanner.hasNextLine()){
            var line = scanner.next();
            initialState = line;
        }
        if(scanner.hasNextLine()){
            var line = scanner.next();
            finalStates = List.of(line.split(","));
        }
        while(scanner.hasNextLine()){
            var line = scanner.next();
            var elements = List.of(line.split(","));
            Transition transition = new Transition(elements.get(0), elements.get(2), elements.get(1));
            transitions.add(transition);
        }
    }

    public void menu(){
        System.out.println("1. Print set of states!");
        System.out.println("2. Print the alphabet!");
        System.out.println("3. Print the transitions!");
        System.out.println("4. Print the initial state!");
        System.out.println("5. Print the set of final states!");
        System.out.println("6. Verify if sequence is accepted!");
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
                for(int i=0; i<states.size(); i++){
                    System.out.println(states.get(i));
                }
            }
            else if(option == 2){
                for(int i=0; i<alphabet.size(); i++){
                    System.out.println(alphabet.get(i));
                }
                System.out.println("\n");
            }
            else if(option == 3){
                for(int i=0; i<transitions.size(); i++){
                    System.out.println(transitions.get(i));
                }
                System.out.println("\n");
            }
            else if(option == 4){
                System.out.println(initialState + "\n");
            }
            else if(option == 5){
                for(int i=0; i<finalStates.size(); i++){
                    System.out.println(finalStates.get(i));
                }
                System.out.println("\n");
            }
            else if(option == 6){
                try {
                    var sequence = reader.readLine();
                    verifySequence(sequence);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                System.out.println("This option does not exist!");
        }
    }

    public boolean deterministicFiniteAutomaton(){
        for(int i=0; i<transitions.size(); i++){
            for(int j=i+1; j<transitions.size(); j++){
                String initial1, initial2, value1, value2;
                initial1 = transitions.get(i).initialS;
                initial2 = transitions.get(j).initialS;
                value1 = transitions.get(i).val;
                value2 = transitions.get(j).val;
                if(initial1.equals(initial2) && value1.equals(value2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean verifySequence(String sequence) {
        if(!deterministicFiniteAutomaton()){
//            System.out.println("The FA is not deterministic");
            return false;
        }
        if(sequence.length() == 0){
            if(finalStates.contains(initialState)){
//                System.out.println("Sequence accepted");
                return true;
            }
//            else{
//                System.out.println("Sequence NOT accepted");
//            }

        }
        String state = initialState;
        for(int i=0; i<sequence.length(); i++){
            var found = false;
            for(int j=0; j<transitions.size(); j++){
                if(transitions.get(j).initialS.equals(state) && transitions.get(j).val.equals(String.valueOf(sequence.charAt(i)))){
                    state = transitions.get(j).finalS;
                    found = true;
                    break;
                }
            }
            if(found == false){
//                System.out.println("Sequence NOT accepted");
                return false;
            }
        }
        if(finalStates.contains(state)){
//            System.out.println("Sequence accepted");
            return true;
        }
//        else{
//            System.out.println("Sequence NOT accepted");
//        }
        return false;
    }

}
