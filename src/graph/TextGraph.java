package graph;

import nlp.Sentence;
import structures.Linkedlist;
import structures.Queue;

public class TextGraph {
    
    private Linkedlist sentences;
    private Linkedlist adjacencyList;
    private int numNodes;

    public TextGraph(Linkedlist sentences){
        this.sentences = sentences;
        this.numNodes = sentences.size();
        this.adjacencyList = new Linkedlist();
        
        for (int i = 0; i < numNodes; i++) {
            adjacencyList.add(new Linkedlist());
        }
    }

    private void addEdge(int from, int to, int weight) {
        Linkedlist edgeList = (Linkedlist) adjacencyList.get(from);
        edgeList.add(new Edge(to, weight));
    }

    public void buildGraph() {
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║ Building graph...                           ║");
        System.out.println("║ Calculating similarities between sentences..║");
        System.out.println("╠═════════════════════════════════════════════╣");
        
        for (int i = 0; i < numNodes; i++) {
            Sentence sentenceI = (Sentence) sentences.get(i);
            
            for (int j = 0; j < numNodes; j++) {
                if (i == j) {
                    continue; 
                }
                
                Sentence sentenceJ = (Sentence) sentences.get(j);
                
                // Hitung similarity (jumlah kata yang sama)
                int similarity = sentenceI.calculateSimilarity(sentenceJ);
                
                int distance = 100 - (similarity * 10);
                
                if (distance < 1) {
                    distance = 1;
                }

                addEdge(i, j, distance);
            }
        }
    }

    public void displayGraph() {
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║            Graph Adjancency List            ║");
        System.out.println("╠═════════════════════════════════════════════╝");
        
        for (int i = 0; i < numNodes; i++) {
            System.out.print("║ SENT " + i + " = ");
            
            Linkedlist edges = (Linkedlist) adjacencyList.get(i);
            
            if (edges.isEmpty()) {
                System.out.println("╠═════════════════════════════════════════════╗");
                System.out.println("║                  No Edges                   ║");
                System.out.println("╚═════════════════════════════════════════════╝");
            } else {
                for (int j = 0; j < edges.size(); j++) {
                    Edge e = (Edge) edges.get(j);
                    System.out.print("SENT " + e.toNode + " (" + e.weight + ")");
                    
                    if (j < edges.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
        System.out.println("╚══════════════════════════════════════════════");
        
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║              Graph Statistics:              ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║ - Total nodes: " + numNodes);
        System.out.println("║ - Total edges: " + getTotalEdges());
        System.out.println("╚══════════════════════╦══════════════════════╝");
        System.out.println("                       ║");
        System.out.println("                       v");
    }

    public int getTotalEdges(){
        int total = 0;
        for(int i = 0; i < numNodes; i++){
            Linkedlist edges = (Linkedlist) adjacencyList.get(i);
            total += edges.size();
        }
        return total;
    }

    public Linkedlist getEdges(int nodeId) {
        if (nodeId >= 0 && nodeId < numNodes) {
            return (Linkedlist) adjacencyList.get(nodeId);
        }
        return new Linkedlist();
    }

    public Linkedlist bfsTraversal(int startNode){
        Linkedlist result = new Linkedlist();
        boolean[] visited = new boolean[numNodes];
        Queue queue = new Queue();

        visited[startNode] = true;
        queue.enqueue(startNode);

        while(!queue.isEmpty()){
            int current = (int) queue.dequeue();
            result.add(current);

            Linkedlist edges = (Linkedlist) adjacencyList.get(current);
            for(int i = 0; i < edges.size(); i++){
                Edge e = (Edge) edges.get(i);
                if(!visited[e.toNode]){
                    visited[e.toNode] = true;
                    queue.enqueue(e.toNode);
                }
            }
        }

        return result;
    }

    public int getNumNodes(){ return numNodes; }

}
