import Domain.Grammar;
import Domain.Parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Grammar grammar = new Grammar("src/Resources/g2.txt");
//        grammar.run();

        Parser parser = new Parser();
    }
}