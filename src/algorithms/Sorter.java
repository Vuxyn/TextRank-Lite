package algorithms;

import nlp.Sentence;
import nlp.SentenceScore;
import structures.Linkedlist;

public class Sorter {
    
    public static Linkedlist mergeSortByScore(Linkedlist list) {

        if (list == null || list.size() <= 1) {
            return list;
        }
        
        int middle = list.size() / 2;

        Linkedlist left = new Linkedlist();
        Linkedlist right = new Linkedlist();
        
        for (int i = 0; i < middle; i++) {
            left.add(list.get(i));
        }
        for (int i = middle; i < list.size(); i++) {
            right.add(list.get(i));
        }

        left = mergeSortByScore(left);
        right = mergeSortByScore(right);
        
        return mergeByScore(left, right);
    }
    
    private static Linkedlist mergeByScore(Linkedlist left, Linkedlist right) {
        Linkedlist result = new Linkedlist();
        int leftIndex = 0;
        int rightIndex = 0;
        
        // Merge sambil membandingkan score
        while (leftIndex < left.size() && rightIndex < right.size()) {
            SentenceScore leftScore = (SentenceScore) left.get(leftIndex);
            SentenceScore rightScore = (SentenceScore) right.get(rightIndex);
            
            // Bandingkan score, ambil yang lebih kecil (lebih penting)
            if (leftScore.compareByScore(rightScore) <= 0) {
                result.add(leftScore);
                leftIndex++;
            } else {
                result.add(rightScore);
                rightIndex++;
            }
        }
        
        // Tambahkan sisa elemen dari left (jika ada)
        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex));
            leftIndex++;
        }
        
        // Tambahkan sisa elemen dari right (jika ada)
        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex));
            rightIndex++;
        }
        
        return result;
    }

    public static Linkedlist sortByOriginalId(Linkedlist list) {
        if (list == null || list.size() <= 1) {
            return list;
        }
        
        int middle = list.size() / 2;
        
        Linkedlist left = new Linkedlist();
        Linkedlist right = new Linkedlist();
        
        for (int i = 0; i < middle; i++) {
            left.add(list.get(i));
        }
        
        for (int i = middle; i < list.size(); i++) {
            right.add(list.get(i));
        }
        
        left = sortByOriginalId(left);
        right = sortByOriginalId(right);
        
        return mergeById(left, right);
    }

    private static Linkedlist mergeById(Linkedlist left, Linkedlist right) {
        Linkedlist result = new Linkedlist();
        int leftIndex = 0;
        int rightIndex = 0;
        
        while (leftIndex < left.size() && rightIndex < right.size()) {
            SentenceScore leftScore = (SentenceScore) left.get(leftIndex);
            SentenceScore rightScore = (SentenceScore) right.get(rightIndex);
            
            if (leftScore.compareById(rightScore) <= 0) {
                result.add(leftScore);
                leftIndex++;
            } else {
                result.add(rightScore);
                rightIndex++;
            }
        }
        
        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex));
            leftIndex++;
        }
        
        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex));
            rightIndex++;
        }
        
        return result;
    }
    

    public static Linkedlist sortSentenceById(Linkedlist list) {
        
        if (list == null || list.size() <= 1) {
            return list;
        }
        
        int middle = list.size() / 2;
        
        Linkedlist left = new Linkedlist();
        Linkedlist right = new Linkedlist();
        
        for (int i = 0; i < middle; i++) {
            left.add(list.get(i));
        }
        
        for (int i = middle; i < list.size(); i++) {
            right.add(list.get(i));
        }
        
        left = sortSentenceById(left);
        right = sortSentenceById(right);
        
        return mergeSentenceById(left, right);
    }
    

    private static Linkedlist mergeSentenceById(Linkedlist left, Linkedlist right) {
        Linkedlist result = new Linkedlist();
        int leftIndex = 0;
        int rightIndex = 0;
        
        // Merge sambil membandingkan ID
        while (leftIndex < left.size() && rightIndex < right.size()) {
            Sentence leftSentence = (Sentence) left.get(leftIndex);
            Sentence rightSentence = (Sentence) right.get(rightIndex);
            
            // Bandingkan ID, ambil yang lebih kecil
            if (leftSentence.id <= rightSentence.id) {
                result.add(leftSentence);
                leftIndex++;
            } else {
                result.add(rightSentence);
                rightIndex++;
            }
        }
        
        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex));
            leftIndex++;
        }
        
        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex));
            rightIndex++;
        }

        return result;
    }
    
}
