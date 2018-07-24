import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        String filename = "1342.txt";

        int totalWords = getTotalNumberOfWords(filename);
        System.out.println(totalWords);

        int uniqueWords = getTotalUniqueWords(filename);
        System.out.println(uniqueWords);

        LinkedList<Entry> topFrequency = get20MostFrequentWords(filename);
        int count = 0;
        for(Entry e: topFrequency) {
            System.out.println(++count + ". " + e.getKey() + " " + e.getValue());
        }

        LinkedList<Entry> topInterestingFrequency = get20MostInterestingFrequentWords(filename);
        count = 0;
        for(Entry e: topInterestingFrequency) {
            System.out.println(++count + ". " + e.getKey() + " " + e.getValue());
        }

        LinkedList<Entry> leaseFrequency = get20LeastFrequentWords(filename);
        count = 0;
        for(Entry e: leaseFrequency) {
            System.out.println(++count + ". " + e.getKey() + " " + e.getValue());
        }

        int[] frequency = getFrequencyOfWord("handsome", filename);
        String frequent = "[" + Integer.toString(frequency[0]);
        for(int i = 1; i < frequency.length; i++) {
            frequent = frequent + ", " + Integer.toString(frequency[i]);
        }
        frequent += "]";
        System.out.println(frequent);

        String sentence = "She had no fear of its spreading farther through his means.";
        int chapter = getChapterQuoteAppears(sentence, filename);
        System.out.println(chapter);

        String generate = generateSentence(filename);
        System.out.println(generate);
    }
    public static int getTotalNumberOfWords(String fileName) {
        int totalNumber = 0;
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                scanner.next();
                totalNumber++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return totalNumber;
    }

    public static int getTotalUniqueWords(String fileName) {
        Set<String> wordSet = new HashSet<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                String word = scanner.next();
                word = word.replaceAll("[^a-zA-Z]","").toLowerCase();
                if(wordSet.contains(word)) {
                    continue;
                } else {
                    wordSet.add(word);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return wordSet.size();
    }

    public static LinkedList<Entry> get20MostFrequentWords(String fileName) {
        LinkedList<Entry> list = new LinkedList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            Map<String, Integer> wordSet = new HashMap<>();
            PriorityQueue<Entry> pq = new PriorityQueue<>(new Comparator<Entry>() {
                public int compare(Entry o1, Entry o2) {
                    return o2.getValue() - o1.getValue();
                }
            });

            while (scanner.hasNext()) {
                String word = scanner.next();
                word = word.replaceAll("[^a-zA-Z]","").toLowerCase();
                wordSet.put(word, wordSet.getOrDefault(word, 0) + 1);
            }

            for(String key: wordSet.keySet()) {
                pq.add(new Entry(key, wordSet.get(key)));
            }

            // Get top 20 words and store in the list
            for(int i = 0; i < 20; i++) {
                list.add(pq.poll());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static LinkedList<Entry> get20MostInterestingFrequentWords(String fileName) {
        LinkedList<Entry> list = new LinkedList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            Map<String, Integer> wordSet = new HashMap<>();
            PriorityQueue<Entry> pq = new PriorityQueue<>(new Comparator<Entry>() {
                @Override
                public int compare(Entry o1, Entry o2) {
                    return o2.getValue() - o1.getValue();
                }
            });

            // Read file and store words and frequencies in a hash map
            while (scanner.hasNext()) {
                String word = scanner.next();
                word = word.replaceAll("[^a-zA-Z]","").toLowerCase();
                wordSet.put(word, wordSet.getOrDefault(word, 0) + 1);
            }

            // Put all words and their frequencies into a priority queue sorted by frequencies
            for (String s : wordSet.keySet()) {
                pq.add(new Entry(s, wordSet.get(s)));
            }

            Scanner scanner2 = new Scanner(new File("1-1000.txt"));

            // Read first 100 frequent words and store in the hash set
            Set<String> frequentWord = new HashSet<>();
            for (int i = 0; i < 100; i++) {
                String word = scanner2.nextLine();
                frequentWord.add(word.toLowerCase());
            }

            // Find top 20 which are not frequent words and store in the list
            for (int i = 0; i < 20; ) {
                if (!frequentWord.contains(pq.peek().getKey())) {
                    list.add(pq.poll());
                    i++;
                } else {
                    pq.poll();
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static LinkedList<Entry> get20LeastFrequentWords(String fileName) {
        LinkedList<Entry> list = new LinkedList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            Map<String, Integer> wordSet = new HashMap<>();
            PriorityQueue<Entry> pq = new PriorityQueue<>(new Comparator<Entry>() {
                @Override
                public int compare(Entry o1, Entry o2) {
                    return o1.getValue() - o2.getValue();
                }
            });

            while (scanner.hasNext()) {
                String word = scanner.next();
                word = word.replaceAll("[^a-zA-Z]","").toLowerCase();
                wordSet.put(word, wordSet.getOrDefault(word, 0) + 1);
            }

            for(String key: wordSet.keySet()) {
                pq.add(new Entry(key, wordSet.get(key)));
            }

            // Get least 20 words and store in the list
            for(int i = 0; i < 20; i++) {
                list.add(pq.poll());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static int[] getFrequencyOfWord(String s, String fileName) {
        int[] frequent = new int[1];
        try {
            String content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
            String[] chapters = content.split("Chapter ");
            frequent = new int[chapters.length - 1];
            for (int i = 1; i < chapters.length; i++) {
                String[] words = chapters[i].split(" ");
                int count = 0;
                for (int j = 0; j < words.length; j++) {
                    if (words[j].replaceAll("[^a-zA-Z]", "").toLowerCase().equals(s.toLowerCase())) {
                        count++;
                    }
                }
                frequent[i - 1] = count;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return frequent;
    }

    public static int getChapterQuoteAppears(String sentence, String fileName) {
        try {
            String content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
            String[] chapters = content.split("Chapter ");
            int size = sentence.length();
            for (int i = 1; i < chapters.length; i++) {
                if(chapters[i].contains(sentence)) {
                    return i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String generateSentence(String fileName) {
        String res = "The";
        String pre = "The";
        try {
            for(int i = 1; i < 20; i++) {
                Scanner scanner = new Scanner(new File(fileName));
                Set<String> possible = new HashSet<>();
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    if (word.replaceAll("[^a-zA-Z]", "").toLowerCase().equals(pre.toLowerCase())) {
                        String next = scanner.next().replaceAll("[^a-zA-Z]", "").toLowerCase();
                        possible.add(next);
                    }
                }
                Random rand = new Random();
                int index = -1;
                if(!possible.isEmpty()) {
                    index = rand.nextInt(possible.size());
                }
                int r = 0;
                for (String s : possible) {
                    if (r == index) {
                        pre = s;
                        break;
                    }
                    r++;
                }
                res = res + " " + pre;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        res += ".";
        return res;
    }

}
// Entry class for storing words and frequencies with comparator
class Entry {
    private String key;
    private int value;

    public Entry(String key, int value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() {
        return key;
    }
    public int getValue() {
        return value;
    }
}
