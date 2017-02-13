public class HashMap<K, V> extends AbstractMap<K, V> implements Cloneable, Serializable {

    private static final int MINIMUM_CAPACITY = 4;//最小容量
    private static final int MAXIMUM_CAPACITY = 1 << 30;//最大容量
    static final float DEFAULT_LOAD_FACTOR = .75F;//装载因子
    transient int size;

    private static final Entry[] EMPTY_TABLE = new HashMapEntry[MINIMUM_CAPACITY >>> 1];
    transient HashMapEntry<K, V>[] table;



    static class HashMapEntry<K, V> implements Entry<K, V> {
        final K key;
        V value;
        final int hash;
        HashMapEntry<K, V> next;

    }

}


bbbbbbbbbbbbbb