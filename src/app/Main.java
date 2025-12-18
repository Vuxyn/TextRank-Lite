package app;
import algorithms.Dijkstra;
import algorithms.Searcher;
import algorithms.Sorter;
import graph.TextGraph;
import java.util.Scanner;
import nlp.Sentence;
import nlp.SentenceScore;
import structures.Linkedlist;
import structures.NavigationStack;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static TextGraph graph;
    private static Linkedlist sentences;
    //private static NavigationStack navStack;
    private static String originalText = "";
    private static final int MAX_WIDTH = 70;
    private static final int TYPING_MS = 10;
    
    public static void main(String[] args) {
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║    GRAPH-BASED TEXT SUMMARIZATION SYSTEM    ║");
        System.out.println("║                 TEXTRANK-LITE               ║");
        System.out.println("╚═════════════════════════════════════════════╝");
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1: inputText();
                    break;
                case 2: generateSummary();
                    break;
                case 3: displayGraph();
                    break;
                case 4: searchSentence();
                    break;
                case 5: exploreBFS();
                    break;
                case 6: displayStatistics();
                    break;
                case 7: findTop1WithStack();
                    break;
                case 0:
                    running = false;
                    System.out.println("╔═════════════════════════════════════════════╗");
                    System.out.println("║               Terima kasih ^_^              ║");
                    System.out.println("╚═════════════════════════════════════════════╝");
                    break;
                default:
                    System.out.println("╔═════════════════════════════════════════════╗");
                    System.out.println("║             Pilihan tidak valid             ║");
                    System.out.println("╚══════════════════════╦══════════════════════╝");
                    System.out.println("                       ║");
                    System.out.println("                       v");
            }
        }
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║             === MENU UTAMA ===              ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║ 1. Input Teks                               ║");
        System.out.println("║ 2. Generate Ringkasan                       ║");
        System.out.println("║ 3. Tampilkan Graph                          ║");
        System.out.println("║ 4. Cari Kalimat                             ║");
        System.out.println("║ 5. BFS Traversal                            ║");
        System.out.println("║ 6. Statistik                                ║");
        System.out.println("║ 7. Find TOP 1                               ║");
        System.out.println("║ 0. Keluar                                   ║");
        System.out.println("╚═════════════════════════════════════════════╝");
        System.out.print("  Pilih: ");
    }
    
    private static void inputText() {
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║  Masukkan teks (akhiri dengan baris kosong) ║");
        System.out.println("╚═════════════════════════════════════════════╝");
        System.out.print("=> ");
        
        Linkedlist lines = new Linkedlist();
        //StringBuilder sb = new StringBuilder();
        scanner.nextLine();
        
        String line;
        while (!(line = scanner.nextLine()).trim().isEmpty()) {
            //sb.append(line).append(" ");
            lines.add(line);
        }
        
        String text = "";
        for(int i = 0; i < lines.size(); i++){
            text += (String) lines.get(i);
        }

        originalText = text.trim();
        //originalText = sb.toString().trim();
        
        if (originalText.isEmpty()) {
            System.out.println("╔═════════════════════════════════════════════╗");
            System.out.println("║                 Teks kosong                 ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
            return;
        }
        
        processingText(originalText);
        System.out.println("║ [OK] Teks berhasil diproses!                ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println(" - Jumlah kalimat: " + sentences.size());
        System.out.println(" - Jumlah kata: " + countTotalWords());
        System.out.println(" - Graph edges: " + graph.getTotalEdges());
        System.out.println("╚══════════════════════╦══════════════════════╝");
        System.out.println("                       ║");
        System.out.println("                       v");
    }
    
    private static int countTotalWords() {
        int total = 0;
        for (int i = 0; i < sentences.size(); i++) {
            Sentence s = (Sentence) sentences.get(i);
            total += s.words.size();
        }
        return total;
    }
    
    private static void processingText(String text) {
        sentences = new Linkedlist();
        String[] rawSentences = text.split("[.!?]+");
        
        int id = 0;
        for (int i = 0; i < rawSentences.length; i++) {
            String sent = rawSentences[i].trim();
            if (!sent.isEmpty()) {
                sentences.add(new Sentence(id++, sent));
            }
        }
        
        graph = new TextGraph(sentences);
        graph.buildGraph();
        
        //navStack = new NavigationStack();
    }
    
    private static void generateSummary() {
        if (graph == null || sentences.size() == 0) {
            System.out.println("╔═════════════════════════════════════════════╗");
            System.out.println("║      Belum ada teks! Input teks dulu        ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
            return;
        }
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║          === GENERATE RINGKASAN ===         ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║            --- TOP 5 KEYWORDS ---           ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        Linkedlist keywords = extractKeywords();
        displayTopKeywords(keywords, 5);
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║              CENTRALITY SCORES              ║");
        System.out.println("╠═════════════════════════════════════════════╝");
        Linkedlist scores = new Linkedlist();
        
        for (int i = 0; i < sentences.size(); i++) {
            Sentence sent = (Sentence) sentences.get(i);
            Dijkstra dijkstra = new Dijkstra(graph, sent.id, sentences);
            int totalDistance = dijkstra.getTotalDistance();
            scores.add(new SentenceScore(sent, totalDistance));
        }
        
        Linkedlist sortedScores = Sorter.mergeSortByScore(scores);
        
        // Display ranking
        for (int i = 0; i < sortedScores.size(); i++) {
            SentenceScore ss = (SentenceScore) sortedScores.get(i);
            String preview = ss.getSentence().text;
            if (preview.length() > 50) {
                preview = preview.substring(0, 47) + "...";
            }
            System.out.println((i+1) + ". SENT-" + ss.getSentence().id + 
                             " (Score: " + (int) ss.getCentralityScore() + ") - " + preview);
        }
        System.out.println("╚══════════════════════════════════════════════");
        
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║                   RINGKASAN                 ║");
        System.out.println("╚═════════════════════════════════════════════╝");
        
        int topN = Math.min(3, sentences.size());
        Linkedlist topSentences = new Linkedlist();
        
        for (int i = 0; i < topN; i++) {
            topSentences.add(sortedScores.get(i));
        }
        
        topSentences = Sorter.sortByOriginalId(topSentences);
        
        String summary = "";
        for (int i = 0; i < topSentences.size(); i++) {
            SentenceScore ss = (SentenceScore) topSentences.get(i);
            summary += ss.getSentence().text;
            
            if (!ss.getSentence().text.endsWith(".") && 
                !ss.getSentence().text.endsWith("!") && 
                !ss.getSentence().text.endsWith("?")) {
                summary += ".";
            }
            
            if (i < topSentences.size() - 1) {
                summary += " ";
            }
        }
        
        typeText(summary);
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║ [INFO] Ringkasan: " + topN + "/" + sentences.size() + " kalimat               ║");
        System.out.println("╚══════════════════════╦══════════════════════╝");
        System.out.println("                       ║");
        System.out.println("                       v");
    }
    
    private static Linkedlist extractKeywords() {
        String[] stopwordsArray = {
    
            "a", "an", "and", "are", "as", "at", "be", "by", "for", "from",
            "has", "he", "in", "is", "it", "its", "of", "on", "that", "the",
            "to", "was", "will", "with", "can", "have", "this", "but", "or",
            "been", "had", "their", "which", "they", "were", "we", "us", "his",
            "her", "she", "our", "am", "very", "more", "about", "than", "also",
            "each",
       
            "yang", "dan", "di", "ke", "dari", "dengan", "untuk", "pada", "dalam",
            "ini", "itu", "adalah", "atau", "juga", "akan", "oleh", "tidak",
            "ada", "dapat", "telah", "sudah", "sedang", "lebih", "hanya", "bisa",
            "sebagai", "seperti", "antara", "bahwa", "saat", "karena", "jika",
            "bila", "maka", "agar", "supaya", "serta", "para", "bagi", "terhadap"
        };
        
        Linkedlist stopwords = new Linkedlist();
        for (int i = 0; i < stopwordsArray.length; i++) {
            stopwords.add(stopwordsArray[i]);
        }
        
        // Hitung frekuensi kata
        Linkedlist wordFreq = new Linkedlist();
        
        for (int i = 0; i < sentences.size(); i++) {
            Sentence s = (Sentence) sentences.get(i);
            for (int j = 0; j < s.words.size(); j++) {
                String word = (String) s.words.get(j);
                word = word.toLowerCase();
                
                // Skip jika stopword atau terlalu pendek
                if (word.length() < 3 || isStopword(word, stopwords)) {
                    continue;
                }
                
                WordCount existing = findWordCount(wordFreq, word);
                
                if (existing != null) {
                    existing.count++;
                } else {
                    wordFreq.add(new WordCount(word, 1));
                }
            }
        }
        return sortWordCountByFrequency(wordFreq);
    }
    
    private static boolean isStopword(String word, Linkedlist stopwords) {
        for (int i = 0; i < stopwords.size(); i++) {
            String sw = (String) stopwords.get(i);
            if (word.equals(sw)) {
                return true;
            }
        }
        return false;
    }
    
    private static WordCount findWordCount(Linkedlist list, String word) {
        for (int i = 0; i < list.size(); i++) {
            WordCount wc = (WordCount) list.get(i);
            if (wc.word.equals(word)) {
                return wc;
            }
        }
        return null;
    }
    
    private static Linkedlist sortWordCountByFrequency(Linkedlist list) {
        if (list.size() <= 1) return list;
        
        // Selection sort cari yang terbesar, pindah ke list baru
        Linkedlist sorted = new Linkedlist();
        
        while (list.size() > 0) {
            // Cari WordCount dengan count terbesar
            int maxIndex = 0;
            int maxCount = ((WordCount) list.get(0)).count;
            
            for (int i = 1; i < list.size(); i++) {
                WordCount wc = (WordCount) list.get(i);
                if (wc.count > maxCount) {
                    maxCount = wc.count;
                    maxIndex = i;
                }
            }
            
            // Pindahkan yang terbesar ke sorted list
            sorted.add(list.get(maxIndex));
            list.remove(maxIndex);
        }
        
        return sorted;
    }
    
    private static void displayTopKeywords(Linkedlist keywords, int top) {
        int limit = Math.min(top, keywords.size());
        
        for (int i = 0; i < limit; i++) {
            WordCount wc = (WordCount) keywords.get(i);
            System.out.println("║ "+(i+1) + ". " + wc.word + " (" + wc.count + "x)");
        }
        System.out.println("╚═════════════════════════════════════════════╝");
    }
    
    private static void displayGraph() {
        if (graph == null) {
            System.out.println("╠═════════════════════════════════════════════╣");
            System.out.println("║               Belum ada graph               ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
            return;
        }
        
        graph.displayGraph();
    }
    
    private static void searchSentence() {
        if (sentences == null || sentences.size() == 0) {
            System.out.println("╠═════════════════════════════════════════════╣");
            System.out.println("║              Belum ada kalimat              ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
            return;
        }

        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║          === PENCARIAN KALIMAT ===          ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║ 1. Cari berdasarkan Keyword (Linear Search) ║");
        System.out.println("║ 2. Cari berdasarkan ID (Binary Search)      ║");
        System.out.println("╚═════════════════════════════════════════════╝");
        System.out.print("  Pilih metode pencarian: ");
        
        int searchMethod = getIntInput();
        
        if (searchMethod == 1) {
            scanner.nextLine();
            System.out.print("║ Masukkan kata kunci: ");
            String keyword = scanner.nextLine();
            
            Linkedlist results = Searcher.linearSearchByKeyword(sentences, keyword);
            
            if (results.size() == 0) {
                System.out.println("╔═════════════════════════════════════════════╗");
                System.out.println("║                Tidak ditemukan              ║");
                System.out.println("╚══════════════════════╦══════════════════════╝");
                System.out.println("                       ║");
                System.out.println("                       v");
            } else {
                System.out.println("╔═════════════════════════════════════════════╗");
                System.out.println("║ Ditemukan " + results.size() + " kalimat:                        ║");
                System.out.println("╠═════════════════════════════════════════════╝");
                for (int i = 0; i < results.size(); i++) {
                    Sentence s = (Sentence) results.get(i);
                    System.out.println("║ "+(i+1) + ". SENT-" + s.id + ": " + s.text);
                }
                System.out.println("╚══════════════════════╦═══════════════════════");
                System.out.println("                       ║");
                System.out.println("                       v");
            }
            
        } else if (searchMethod == 2) {
            // BINARY SEARCH BY ID
            System.out.print("║ Masukkan ID kalimat (0-" + (sentences.size()-1) + "): ");
            int targetId = getIntInput();
            
            Sentence result = Searcher.binarySearchById(sentences, targetId);
            
            if (result == null) {
                System.out.println("╔═════════════════════════════════════════════╗");
                System.out.println("║           Kalimat tidak ditemukan           ║");
                System.out.println("╚══════════════════════╦══════════════════════╝");
                System.out.println("                       ║");
                System.out.println("                       v");
            } else {
                System.out.println("╔═════════════════════════════════════════════╗");
                System.out.println("║            Kalimat ditemukan!               ║");
                System.out.println("╠═════════════════════════════════════════════╝");
                System.out.println("║ SENT-" + result.id + ": " + result.text);
                System.out.println("╚══════════════════════╦═══════════════════════");
                System.out.println("                       ║");
                System.out.println("                       v");
            }
            
        } else {
            System.out.println("╔═════════════════════════════════════════════╗");
            System.out.println("║             Pilihan tidak valid             ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
        }
    }
    
    private static void exploreBFS() {
        if (graph == null) {
            System.out.println("╠═════════════════════════════════════════════╣");
            System.out.println("║               Belum ada graph               ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
            return;
        }
        
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║            === BFS TRAVERSAL ===            ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.print("║ ID kalimat awal (0-" + (sentences.size() - 1) + "): ");
        int startId = getIntInput();
        
        if (startId < 0 || startId >= sentences.size()) {
            System.out.println("╠═════════════════════════════════════════════╣");
            System.out.println("║                ID tidak valid               ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
            return;
        }
        
        Linkedlist bfsResult = graph.bfsTraversal(startId);
        System.out.println("╚═════════════════════════════════════════════╝");
        System.out.println("╔═════════════════════════════════════════════╗");
        System.out.println("║               Urutan kunjungan              ║");
        System.out.println("╠═════════════════════════════════════════════╝");
        for (int i = 0; i < bfsResult.size(); i++) {
            int id = (int) bfsResult.get(i);  
            Sentence s = (Sentence) sentences.get(id);
            System.out.println("║ "+(i+1) + ". SENT-" + id + ": " + s.text);
        }
        System.out.println("╚══════════════════════╦═══════════════════════");
        System.out.println("                       ║");
        System.out.println("                       v");
    }
    
    private static void displayStatistics() {
        if (sentences == null || sentences.size() == 0) {
            System.out.println("╠═════════════════════════════════════════════╣");
            System.out.println("║                Belum ada data               ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
            return;
        }

        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║            === STATISTIK TEKS ===           ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        
        int totalWords = 0;
        for (int i = 0; i < sentences.size(); i++) {
            Sentence s = (Sentence) sentences.get(i);
            totalWords += s.words.size();
        }
        
        System.out.println("║ Total kalimat: " + sentences.size());
        System.out.println("║ Total kata: " + totalWords);
        System.out.println("║ Rata-rata kata/kalimat: " + (totalWords / sentences.size()));

        if (graph != null) {
            System.out.println("║ Total nodes: " + graph.getNumNodes());
            System.out.println("║ Total edges: " + graph.getTotalEdges());
        }
        System.out.println("╚══════════════════════╦══════════════════════╝");
        System.out.println("                       ║");
        System.out.println("                       v");
    }
    
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus angka! Coba lagi: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    static class WordCount {
        String word;
        int count;
        
        public WordCount(String word, int count) {
            this.word = word;
            this.count = count;
        }
    }

    private static void findTop1WithStack() {
        if (sentences == null || sentences.size() == 0) {
            System.out.println("╠═════════════════════════════════════════════╣");
            System.out.println("║                Belum ada data               ║");
            System.out.println("╚══════════════════════╦══════════════════════╝");
            System.out.println("                       ║");
            System.out.println("                       v");
            return;
        }

        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║   MENCARI TOP 1 KALIMAT MENGGUNAKAN STACK   ║");
        System.out.println("╠═════════════════════════════════════════════╣");
        
        NavigationStack scoreStack = new NavigationStack();
        
        for (int i = 0; i < sentences.size(); i++) {
            Sentence sent = (Sentence) sentences.get(i);
            Dijkstra dijkstra = new Dijkstra(graph, sent.id, sentences);
            int totalDistance = dijkstra.getTotalDistance();
            
            sent.distance = totalDistance;
            scoreStack.push(sent);
        }
        
        NavigationStack tempStack = new NavigationStack();
        
        Sentence top1 = null;
        int minScore = Integer.MAX_VALUE;
        
        while (!scoreStack.isEmpty()) {
            Sentence current = scoreStack.peek(); 
            current = scoreStack.pop(); 
            
            if (current.distance < minScore) {
                minScore = current.distance;
                top1 = current;
            }
            
            tempStack.push(current); 
        }
        
        while (!tempStack.isEmpty()) {
            Sentence s = tempStack.pop();
            scoreStack.push(s);
        }
        
        System.out.println("║ ID: SENT-" + top1.id);
        System.out.println("║ Centrality Score: " + minScore);
        System.out.println("╠═════════════════════════════════════════════╣");
        System.out.println("║ Text:                                       ║");
        System.out.println("╠═════════════════════════════════════════════╝");
        System.out.println("\"" + top1.text + "\"");
        System.out.println("╚══════════════════════╦═══════════════════════");
        System.out.println("                       ║");
        System.out.println("                       v");
    }

    private static void sleep(int ms) {
        try { Thread.sleep(ms); } 
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static void typeText(String text) {
        String[] words = text.split(" ");
        int lineLen = 0;
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (lineLen + word.length() + 1 > MAX_WIDTH) {
                System.out.println();
                lineLen = 0;
            }
            for (char c : word.toCharArray()) {
                System.out.print(c);
                sleep(TYPING_MS);
            }
            if (i < words.length - 1) {
                System.out.print(" ");
                lineLen += word.length() + 1;
            }
        }
        System.out.println();
    }
}



