public class Edge {
    int toNode;
    int weight;

    public Edge(int toNode, int weight){
        this.toNode = toNode;
        this.weight = weight;
    }

    @Override
    public String toString(){
        return "SENTENCE-" + toNode + "(" + weight + ")";
    }
}
