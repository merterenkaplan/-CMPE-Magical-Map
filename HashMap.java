import java.util.ArrayList;
import java.util.Arrays;

public class HashMap<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        boolean isDeleted; // To mark deleted entries

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false; // Initially, the entry is not deleted
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    public Entry<K, V>[] table;
    public int size;

    @SuppressWarnings("unchecked")
    public HashMap() {
        table = new Entry[INITIAL_CAPACITY];
        size = 0;
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public void put(K key, V value) {
        if (size >= table.length * 0.75) {
            resize();
        }

        int index = getHash(key);
        while (table[index] != null && !table[index].isDeleted) {
            if (table[index].key.equals(key)) {
                table[index].value = value; // Update existing value
                return;
            }
            index = (index + 1) % table.length; // Linear probing
        }

        table[index] = new Entry<>(key, value);
        size++;
    }

    public V get(K key) {
        int index = getHash(key);
        while (table[index] != null) {
            if(!table[index].isDeleted && Arrays.deepEquals(new Object[]{table[index].key}, new Object[]{key})){
            //if (!table[index].isDeleted && table[index].key.equals(key)) {
                return table[index].value; // Return the value if found

            }
            index = (index + 1) % table.length; // Linear probing
        }
        return null; // Return null if key is not found
    }

    public void remove(K key) {
        int index = getHash(key);
        while (table[index] != null) {
            if (!table[index].isDeleted && table[index].key.equals(key)) {
                table[index].isDeleted = true; // Mark entry as deleted
                size--;
                return;
            }
            index = (index + 1) % table.length; // Linear probing
        }
    }



    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        size = 0; // Reset size to 0 and reinsert entries

        for (Entry<K, V> entry : oldTable) {
            if (entry != null && !entry.isDeleted) {
                put(entry.key, entry.value); // Rehash the entries
            }
        }
    }

    //this function helps us to traverse all hash's
    public ArrayList<K> traverse() {
        ArrayList<K> keys = new ArrayList<>();//arraylist to keep keys
        for (Entry<K, V> entry : table) {
            if (entry != null && !entry.isDeleted) {
                keys.add(entry.key);//adding keys to arraylist
            }
        }
        return keys;
    }
}
