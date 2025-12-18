package nlp;
import structures.Linkedlist;

public class Sentence {
    public int id;
    public String text;
    public Linkedlist words;

    //dijkstra
    public int distance;
    public boolean visited;
    public Sentence prev;

    public Sentence(int id, String text){
        this.id = id;
        this.text = text;
        this.words = new Linkedlist();

        //untukk dijkstra
        this.distance = Integer.MAX_VALUE;
        this.visited = false;
        this.prev = null;

        tokenize();
    }

    private void tokenize(){
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
        String[] tokens = cleaned.split("\\s+");

        for(int i = 0; i < tokens.length; i++){
            String token = tokens[i].trim();
            if(!token.isEmpty()){
                words.add(token);
            }
        }
    }

    public int calculateSimilarity(Sentence other) {
        int count = 0;
        
        // Loop setiap kata di kalimat ini
        for (int i = 0; i < this.words.size(); i++) {
            String word1 = (String) this.words.get(i);
            
            // Cek apakah kata ini ada di kalimat lain
            for (int j = 0; j < other.words.size(); j++) {
                String word2 = (String) other.words.get(j);
                
                if (word1.equals(word2)) {
                    count++;
                    break;  // Hitung sekali saja per kata
                }
            }
        }
        return count;
    }

    public boolean containsWord(String keyword){
        keyword = keyword.toLowerCase();
        for(int i = 0; i < words.size(); i++){
            String word = (String) words.get(i);
            if(word.equals(keyword)){
                return true;
            }
        }
        return false;
    }

    public void resetDijkstra(){
        this.distance = Integer.MAX_VALUE;
        this.visited = false;
        this.prev = null;
    }

    public int getWordCount(){ return words.size(); }
}
