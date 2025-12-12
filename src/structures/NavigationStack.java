package structures;

import nlp.Sentence;

public class NavigationStack {
    private StackNode top;
    private int size;

    private static class StackNode {
        Sentence data;
        StackNode next;
        
        StackNode(Sentence data) {
            this.data = data;
            this.next = null;
        }
    }
    
    public NavigationStack() {
        this.top = null;
        this.size = 0;
    }
    
    public void push(Sentence sentence) {
        StackNode newNode = new StackNode(sentence);
        newNode.next = top;
        top = newNode;
        size++;
    }
    
    public Sentence pop() {
        if (isEmpty()) {
            return null;
        }
        
        Sentence data = top.data;
        top = top.next;
        size--;
        return data;
    }
    
    public Sentence peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }
    
    public boolean isEmpty() { return top == null; }
    public int size() { return size; }
    
    public void clear() {
        top = null;
        size = 0;
    }

    public void displayHistory() {
        if (isEmpty()) {
            System.out.println("History kosong!");
            return;
        }
        
        System.out.println("\n=== HISTORY NAVIGASI ===");
        System.out.println("(Dari terbaru ke terlama)\n");
        
        StackNode current = top;
        int index = 1;
        
        while (current != null) {
            Sentence s = current.data;
            System.out.println(index + ". SENTENCE-" + s.id);
            
            // Tampilkan preview text (max 60 karakter)
            String preview = s.text;
            if (preview.length() > 60) {
                preview = preview.substring(0, 60) + "...";
            }
            System.out.println("   " + preview);
            
            if (current.next != null) {
                System.out.println("   ↓");
            }
            
            current = current.next;
            index++;
        }
        System.out.println("\nTotal: " + size + " kalimat dalam history");
    }
}
