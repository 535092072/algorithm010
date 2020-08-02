class LRUCache {

    static class Node{
        private int key;
        private int value;

        private Node pre;
        private Node next;

        // 用来记录插入顺序
        private Node preI; 
        private Node nextI;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.pre = this.next = null;
            this.preI = this.nextI = null;
        }
    }

    private final int capacity;
    private final int length;
    private int size;

    private Node [] table;
    private Node head;
    private Node tail;

    /*
    获取大于等于 cap 的数，并且该数是 2 的整数次幂。
    主要思想是把 cap 的从高位到低位第一个不为0 的数到最低位的数全部赋值为 1
    例子：    9 : 00001001
        step 0 : 00001001
        step 1 : 00001000 // 减一，这样做的目的是为了防止原来的数本身就是 2 的整数次幂
        step 2 : 
                n | = n >>> 1
            即：
                00001000
              ^ 00000100
        _----------------
                00001100
        step 3: 
                n |= n >>> 2;
            即:
                00001100
              ^ 00000011
        ----------------
                00001111
        .
        .
        .

        step last: 
               return  n + 1;
               00010000 // 16

    */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }

    // hashmap里的 hash 方法， 原对象的hashCode的高16位 与 低16位异或
    static final int hash(Object key) {
        int h = 0;
        return (key == null) ? 0 : ((h = key.hashCode()) ^ h >>> 16);
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = this.tail = null;
        // 数组长度赋值为两倍长度，因为没有写扩容方法，有个样例结点太多，导致冲突太多
        this.length = tableSizeFor(2 * capacity);
        table = new Node[length];
    }
    
    public int get(int key) {
        
        int h = hash(key);
        Node p = table[h & length - 1];
        while (p != null) {
            if (p.key == key) break; // 在 hashcode 相等的情况下， 比较键是否相等（equals)
            p = p.next;
        }
        if (p == null) return -1;
        adjust(p);  // 在hash表里面的位置不变，但是在先进先出的链表里的位置改变
        return p.value;
    }
    
    public void put(int key, int value) {
        
        int h = hash(key);
        int index = h & length - 1;
        Node p = table[index];
        
        while (p != null) {
            if (p.key == key) break;
            p = p.next;
        }
        if (p != null) {
            p.value = value;
            adjust(p); 
            return;
        }
        if (size >= capacity) removeHead(); // 不仅仅在链表中删除，也要在hash表中删除。
        
        Node t = new Node(key, value);
        
        // 在 哈希表 中添加
        if(table[index] == null) {
            table[index] = t;
        }
        else {
            Node e;
            p = table[index];
            while ((e = p.next) != null) p = p.next;
            t.pre = p;
            p.next = t;
        }
        
        // 在 链表 中添加
        if (tail == null) {
            head = tail = t;
        }
        else {
            t.preI = tail;
            tail = tail.nextI = t;
        }
        ++size;
    }

    // 调整 链表 的次序
    public void adjust(Node p) {
        if (p == tail) return;
        else if (p == head) {
            Node t = head;
            head = head.nextI;
            head.preI = null;

            t.preI = tail;
            t.nextI = null;
            tail = tail.nextI = t;

            return;
        }
        else {

            p.preI.nextI = p.nextI;
            p.nextI.preI = p.preI;

            p.preI = tail;
            p.nextI = null;
            tail = tail.nextI = p;
            return;
        }
    }

    public void removeHead() {
         
        if (head == null) return;

        int h = hash(head.key);
        int index = (h & length - 1);
        
        // 在 哈希表中删除
        if (head.pre == null) {
            table[index] = head.next;
            head.pre = head.next = head;  // 指向自己，垃圾回收
        }
        else if (head.next == null) {
            head.pre.next = null;
            head.pre = head.next = head;
        }
        else {
            head.pre.next = head.next;
            head.next.pre = head.pre;
            
            head.pre= head.next = head;
        }
        
        // 在链表中删除
        if (head == tail) {
            
            Node t = head;
            head = tail = null;
            
            t.preI = t.nextI = t;
        }
        else {
            Node t = head;
            head = head.nextI;
            head.preI = null;
            t.preI = t.nextI = null;
            
        }
        --size;
    }
}