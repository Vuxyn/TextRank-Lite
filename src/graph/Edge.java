package graph;

public class Edge {
    public int toNode;
    public int weight;

    public Edge(int toNode, int weight){
        this.toNode = toNode;
        this.weight = weight;
    }

    @Override
    public String toString(){
        return "SENTENCE-" + toNode + "(" + weight + ")";
    }
}
