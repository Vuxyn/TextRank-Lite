package structures;

public class Linkedlist {
    private Node head;
    private Node tail;
    private int size;

    private static class Node {
        Object data;
        Node next;

        Node(Object data){
            this.data = data;
            this.next = null;
        }
    }

    public Linkedlist(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(Object data){
        Node newNode = new Node(data);
        if(head == null){
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public Object get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public void set(int index, Object data) {
        if (index < 0 || index >= size) {
            return;
        }
        
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.data = data;
    }

    public boolean remove(int index) {
        if (index < 0 || index >= size) {
            return false;
        }
        
        if (index == 0) {
            // Hapus head
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            // Traverse sampai node sebelum target
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            // Skip node yang dihapus
            current.next = current.next.next;
            
            // Update tail jika node terakhir dihapus
            if (current.next == null) {
                tail = current;
            }
        }
        size--;
        return true;
    }

    public boolean contains(Object data){
        Node current = head;
        while(current != null){
            if(current.data.equals(data)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int size(){ return size; }
    public boolean isEmpty(){ return size == 0;  }

    public void clear(){
        head = tail = null;
        size = 0;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("List is empty");
            return;
        }
        
        Node current = head;
        System.out.print("LinkedList: ");
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) {
                System.out.print(" -> ");
            }
            current = current.next;
        }
        System.out.println();
    }
}
