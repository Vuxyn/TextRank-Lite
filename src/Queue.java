public class Queue {
    private QueueNode front;
    private QueueNode rear;
    private int size;

    private static class QueueNode {
        Object data;
        QueueNode next;

        QueueNode(Object data){
            this.data = data;
            this.next = null;
        }
    }

    public Queue(){
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(Object data) {
        QueueNode newNode = new QueueNode(data);
        
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public Object dequeue() {
        if (isEmpty()) {
            return null;
        }
        Object data = front.data;
        front = front.next;
        
        // Jika queue jadi kosong, reset rear juga
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    public Object peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }

    public boolean isEmpty(){ return front == null; }
    public int size(){ return size; }

    public void clear(){
        front = null;
        rear = null;
        size = 0;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }
        
        QueueNode current = front;
        System.out.print("Queue: ");
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
