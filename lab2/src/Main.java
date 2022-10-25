import Domain.MySymbolTable;

public class Main {

    public static void main(String[] args) {
        System.out.println();
        MySymbolTable symbolTable = new MySymbolTable(10);
        System.out.println("Can add a? "+ symbolTable.add("a"));
        System.out.println("Contains a? "+ symbolTable.contains("a"));
        System.out.println("Contains b? "+ symbolTable.contains("b"));
        System.out.println("Can remove b? "+ symbolTable.remove("b"));
        System.out.println("Can remove a? "+ symbolTable.remove("a"));
        System.out.println("Contains a? "+ symbolTable.contains("a"));
    }

}
