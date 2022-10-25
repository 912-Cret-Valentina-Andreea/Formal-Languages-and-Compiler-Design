package Domain;

import java.util.ArrayList;

public class MySymbolTable {

    private ArrayList<ArrayList<String>> items;
    private int capacity;

    public MySymbolTable(int size) {
        this.capacity = size;
        this.items = new ArrayList<>();
        for(int i=0;i<size;++i) this.items.add(new ArrayList<>());
    }

    private int hash(String key) {
        int sum = 0;
        for(int i=0;i< key.length();++i){
            sum += key.charAt(i);
        }
        return sum % capacity;
    }

    public boolean add(String key){
        int hashValue = hash(key);
        if(!items.get(hashValue).contains(key)){
            items.get(hashValue).add(key);
            return true;
        }
        return false;
    }

    public boolean contains(String key){
        int hashValue = hash(key);

        return items.get(hashValue).contains(key);
    }

    public boolean remove(String key){
        int hashValue = hash(key);

        if(items.get(hashValue).contains(key)){
            items.get(hashValue).remove(key);
            return true;
        }
        return false;
    }

}
