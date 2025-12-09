# TextRank-Lite

A lightweight Java implementation of the TextRank algorithm for automatic text summarization. This project builds a graph-based model where sentences are represented as nodes, and edges are weighted based on sentence similarity. Using Dijkstra's algorithm for centrality calculation, it extracts the most important sentences to generate concise summaries.

## Features

- **Text Input & Processing**: Input multi-sentence text and automatically split into sentences
- **Graph Construction**: Build similarity graph between sentences based on word overlap
- **Centrality Scoring**: Calculate sentence importance using Dijkstra's shortest path algorithm
- **Automatic Summarization**: Generate summaries by selecting top-ranked sentences
- **Keyword Extraction**: Extract and rank important keywords from the text
- **Sentence Search**: Linear search functionality to find sentences containing specific keywords
- **Graph Visualization**: Display the adjacency list representation of the sentence graph
- **BFS Traversal**: Explore the graph using Breadth-First Search starting from any sentence
- **Statistics Display**: Show text statistics including sentence count, word count, and graph metrics
- **Stack-based Operations**: Find top sentence using stack data structure
- **Interactive Menu**: User-friendly console interface with numbered menu options

## Requirements

- Java Development Kit (JDK) 8 or higher
- No external dependencies required (uses only standard Java libraries)

## Installation & Compilation

1. Clone or download the project files
2. Navigate to the project directory
3. Compile the Java source files:

```bash
javac -d bin src/*.java src/*/*.java
```

4. Run the application:

```bash
java -cp bin Main
```

## Usage

1. Launch the application using the command above
2. Use the interactive menu to:
   - **Input Text**: Enter your text (end with empty line)
   - **Generate Summary**: Create automatic summary with top keywords and sentences
   - **Display Graph**: View the sentence similarity graph
   - **Search Sentences**: Find sentences containing specific keywords
   - **BFS Traversal**: Explore graph connectivity
   - **View Statistics**: Check text and graph metrics
   - **Find Top 1**: Get most important sentence using stack operations

### Example Workflow

```
1. Input Teks
   -> Enter your text paragraph here
   -> Press Enter on empty line to finish

2. Generate Ringkasan
   -> View top keywords
   -> See sentence rankings with centrality scores
   -> Get automatic summary (top 3 sentences)
```

## How It Works

### TextRank Algorithm Implementation

1. **Text Preprocessing**:
   - Split input text into individual sentences
   - Tokenize sentences into words
   - Remove stopwords (English and Indonesian)

2. **Graph Construction**:
   - Each sentence becomes a node
   - Edge weights represent sentence similarity (based on shared words)
   - Distance = 100 - (similarity_score × 10)

3. **Centrality Calculation**:
   - Use Dijkstra's algorithm from each node
   - Calculate total distance to all other nodes
   - Lower total distance = higher centrality (more important)

4. **Summary Generation**:
   - Rank sentences by centrality score
   - Select top N sentences (default: 3)
   - Reorder to maintain original sequence
   - Combine into coherent summary

### Data Structures Used

- **LinkedList**: Custom implementation for dynamic arrays
- **Graph**: Adjacency list representation
- **Priority Queue**: For Dijkstra's algorithm
- **Stack**: For navigation and top-1 finding operations
- **Queue**: For BFS traversal

## Project Structure

```
TextRank-Lite/
├── src/
│   ├── Main.java              # Main application with menu interface
│   ├── TextGraph.java         # Graph construction and operations
│   ├── Sentence.java          # Sentence representation
│   ├── Edge.java              # Graph edge representation
│   ├── Dijkstra.java          # Shortest path algorithm
│   ├── Searcher.java          # Linear search functionality
│   ├── Sorter.java            # Sorting algorithms (merge sort)
│   ├── NavigationStack.java   # Stack implementation
│   ├── Queue.java             # Queue implementation
│   ├── Linkedlist.java        # Custom linked list
│   ├── SentenceScore.java     # Sentence scoring
│   └── algorithm/             # Algorithm utilities
│   └── graph/                 # Graph utilities
│   └── model/                 # Data models
│   └── structures/            # Data structures
│   └── utils/                 # Utility functions
├── bin/                       # Compiled class files
├── lib/                       # External libraries (if any)
└── README.md                  # This file
```

## Algorithm Details

### Similarity Calculation
- Counts overlapping words between sentences
- Normalized by sentence length
- Used to determine edge weights in the graph

### Centrality Scoring
- Implements TextRank through graph centrality
- Uses Dijkstra's algorithm for efficiency
- Considers both direct and indirect connections

### Keyword Extraction
- Frequency-based ranking
- Stopword filtering (bilingual: English + Indonesian)
- Minimum word length filtering

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the MIT License.

## Academic Context

This implementation was developed as part of the Algorithm and Data Structure course project, demonstrating practical application of graph algorithms, data structures, and algorithmic thinking in natural language processing tasks.
