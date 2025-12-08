import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static TextGraph graph;
    private static Linkedlist sentences;
    private static NavigationStack navStack;
    private static String originalText = "";
    
    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println("|    GRAPH-BASED TEXT SUMMARIZATION SYSTEM    |");
        System.out.println("|    (Dijkstra's Algorithm for Centrality)    |");
        System.out.println("===============================================");
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    inputText();
                    break;
                case 2:
                    generateSummary();
                    break;
                case 3:
                    displayGraph();
                    break;
                case 4:
                    searchSentence();
                    break;
                case 5:
                    exploreBFS();
                    break;
                case 6:
                    displayStatistics();
                    break;
                case 0:
                    running = false;
                    System.out.println("==========================");
                    System.out.println("|    Terima kasih ^_^    |");
                    System.out.println("==========================");
                    break;
                default:
                    System.out.println("==========================");
                    System.out.println("|  Pilihan tidak valid!  |");
                    System.out.println("==========================");
                    System.out.println("             |            ");
                    System.out.println("             |            ");
                    System.out.println("             v            ");
            }
        }
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("==========================");
        System.out.println("|   === MENU UTAMA ===   |");
        System.out.println("==========================");
        System.out.println("| 1. Input Teks          |");
        System.out.println("| 2. Generate Ringkasan  |");
        System.out.println("| 3. Tampilkan Graph     |");
        System.out.println("| 4. Cari Kalimat        |");
        System.out.println("| 5. BFS Traversal       |");
        System.out.println("| 6. Statistik           |");
        System.out.println("| 0. Keluar              |");
        System.out.println("=========================|");
        System.out.print("| Pilih: ");
    }
    
    private static void inputText() {
        System.out.println("=========================|");
        System.out.println("                         |");
        System.out.println("                         v");
        System.out.println("==================================================");
        System.out.println("|   Masukkan teks (akhiri dengan baris kosong)   |");
        System.out.println("==================================================");
        
        StringBuilder sb = new StringBuilder();
        scanner.nextLine();
        
        String line;
        while (!(line = scanner.nextLine()).trim().isEmpty()) {
            sb.append(line).append(" ");
        }
        
        originalText = sb.toString().trim();
        
        if (originalText.isEmpty()) {
            System.out.println("==========================");
            System.out.println("|      Teks kosong!      |");
            System.out.println("==========================");
            System.out.println("             |            ");
            System.out.println("             |            ");
            System.out.println("             v            ");
            return;
        }
        
        processingText(originalText);
        System.out.println("==================================================");
        System.out.println("|          [OK] Teks berhasil diproses!          |");
        System.out.println("==================================================");
        System.out.println("| - Jumlah kalimat: " + sentences.size());
        System.out.println("| - Jumlah kata: " + countTotalWords());
        System.out.println("| - Graph edges: " + graph.getTotalEdges());
        System.out.println("=================================================|");
        System.out.println("                                                 |");
        System.out.println("             -------------------------------------");
        System.out.println("             |            ");
        System.out.println("             v            ");
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
        
        navStack = new NavigationStack();
    }
    
    private static void generateSummary() {
        if (graph == null || sentences.size() == 0) {
            System.out.println("\nBelum ada teks! Input teks dulu.");
            return;
        }
        
        System.out.println("\n=== GENERATE RINGKASAN ===\n");
        
        System.out.println("--- TOP 5 KEYWORDS ---");
        Linkedlist keywords = extractKeywords();
        displayTopKeywords(keywords, 5);
        
        System.out.println("\n--- CENTRALITY SCORES ---");
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
                             " (Score: " + (int)ss.getCentralityScore() + ") - " + preview);
        }
        
        System.out.println("\n--- RINGKASAN (TOP 3 KALIMAT) ---");
        
        int topN = Math.min(3, sentences.size());
        Linkedlist topSentences = new Linkedlist();
        
        for (int i = 0; i < topN; i++) {
            topSentences.add(sortedScores.get(i));
        }
        
        topSentences = Sorter.sortByOriginalId(topSentences);
        
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < topSentences.size(); i++) {
            SentenceScore ss = (SentenceScore) topSentences.get(i);
            summary.append(ss.getSentence().text);
            
            if (!ss.getSentence().text.endsWith(".") && 
                !ss.getSentence().text.endsWith("!") && 
                !ss.getSentence().text.endsWith("?")) {
                summary.append(".");
            }
            
            if (i < topSentences.size() - 1) {
                summary.append(" ");
            }
        }
        
        System.out.println("\n" + summary.toString());
        System.out.println("\n[INFO] Ringkasan: " + topN + "/" + sentences.size() + " kalimat");
    }
    
    private static Linkedlist extractKeywords() {
        String[] stopwordsArray = {
            // English
            "a", "an", "and", "are", "as", "at", "be", "by", "for", "from",
            "has", "he", "in", "is", "it", "its", "of", "on", "that", "the",
            "to", "was", "will", "with", "can", "have", "this", "but", "or",
            "been", "had", "their", "which", "they", "were", "we", "us", "his",
            "her", "she", "our", "am", "very", "more", "about", "than", "also",
            // Indonesian
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
            System.out.println((i+1) + ". " + wc.word + " (" + wc.count + "x)");
        }
    }
    
    private static void displayGraph() {
        if (graph == null) {
            System.out.println("\nBelum ada graph!");
            return;
        }
        
        graph.displayGraph();
    }
    
    private static void searchSentence() {
        if (sentences == null || sentences.size() == 0) {
            System.out.println("\nBelum ada kalimat!");
            return;
        }
        
        System.out.println("\n=== PENCARIAN KALIMAT ===");
        scanner.nextLine();
        System.out.print("Masukkan kata kunci: ");
        String keyword = scanner.nextLine();
        
        Linkedlist results = Searcher.linearSearchByKeyword(sentences, keyword);
        
        if (results.size() == 0) {
            System.out.println("Tidak ditemukan.");
        } else {
            System.out.println("\nDitemukan " + results.size() + " kalimat:");
            for (int i = 0; i < results.size(); i++) {
                Sentence s = (Sentence) results.get(i);
                System.out.println((i+1) + ". SENT-" + s.id + ": " + s.text);
            }
        }
    }
    
    private static void exploreBFS() {
        if (graph == null) {
            System.out.println("\nBelum ada graph!");
            return;
        }
        
        System.out.println("\n=== BFS TRAVERSAL ===");
        System.out.print("ID kalimat awal (0-" + (sentences.size() - 1) + "): ");
        int startId = getIntInput();
        
        if (startId < 0 || startId >= sentences.size()) {
            System.out.println("ID tidak valid!");
            return;
        }
        
        Linkedlist bfsResult = graph.bfsTraversal(startId);
        
        System.out.println("\nUrutan kunjungan:");
        for (int i = 0; i < bfsResult.size(); i++) {
            int id = (int) bfsResult.get(i);  
            Sentence s = (Sentence) sentences.get(id);
            System.out.println((i+1) + ". SENT-" + id + ": " + s.text);
        }
    }
    
    private static void displayStatistics() {
        if (sentences == null || sentences.size() == 0) {
            System.out.println("\nBelum ada data!");
            return;
        }
        
        System.out.println("\n=== STATISTIK TEKS ===");
        
        int totalWords = 0;
        for (int i = 0; i < sentences.size(); i++) {
            Sentence s = (Sentence) sentences.get(i);
            totalWords += s.words.size();
        }
        
        System.out.println("Total kalimat: " + sentences.size());
        System.out.println("Total kata: " + totalWords);
        System.out.println("Rata-rata kata/kalimat: " + (totalWords / sentences.size()));
        
        if (graph != null) {
            System.out.println("Total nodes: " + graph.getNumNodes());
            System.out.println("Total edges: " + graph.getTotalEdges());
        }
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
}



