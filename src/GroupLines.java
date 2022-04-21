import java.io.*;
import java.util.*;

public class GroupLines {
    public static void groupToSetsFromCsv (String inputPath, String outputPath, int columns) throws IOException {
        List<String[]> lines = readFromCsv(inputPath, columns);
        List<List<String[]>> findedSets = findSets(lines);
        System.out.println("Найдено непересекающихся множеств:  " + findedSets.size());
        findedSets.sort((o1, o2) -> o2.size() - o1.size());
        System.out.println("Найдено непересекающихся множеств в которых количество групп больше одной:  " + countOfSets(findedSets));
        System.out.println("Запись в файл csv...");
        writeToCsv(findedSets, outputPath);
        System.out.println("Запись в файл csv окончена");
    }

    private static List<String[]> readFromCsv (String inputPath, int columns) throws IOException {
        System.out.println("Загрузка из файла csv...");
        List<String[]> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(inputPath));
        String line;
        Set<String> linesHash = new HashSet<>();
        while ((line = reader.readLine()) != null){
            String[] words = line.split(";", -1);
            if (words.length == columns && linesHash.add(line))
                lines.add(words);
        }
        System.out.println("Загрузка из файла csv окончена");
        System.out.println(Arrays.toString(new String[]{"Загружено: " + linesHash.size() + " уникальных строк."}));
        return lines;
    }

    private static void writeToCsv (List<List<String[]>> groups, String outputPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath), 9999999);
        int i = 0;
        StringBuilder groupNameLine;
        StringBuilder lineOfWords;
        for (List<String[]> group : groups){
            i++;
            groupNameLine = new StringBuilder("\"Группа " + i + "\"");
            groupNameLine.append(";\"\"".repeat(Math.max(0, group.get(0).length - 1)));
            writer.write(groupNameLine.toString());
            writer.newLine();
            for(String[] words : group){
                lineOfWords = new StringBuilder(words[0]);
                for (int j = 1; j < words.length; j++)
                    lineOfWords.append(";").append(words[j]);
                writer.write(lineOfWords.toString());
                writer.newLine();
            }
        }
        writer.close();
    }

    private static int countOfSets (List<List<String[]>> findedSets){
        int count = 0;
        for(List<String[]> set : findedSets){
            if (set.size()<=1)
                break;
            count++;
        }
        return count;
    }

    private static class NewWord {
        public String value;
        public int position;

        public NewWord(String value, int position) {
            this.value = value;
            this.position = position;
        }
    }

    private static List<List<String[]>> findSets(List<String[]> lines) {
        List<Map<String, Integer>> wordsInSets = new ArrayList<>();
        List<List<String[]>> findedSets = new ArrayList<>();
        Map<Integer, Integer> mergedSets = new HashMap<>();
        for (String[] words : lines) {
            TreeSet<Integer> foundInSets = new TreeSet<>();
            List<NewWord> newWords = new ArrayList<>();
            for (int i = 0; i < words.length; i++) {
                String word = words[i];

                if (wordsInSets.size() == i)
                    wordsInSets.add(new HashMap<>());

                if (word.equals("\"\"") || word.equals(""))
                    continue;

                Integer wordSetNumber = wordsInSets.get(i).get(word);
                if (wordSetNumber != null) {
                    while (mergedSets.containsKey(wordSetNumber))
                        wordSetNumber = mergedSets.get(wordSetNumber);
                    foundInSets.add(wordSetNumber);
                }
                else {
                    newWords.add(new NewWord(word, i));
                }
            }
            int setNumber;
            if (foundInSets.isEmpty()) {
                setNumber = findedSets.size();
                findedSets.add(new ArrayList<>());
            }
            else
                setNumber = foundInSets.first();

            for (NewWord newWord : newWords) {
                wordsInSets.get(newWord.position).put(newWord.value, setNumber);
            }
            for (int mergeSetNumber : foundInSets) {
                if (mergeSetNumber != setNumber) {
                    mergedSets.put(mergeSetNumber, setNumber);
                    findedSets.get(setNumber).addAll(findedSets.get(mergeSetNumber));
                    findedSets.set(mergeSetNumber, null);
                }
            }
            findedSets.get(setNumber).add(words);
        }
        findedSets.removeAll(Collections.singleton(null));
        return findedSets;
    }


}



