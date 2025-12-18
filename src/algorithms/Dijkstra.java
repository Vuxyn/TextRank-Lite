package algorithms;

import graph.Edge;
import graph.TextGraph;
import nlp.Sentence;
import structures.Linkedlist;

public class Dijkstra {
    
    private TextGraph graph;
    private Linkedlist sentences;
    private int sourceNode;

    public Dijkstra(TextGraph graph, int sourceNode, Linkedlist sentences){
        this.graph = graph;
        this.sourceNode = sourceNode;
        this.sentences = sentences;

        resetAllSentences();
        runDijkstra();
    }

    public void resetAllSentences(){
        for(int i = 0; i < sentences.size(); i++){
            Sentence s = (Sentence) sentences.get(i);
            s.resetDijkstra();

            if(s.id == sourceNode){
                s.distance = 0;
            }
        }
    }

    private void runDijkstra() {

        while (true) {
            
            Sentence minSentence = null;
            int minDistance = Integer.MAX_VALUE;
            
            for (int i = 0; i < sentences.size(); i++) {
                Sentence s = (Sentence) sentences.get(i);
                
                if (!s.visited && s.distance < minDistance) {
                    minDistance = s.distance;
                    minSentence = s;
                }
            }
            
            if (minSentence == null) {
                break;
            }
            
            minSentence.visited = true;
            
            // Update distance semua tetangga dari minSentence
            Linkedlist edges = graph.getEdges(minSentence.id);
            
            for (int i = 0; i < edges.size(); i++) {
                Edge e = (Edge) edges.get(i);
                Sentence neighbor = getSentenceById(e.toNode);
                
                if (neighbor != null && !neighbor.visited) {
                    int newDistance = minSentence.distance + e.weight;
                    
                    if (newDistance < neighbor.distance) {
                        neighbor.distance = newDistance;
                        neighbor.prev = minSentence;
                    }
                }
            }
        }
    }

    private Sentence getSentenceById(int id) {
        for (int i = 0; i < sentences.size(); i++) {
            Sentence s = (Sentence) sentences.get(i);
            if (s.id == id) {
                return s;
            }
        }
        return null;
    }

    public int getTotalDistance() {
        int total = 0;
        
        for (int i = 0; i < sentences.size(); i++) {
            Sentence s = (Sentence) sentences.get(i);
            
            // Jangan hitung distance ke diri sendiri
            // skip jika tidak reachable (infinity)
            if (s.id != sourceNode && s.distance != Integer.MAX_VALUE) {
                total += s.distance;
            }
        }
        return total;
    }

    public int getDistance(int nodeId) {
        Sentence s = getSentenceById(nodeId);
        if (s != null) {
            return s.distance;
        }
        return Integer.MAX_VALUE;
    }

    public void displayDistances() {
        System.out.println("           DIJKSTRA SHORTEST PATH               ");
        System.out.println("\nSource: SENT-" + sourceNode);
        System.out.println("\nShortest distances:");
        
        for (int i = 0; i < sentences.size(); i++) {
            Sentence s = (Sentence) sentences.get(i);
            
            if (s.id == sourceNode) {
                System.out.println("  SENT-" + s.id + ": 0 (source)");
            } else if (s.distance == Integer.MAX_VALUE) {
                System.out.println("  SENT-" + s.id + ": ∞ (unreachable)");
            } else {
                System.out.println("  SENT-" + s.id + ": " + s.distance);
            }
        }
        
        System.out.println("\nCentrality Score: " + getTotalDistance());
        System.out.println("   (Lower = more central/important)");
    }
}
