import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    private static final long numberOfWords = 5L;

    private static Map<Integer,String> createMappings() {

        Map<Integer,String> mappings = new HashMap<>();
        mappings.put(2,"aąbcć");
        mappings.put(3,"deęf");
        mappings.put(4,"ghi");
        mappings.put(5,"jklł");
        mappings.put(6,"mnńoó");
        mappings.put(7,"prsś");
        mappings.put(8,"tu");
        mappings.put(9,"wyzźż");

        return Collections.unmodifiableMap(mappings);
    }

    public List<String> readFile() {

        Path path = Paths.get(getClass().getResource("slowa.txt").getPath());
        List<String> dictionary = new ArrayList<>();
        try {
            dictionary = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(dictionary);
    }

    private String decodeDigits(String digit) {

        if (!digit.matches("^[2-9]")) {
            throw new IllegalArgumentException("You have to pass a number from the range 2-9");
        }
        else {
            Map<Integer,String> mappings = Solution.createMappings();
            int key = Integer.parseInt(digit);
            return mappings.get(key);
        }
    }

    private List<String> getPossibleWordsFromDictionary(List<String> dictionary, String digits) {

        if (dictionary.isEmpty()) {
            throw new IllegalArgumentException("Given list cannot be empty");
        }

        int wordLength = digits.length();

        String firstDigit = digits.substring(0,1);
        String lastDigit = digits.substring(wordLength-1,wordLength);

        String firstLetters = decodeDigits(firstDigit);
        String lastLetters = decodeDigits(lastDigit);

        List<String> resultList = new ArrayList<>();
        List<String> temp = dictionary.stream().filter(w->w.length() == wordLength).collect(Collectors.toList());

        for (String firstLetter : firstLetters.split("")) {
            for (String lastLetter : lastLetters.split("")) {
                resultList.addAll(temp.stream().filter(w->w.startsWith(firstLetter)).filter(w->w.endsWith(lastLetter)).collect(Collectors.toList()));
            }
        }
        return resultList;
    }

    private List<String> generateLetterCombinations(String digits) {

        List<String> result = new ArrayList<>();
        List<String> temp = new ArrayList<>();

        for (String digit : digits.split("")) {

            String letters = decodeDigits(digit);

            if(result.isEmpty()) {
                result.addAll(Arrays.asList(letters.split("")));
                continue;
            }
            else {
                for (String letter1 : result) {
                    for (String letter2 : letters.split("")) {
                        temp.add(letter1.concat(letter2));
                    }
                }
            }
            result.clear();
            result.addAll(temp);
            temp.clear();
        }
        return result;
    }

    private List<String> createResult(List<String> possibleWords, List<String> letterCombinations) {

        if (possibleWords.isEmpty() || letterCombinations.isEmpty()) {
            throw new IllegalArgumentException("Given lists cannot be empty");
        }
        else {
            List<String> result = new ArrayList<>();
            for (String combination : letterCombinations) {
                possibleWords.stream().filter(w -> w.equals(combination)).forEach(w -> result.add(w));
            }
            return result;
        }
    }

    public void generateSolution(List<String> dictionary, String digits) {

        List<String> possibleWords = getPossibleWordsFromDictionary(dictionary,digits);
        List<String> letterCombinations = generateLetterCombinations(digits);
        List<String> result = createResult(possibleWords,letterCombinations);

        System.out.println("Number of all matched words: " + result.size());
        System.out.println("Examples: ");
        if (result.size() > numberOfWords) {
            System.out.println(result.stream().limit(numberOfWords).collect(Collectors.toList()));
        }
        else System.out.println(result);
    }

    public static void main(String[] args) {

        Solution solution = new Solution();

        List<String> dictionary = solution.readFile();
        solution.generateSolution(dictionary,"28725");
    }
}
