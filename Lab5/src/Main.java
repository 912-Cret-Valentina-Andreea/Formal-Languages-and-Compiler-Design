import Domain.Grammar;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Grammar grammar = new Grammar("src/Resources/g1.txt");
        grammar.run();
    }
}