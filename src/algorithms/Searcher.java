package algorithms;

import nlp.Sentence;
import nlp.SentenceScore;
import structures.Linkedlist;

public class Searcher {

    public static Sentence linearSearchById(Linkedlist list, int targetId){
        if(list == null || list.size() == 0){
            return null;
        }

        for(int i = 0; i < list.size(); i++){
            Sentence sentence = (Sentence) list.get(i);
            if(sentence.id == targetId){
                return sentence;
            }
        }

        return null;
    }

    public static Linkedlist linearSearchByKeyword(Linkedlist list, String keyword) {
        Linkedlist results = new Linkedlist();
        
        if (list == null || list.size() == 0 || keyword == null || keyword.trim().isEmpty()) {
            return results;
        }
        
        String lowerKeyword = keyword.toLowerCase();
        
        for (int i = 0; i < list.size(); i++) {
            Sentence sentence = (Sentence) list.get(i);
            String lowerText = sentence.text.toLowerCase();
            
            if (lowerText.contains(lowerKeyword)) {
                results.add(sentence);
            }
        }
        return results;
    }

    public static Sentence binarySearchById(Linkedlist list, int targetId){
        if(list == null || list.size() == 0){
            return null;
        }

        int left = 0;
        int right = list.size() - 1;

        while(left <= right){
            int middle = left + (right - left)/2;
            Sentence middlSentence = (Sentence) list.get(middle);
            int middleId = middlSentence.id;

            if(middleId == targetId){
                return middlSentence;
            }else if(middleId < targetId){
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        return null;
    }

    public static SentenceScore linearSearchScoreById(Linkedlist list, int targetId) {
        if (list == null || list.size() == 0) {
            return null;
        }
        
        for (int i = 0; i < list.size(); i++) {
            SentenceScore sentenceScore = (SentenceScore) list.get(i);
            if (sentenceScore.getSentence().id == targetId) {
                return sentenceScore;
            }
        }
        
        return null;
    }

    public static SentenceScore binarySearchScoreById(Linkedlist list, int targetId) {
        if (list == null || list.size() == 0) {
            return null;
        }
        
        int left = 0;
        int right = list.size() - 1;
        
        while (left <= right) {
            int middle = left + (right - left) / 2;
            SentenceScore middleScore = (SentenceScore) list.get(middle);
            int middleId = middleScore.getSentence().id;
            
            if (middleId == targetId) {
                return middleScore; 
            } else if (middleId < targetId) {
                left = middle + 1; // Cari di kanan
            } else {
                right = middle - 1; // Cari di kiri
            }
        }
        
        return null; 
    }

    public static void displaySearchResults(Linkedlist results, String keyword) {
        if (results == null || results.size() == 0) {
            System.out.println("Tidak ada kalimat yang mengandung keyword: \"" + keyword + "\"");
            return;
        }
        
        System.out.println("\n=== Hasil Pencarian untuk \"" + keyword + "\" ===");
        System.out.println("Ditemukan " + results.size() + " kalimat:");
        System.out.println();
        
        for (int i = 0; i < results.size(); i++) {
            Sentence sentence = (Sentence) results.get(i);
            System.out.println("ID " + sentence.id + ": " + sentence.text);
        }
        System.out.println("================================================");
    }
    

    public static void displaySentenceDetail(Sentence sentence) {
        if (sentence == null) {
            System.out.println("Kalimat tidak ditemukan.");
            return;
        }
        
        System.out.println("\n=== Detail Kalimat ===");
        System.out.println("ID       : " + sentence.id);
        System.out.println("Text     : " + sentence.text);
        System.out.print("Words    : [");
        
        for (int i = 0; i < sentence.words.size(); i++) {
            String word = (String) sentence.words.get(i);
            System.out.print(word);
            if (i < sentence.words.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("Total Words: " + sentence.words.size());
        System.out.println("======================");
    }
    
}
