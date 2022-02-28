package searchengine;

import java.util.ArrayList;
import java.util.Collections;

/*
 * This class builds a hash table of words from movies descriptions. Each word maps to a set
 * of movies in which it occurs.
 * 
 * @author Haolin (Daniel) Jin
 * @author Ana Paula Centeno
 * 
 */ 
public class RUMDbSearchEngine {
    
    private int    hashSize;   // the hash table size
    private double threshold;  // load factor threshold. load factor = wordCount/hashSize
    private int    wordCount;  // the number of unique words in the table
    private WordOccurrence[] hashTable;  // the hash table

    private ArrayList<String> noiseWords; // noisewords are not to be inserted in the hash table

    /* 
     * Constructor initilizes the hash table.
     * 
     *  @param hashSize is the size for the hash table 
     *     @param threshold for the hash table load factor. Rehash occurs when the ratio 
     *     wordCount : hashSize exceeds the threshold.
     *  @param noiseWordsFile contains words that will not be inserted into the hash table.
     */
    public RUMDbSearchEngine (int hashSize, double threshold, String noiseWordsFile){

        this.hashSize   = hashSize;
        this.hashTable  = new WordOccurrence[hashSize];
        this.noiseWords = new ArrayList<String>();
        this.threshold  = threshold;
        this.wordCount  = 0;

        // Read noise words from file
        StdIn.setFile(noiseWordsFile);
        while ( !StdIn.isEmpty() ) {
            String word = StdIn.readString();
            if ( !noiseWords.contains(word) )
                noiseWords.add(word);
        }
    }

    /*
     * Method used to map a word into an array index.
     * 
     * @param word the word
     * @return array index within @hashTable
     */
    private int hashFunction ( String word ) {
        int hashCode = Math.abs(word.toLowerCase().replaceAll("/[^a-z0-9]/","").hashCode());
        return hashCode % hashSize;
    }

    /*
     * Returns the hash table load factor
     * @return the load factor
     */ 
    public double getLoadFactor () {
        return (double)wordCount/hashSize;
    }

    /*
     * This method reads movies title and description from the input file.
     * 
     * @param inputFile the file to be read containg movie's titles and descriptions.
     * 
     * The inputFile format:
     *         Each line describes a movie's title, and a short description on the movie.
     *         title| word1 word2 word3;
     * 
     *      Note that title can have multiple words, there is no space between the last 
     *      word on the tile and '|'
     *         No duplicate movie name accepted.
     * 
     * @return ArrayList of ArrayList of Strings, each inner ArrayList refers to a movie, 
     *         the first index contains the title, the remaining indices contain the movie's
     *         description words (one word per index). 
     * 
     *  Example: 
     *         [
     *             [full title1][word1][word2]
     *             [full title2][word1]
     *             [full title3][word1][word2][word3][word4]
     *         ]
     */
    public ArrayList<ArrayList<String>> readInputFile ( String inputFile ) {

        ArrayList<ArrayList<String>> allMovies = new ArrayList<ArrayList<String>>();
        StdIn.setFile(inputFile);
        
        String[] read = StdIn.readAllStrings();
        
        for ( int i = 0; i < read.length; i++ ) {
            ArrayList<String> movie = new ArrayList<String>();
            String t = "";
            do {
                t += " "+read[i];
            } while ( read[i++].indexOf('|') == -1 );
            
            movie.add(t.substring(1,t.length()-1).toLowerCase().replaceAll("/[^a-z0-9]/",""));

            while ( i < read.length ) {
                if ( read[i].indexOf(';') != -1 ) {
                    movie.add(read[i].substring(0,  read[i].indexOf(';')));
                    break;
                }
                movie.add(read[i].toLowerCase().replaceAll("/[^a-z0-9]/","") );
                i++;
            }
            allMovies.add(movie);
        }
        return allMovies;
    }
    
    /* 
     * This method calls readInputFile and uses its output to load the movies and their
     * descriptions words into the hashTable.
     * 
     * Use the result from readInputFile() to insert each word and its location
     * into the hash table.
     * 
     * Use isWord() to discard noise words, remove trailing punctuation, and to transform
     * the word into all lowercase character.
     * 
     * Use insertWordLocation() to insert each word into the hash table.
     * 
     * Use insertWordLocation() to insert the word into the hash table.
     * 
     * @param inputFile the file to be read containg movie's titles and descriptions
     * 
     */
    public void insertMoviesIntoHashTable ( String inputFile ) {

        // COMPLETE THIS METHOD
        ArrayList<ArrayList<String>> movies = readInputFile(inputFile);
       
        for (ArrayList<String> movie : movies) 
        
        {
            String title = movie.get(0);

            for (int i = 1; i < movie.size(); i++) 
            
            {
                String word = movie.get(i);

                word = isWord(word);

                if (word != null) 
                
                {
                    Location location = new Location(title, i);

                    insertWordLocation(word, location);
                }
            }
        }
    }

    /**
     * Given a word, returns it as a word if it is any word that, after being stripped of any
     * trailing punctuation, consists only of alphabetic letters and digits, and is not
     * a noise word. All words are treated in a case-INsensitive manner.
     * 
     * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
     * 
     * @param word Candidate word
     * @return word (word without trailing punctuation, LOWER CASE)
     */
    private String isWord ( String word ) {
        int p = 0;
        char ch = word.charAt(word.length()-(p+1));
        while (ch == '.' || ch == ',' || ch == '?' || ch == ':' || ch == ';' || ch == '!') {
            p++;
            if ( p == word.length() ) {
                // the entire word is punctuation
                return null;
            }
            int index = word.length()-(p+1);
            if (index == -1) {
                System.out.flush();
            }
            ch = word.charAt(word.length()-(p+1));    
        }
        
        word = word.substring(0,word.length()-p);

        // are all characters alphabetic letters?
        for (int i=0; i < word.length(); i++) {
            if (!Character.isLetterOrDigit(word.charAt(i))) {
                return null;
            }
        }
        word = word.toLowerCase();
        if (noiseWords.contains(word)) {
            return null;
        }
        return word;
    }

    /*
     * Prints the entire hash table
     */
    public void print () {

        for ( int i = 0; i < hashTable.length; i++ ) {
            
            StdOut.printf("[%d]->", i);
            for ( WordOccurrence ptr = hashTable[i]; ptr != null; ptr = ptr.next ) {

                StdOut.print(ptr.toString());
                if ( ptr.next != null ) {
                    StdOut.print("->");
                }
            }
            StdOut.println();
        }
    }

    /*
     * This method inserts a Location object @loc into the matching WordOccurrence object
     * in the hash table. If the word is not present into the hash table, add a new 
     * WordOccurrence object into hash table. 
     *         
     * @param word to be inserted
     * @param loc the word's position within the description.
     */
    public void insertWordLocation (String word, Location loc) {


        WordOccurrence tim = getWordOccurrence(word);
        
        if (tim != null) {

            tim.addOccurrence(loc);

        }
        else {

            tim = new WordOccurrence(word);

            tim.addOccurrence(loc);
            
            int pos = hashFunction(word);
            
            if (hashTable[pos] == null) {

                hashTable[pos] = tim;

            }

            else {
                WordOccurrence next = hashTable[pos];

                hashTable[pos] = tim;

                tim.next = next;
            }
            
            wordCount++;
            
            if (getLoadFactor() > threshold) {

                rehash(hashSize * 2);

            }
        }
    }


    /*
     * Rehash the hash table to newHashSize. Rehash happens when the load factor is
     * greater than the @threshold (load factor = wordCount/hashSize).
     * 
     * @param newHashSize is the new hash size
     */
    private void rehash (int newHashSize){


        WordOccurrence[] oldHashTable = hashTable;

        int oldSize = hashSize;
        
        hashTable = new WordOccurrence[newHashSize];

        hashSize = newHashSize;

        wordCount = 0;
        
        for (int i = 0; i < oldSize; i++) {

            for (WordOccurrence tim = oldHashTable[i]; tim != null; tim = tim.next) {

                String word = tim.getWord();

                for (Location location : tim.getLocations()) {

                    insertWordLocation(word, location);
                    
                } 
            }
        }
    }

    /* 
     * Find the WordOccurrence object with the target word in the hash table
     * @param word search target
     * @return @word WordOccurrence object
     */
    public WordOccurrence getWordOccurrence (String word) {

        
       WordOccurrence tim;

        int pos = hashFunction(word);
        
        for (tim = hashTable[pos]; tim != null; tim = tim.next) {

            String s = tim.getWord();

            if (s.equals(word)) {

                return tim;

            }
        }
        
        return null;
    }
    
    /*
     * Finds all occurrences of wordA and wordB in the hash table, and add them to an 
     * ArrayList of MovieSearchResult based on titles.
     *         (no need to calculate distance here)
     * 
     * @param wordA is the first queried word
     * @param wordB is the second queried word
     * @return ArrayList of MovieSearchResult objects.
     */
    public ArrayList<MovieSearchResult> createMovieSearchResult (String wordA, String wordB) {

        // COMPLETE THIS METHOD
               
        ArrayList<MovieSearchResult> result = new ArrayList<MovieSearchResult>();

        WordOccurrence tim1 = getWordOccurrence(wordA);

        WordOccurrence tim2 = getWordOccurrence(wordB);
        
        if (tim1 == null || tim2 == null) {

            return null;
        }
        
        ArrayList<Location> Alocats = tim1.getLocations();

        ArrayList<Location> Blocats = tim2.getLocations();

        ArrayList<String> commonTitles = new ArrayList<String>();
        
        for (Location Alocat : Alocats) {

            String title1 = Alocat.getTitle();

            if (commonTitles.indexOf(title1) == -1) {

                commonTitles.add(title1);

            }
        }
        
        for (Location Blocat : Blocats) {

            String title2 = Blocat.getTitle();

            if (commonTitles.indexOf(title2) == -1) {

                commonTitles.add(title2);

            }
        }
            
        for (String title : commonTitles) {

            MovieSearchResult cur = new MovieSearchResult(title);

            for (Location Alocat : Alocats) {

                String title1 = Alocat.getTitle();


                if (title1.equals(title)) {

                    cur.addOccurrenceA(Alocat.getPosition());

                }
            }

            for (Location Blocat : Blocats) {

                String title2 = Blocat.getTitle();

                if (title2.equals(title)) {

                    cur.addOccurrenceB(Blocat.getPosition());

                }
            }
            
            result.add(cur);
        }
        
        return result;
    }


    /*
     * 
     * Computes the minimum distance between the two wordA and wordB in @msr.
     * In another words, this method computes how close these two words appear 
     * in the description of the movie (MovieSearchResult refers to one movie).
     * 
     * If the movie's description doesn't contain one, or both words set distance to -1;
     * 
     *  NOTE: the ArrayLists for A and B will always be in order since the words were added in order.
     *         
     * The shortest distance between two words can be found by keeping track of the index 
     * of previous wordA and wordB, then find the next location of either word and calculate 
     * the distance between the word and the previous location of the other word.
     * 
     * For example:
     *         wordA locations: 1 3 5 11
     *         wordB locations: 4 10 12 
     *         start previousA as 1, and previousB as 4, calculate distance as abs(1-4) = 3
     *         because 1<4,     update previousA to 3,     abs(4-3)   = 1 , smallest so far
     *         because 3<4,     update previousA to 5,     abs(5-4)   = 1 
     *         because 5>4,     update previousB to 10, abs(5-10)  = 5
     *         because 5<10,     update previousA to 11, abs(11-10) = 1
     *         End because all elements from A have been used.
     *             
     * @param msr the MovieSearchResult object to be updated with the minimum distance between its 
     * words.
     */
    public void calculateMinDistance(MovieSearchResult msr){


        int minDistance = -1;

        ArrayList<Integer> wordAlocats = msr.getArrayListA();

        ArrayList<Integer> wordBlocats = msr.getArrayListB();

        int l1 = 0;

        int l2 = 0;

        
        if (wordAlocats.size() == 0 || wordBlocats.size() == 0) {

            msr.setMinDistance(minDistance);

            return;

        }
        
        while (l1 < wordAlocats.size() && l2 < wordBlocats.size()) {

            if (minDistance == -1) {

                if (wordAlocats.get(l1) < wordBlocats.get(l2)) {

                    minDistance = wordBlocats.get(l2) - wordAlocats.get(l1);

                    l1++;

                }

                else {

                    minDistance = wordAlocats.get(l1) - wordBlocats.get(l2);
                    l2++;

                }
            }

            else {

                int curDistance = 0;

                if (wordAlocats.get(l1) < wordBlocats.get(l2)) {

                    curDistance = wordBlocats.get(l2) - wordAlocats.get(l1);

                    l1++;

                }
                else {

                    curDistance = wordAlocats.get(l1) - wordBlocats.get(l2);

                    l2++;
                }

                if (curDistance < minDistance) {

                    minDistance = curDistance;
                }
            }
        }
        
        while (l1 < wordAlocats.size()) {
            int curDistance = 0;
            
            if (wordAlocats.get(l1) < wordBlocats.get(l2 - 1)) {

                curDistance = wordBlocats.get(l2) - wordAlocats.get(l1);

            }
            else {

                curDistance = wordAlocats.get(l1) - wordBlocats.get(l2 - 1);

            }
            if (curDistance < minDistance) {

                minDistance = curDistance;

            }

            l1++;
        }
        
        while (l2 < wordBlocats.size()) {

            int curDistance = 0;

            if (wordAlocats.get(l1 - 1) < wordBlocats.get(l2)) {

                curDistance = wordBlocats.get(l2) - wordAlocats.get(l1 - 1);

            }
            else {

                curDistance = wordAlocats.get(l1 - 1) - wordBlocats.get(l2);

            }
            if (curDistance < minDistance) {

                minDistance = curDistance;

            }

            l2++;
        }
        
        msr.setMinDistance(minDistance);
    }


    /*
     * This method's purpose is to search the movie database to find movies that 
     * contain two words (wordA and wordB) in their description.
     * 
     * @param wordA the first word to search
     * @param wordB the second word to search
     * 
     * @return ArrayList of MovieSearchResult, with length <= 10. Each
     * MovieSearchResult object returned must have a non -1 distance (meaning that
     * both words appear in the description). The ArrayList is expected to be 
     * sorted from the smallest distance to the greatest.
     *         
     *     NOTE: feel free to use Collections.sort( arrayListOfMovieSearchResult ); to sort.
     */
    public ArrayList<MovieSearchResult> topTenSearch(String wordA, String wordB){

        
        ArrayList<MovieSearchResult> result = new ArrayList<MovieSearchResult>();

        ArrayList<MovieSearchResult> tmp = new ArrayList<MovieSearchResult>();
        
        WordOccurrence tim1 = getWordOccurrence(wordA);

        WordOccurrence tim2 = getWordOccurrence(wordB);
        


        if (tim1 == null || tim2 == null) {

            return null;

        }
        
        ArrayList<Location> Alocats = tim1.getLocations();

        ArrayList<Location> Blocats = tim2.getLocations();

        ArrayList<String> commonTitles = new ArrayList<String>();

        
        for (Location Alocat : Alocats) {

            String title1 = Alocat.getTitle();

            for (Location Blocat : Blocats) {

                String title2 = Blocat.getTitle();

                if (title1.equals(title2)) {

                    if (commonTitles.indexOf(title1) == -1) {

                        commonTitles.add(title1);

                    }
                }
            }
        }
        
        for (String title : commonTitles) {

            MovieSearchResult cur = new MovieSearchResult(title);

            for (Location Alocat : Alocats) {

                String title1 = Alocat.getTitle();

                if (title1.equals(title)) {

                    cur.addOccurrenceA(Alocat.getPosition());

                }
            }

            for (Location Blocat : Blocats) {

                String title2 = Blocat.getTitle();

                if (title2.equals(title)) {

                    cur.addOccurrenceB(Blocat.getPosition());

                }
            }
            
            tmp.add(cur);

        }
        
        for (MovieSearchResult title : tmp) {

            calculateMinDistance(title);

        }
        java.util.Collections.sort(tmp);

        int size = 10;

        if (tmp.size() < size) {

            size = tmp.size();

        }
        for (int i = 0; i < size; i++) {

            result.add(tmp.get(i));

        }
        
        return result;

    }
}
