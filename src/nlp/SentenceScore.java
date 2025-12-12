package nlp;

public class SentenceScore {
    private Sentence sentence;
    private double centralityScore;

    public SentenceScore(Sentence sentence, double centralityScore){
        this.sentence = sentence;
        this.centralityScore = centralityScore;
    }

    //Getter
    public Sentence getSentence(){ return sentence; }
    public double getCentralityScore(){ return centralityScore; }

    //Setter
    public void setSentence(Sentence sentence){
        this.sentence = sentence;
    }
    public void setCentralityScore(double centralityScore){
        this.centralityScore = centralityScore;
    }

    //Compare
    public int compareByScore(SentenceScore other){
        if(this.centralityScore < other.centralityScore){
            return -1;
        } else if(this.centralityScore > other.centralityScore){
            return 1;
        } else {
            return 0;
        }
    }

    public int compareById(SentenceScore other){
        if(this.sentence.id < other.sentence.id){
            return -1;
        } else if(this.sentence.id > other.sentence.id){
            return 1;
        } else {
            return 0;
        }
    }
}
