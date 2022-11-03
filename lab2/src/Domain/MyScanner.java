package Domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MyScanner {
    public static final String CONSTANT = "const";
    public static final String IDENTIFIER = "id";
    String tokensFile;
    List<String> tokens;
    List<Pair<String, Integer>> pif;
    List<String> symbolTable;
    String delimiters = " ,;()[]{}";

    FileWriter pifFile = new FileWriter("pif.out");
    FileWriter stFile = new FileWriter("st.out");

    public MyScanner(String tokensFile) throws IOException {
        this.tokensFile = tokensFile;
        tokens = new ArrayList<>();
        pif = new ArrayList<>();
        symbolTable = new ArrayList<>();
    }

    public String scanFile(String filename){
        try (Scanner scanner = new Scanner(new File(filename))) {
            int lineCount = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                lineCount += 1;
                var tokenizer = new StringTokenizer(line, delimiters, true);
                while(tokenizer.hasMoreTokens()) {
                    var token = tokenizer.nextToken();
                    if (!token.equals(" ")) {
                        token = token.strip();
                        if (isReserved(token)) {
                            pif.add(new Pair<>(token, -1));
                            pifFile.write("[" + token + ", -1" + "]\n");
                        } else {
                            if (isConstant(token)) {
                                int pos = addConstantOrSymbol(token);
                                pif.add(new Pair<>(CONSTANT, pos));
                                pifFile.write("[" + CONSTANT + ", " + pos + "]\n");
                            } else if (isIdentifier(token)) {
                                int pos = addConstantOrSymbol(token);
                                pif.add(new Pair<>(IDENTIFIER, pos));
                                pifFile.write("[" + CONSTANT + ", " + pos + "]\n");
                            } else {
                                throw new SyntaxError("Syntax error: unidentified token [" + token + "] on line " + lineCount);
                            }
                        }
                    }
                }
            }
            pifFile.close();
            stFile.close();
            return "lexically correct";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readTokens(){
        try (Scanner scanner = new Scanner(new File(tokensFile))) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine().strip();
                tokens.add(line);
            }
            tokens.add(" ");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isReserved(String token) {
        return tokens.contains(token);
    }

    public boolean isIdentifier(String token) {
        return token.matches("[a-zA-Z]+[a-zA-Z0-9]*");
    }

    public boolean isConstant(String token) {
        return token.matches("\"[a-zA-Z0-9]+\"|'[a-zA-Z0-9]'|[0-9]|[1-9][0-9]*");
    }

    public int addConstantOrSymbol(String token) throws IOException {
        for (int i = 0; i < symbolTable.size(); i++) {
            if (symbolTable.get(i).equals(token)) {
                return i;
            }
        }
        symbolTable.add(token);
        int pos = symbolTable.size() - 1;
        stFile.write("[" + token + ", " + pos + "]\n");
        return pos;
    }
}
