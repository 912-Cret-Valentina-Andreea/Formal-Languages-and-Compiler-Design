import Domain.MyScanner;
import Domain.MySymbolTable;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        MyScanner scanner = new MyScanner("src/Resources/tokens.txt");
        scanner.readTokens();
        System.out.println(scanner.scanFile("src/Resources/program2.txt"));
    }

}
