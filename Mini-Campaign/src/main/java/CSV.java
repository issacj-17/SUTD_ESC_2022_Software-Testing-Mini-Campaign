import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class CSV {

    public static class CSVParser {

        public static LinkedHashSet<LinkedHashMap> parseCSV(String filePath, String delimiter, boolean columnsPresent) throws IOException, CSVException {
            LinkedHashSet<LinkedHashMap> hashSet;

            if (columnsPresent){
                hashSet = parseCSVColumns(filePath, delimiter);
            } else {
                hashSet = parseCSVNoColumns(filePath, delimiter);
            }

//            System.out.println(hashSet);

            String fileName = Paths.get(filePath).getFileName().toString().replace(".csv", "");
            System.out.println(fileName + ": Parse Success!");

            return hashSet;
        }

        private static LinkedHashSet<LinkedHashMap> parseCSVColumns(String filePath, String delimiter) throws IOException, CSVException {
            String line;
            String[] columns = new String[0];

            int i = 0;

            LinkedHashSet<LinkedHashMap> hashSet = new LinkedHashSet<>();
            LinkedHashMap<String, String> hashMap;

            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            while ((line = reader.readLine()) != null) {

                if (i == 0) {
                    columns = line.split(delimiter);
                    validateColumns(columns);

//                    System.out.println(Arrays.toString(columns));
                }
                else {
                    String regex = delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    String[] row = line.split(regex);
                    hashMap = getLinkedHashMap(columns, row);

                    if (!hashSet.add(hashMap)) {
                        System.err.println("WARNING: Duplicate Rows Detected!");
                    }
                }

                i++;
            }

            reader.close();

            return hashSet;
        }

        private static LinkedHashSet<LinkedHashMap> parseCSVNoColumns(String filePath, String delimiter) throws IOException, CSVException {
            String line;
            String[] columns = new String[0];

            int i = 0;

            LinkedHashSet<LinkedHashMap> hashSet = new LinkedHashSet<>();
            LinkedHashMap<String, String> hashMap;

            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            while ((line = reader.readLine()) != null) {

                if (i == 0) {
                    columns = new String[line.split(delimiter).length];
                    Arrays.setAll(columns, j -> Integer.toString(j + 1));

//                System.out.println(Arrays.toString(columns));
                }
                String regex = delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                String[] row = line.split(regex);
                hashMap = getLinkedHashMap(columns, row);

                if (!hashSet.add(hashMap)) {
                    System.err.println("WARNING: Duplicate Rows Detected!");
                }

                i++;
            }

            reader.close();

            return hashSet;
        }

        private static LinkedHashMap<String, String> getLinkedHashMap(String[] columns, String[] row) throws CSVException {
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();

            if (row.length != columns.length) {
                System.err.println(Arrays.toString(columns) + "\tColumn Count: " + columns.length);
                System.err.println(Arrays.toString(row) + "\tEntry Count: " + row.length);

                throw new CSVException("Entry Count Does Not Match Column Count!");
            }

            for (int j = 0; j < row.length; j++) {
                String clean = row[j].strip();
//                    System.out.println(row[j] + ", " + clean);

                if (!row[j].equals(clean)) {
                    System.out.println("White Space Detected! Check Row: " + Arrays.toString(row) + " Entry: " + row[j]);
                    row[j] = row[j].strip();
                }

                hashMap.put(columns[j], row[j]);
            }

//            System.out.println(hashMap);

            return hashMap;
        }

        private static void validateColumns(String[] columns) throws CSVException {
            LinkedHashSet<String> set = new LinkedHashSet<>();

            for (int j = 0; j < columns.length; j++) {
                String clean = columns[j].strip();
//                    System.out.println(columns[j] + ", " + clean);

                if (!columns[j].equals(clean)) {
                    System.err.println("WARNING: White Space Detected! Check Column: " + columns[j]);
                    columns[j] = columns[j].strip();
                }

                if (columns[j] == "") {
                    throw new CSVException("Missing Column Detected");
                }

                if (!set.add(columns[j])) {
                    System.err.println(Arrays.toString(columns));
                    System.err.println(columns[j]);

                    throw new CSVException("Duplicate Column Names Detected!");
                }
            }
        }
    }

    public static class CSVCompare {

        public static ArrayList<LinkedHashSet> compare(LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2, String[] selectedKeys) throws CSVException {
            ArrayList<LinkedHashSet> result;

            if (selectedKeys.length == 0) {
                result = compareAll(set1, set2);
            }
            else {
                result = compareSelection(set1, set2, selectedKeys);
            }

            System.out.println("CSV Files: Compare Success!");

            return  result;
        }
        public static ArrayList<LinkedHashSet> compareAll(LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2) {
            ArrayList<LinkedHashSet> arrayList = new ArrayList<>();
            LinkedHashSet<LinkedHashMap> set3 = new LinkedHashSet<>();

            for (LinkedHashMap hashMap : set2) {

                if (set1.contains(hashMap)){
                    set1.remove(hashMap);
                }
                else {
                    set3.add(hashMap);
                }
            }

            arrayList.add(set1);
            arrayList.add(set3);

            return arrayList;
        }

        public static ArrayList<LinkedHashSet> compareSelection(LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2, String[] selectedKeys) throws CSVException {
            ArrayList<LinkedHashSet> arrayList = new ArrayList<>();

            LinkedHashSet<LinkedHashMap> modifiedSet1;
            LinkedHashSet<LinkedHashMap> modifiedSet2;
            LinkedHashSet<LinkedHashMap> modifiedSet3 = new LinkedHashSet<>();

            LinkedHashSet<LinkedHashMap> mismatchSet1;
            LinkedHashSet<LinkedHashMap> mismatchSet2;

            for (LinkedHashMap hashMap1 : set1) {
                for (LinkedHashMap hashMap2 : set2){
                    compareMap(hashMap1, hashMap2, selectedKeys);
                }
            }

            modifiedSet1 = filterMap(set1, selectedKeys);
            modifiedSet2 = filterMap(set2, selectedKeys);

            for (LinkedHashMap hashMap : modifiedSet2) {
                if (modifiedSet1.contains(hashMap)){
                    modifiedSet1.remove(hashMap);
                }
                else {
                    modifiedSet3.add(hashMap);
                }
            }

            mismatchSet1 = getMismatchMap(set1, modifiedSet1);
            mismatchSet2 = getMismatchMap(set2, modifiedSet3);

            arrayList.add(mismatchSet1);
            arrayList.add(mismatchSet2);

            return arrayList;
        }

        private static void compareMap(LinkedHashMap<String, String> hashMap1, LinkedHashMap<String, String> hashMap2, String[] selectedKeys) throws CSVException {
            boolean selection = true;

            for (String key : selectedKeys) {
                if (!hashMap1.containsKey(key) || !hashMap2.containsKey(key)) {
//                    System.out.println(hashMap1);
//                    System.out.println(hashMap1.containsKey(key));
//                    System.out.println(hashMap2);
//                    System.out.println(hashMap2.containsKey(key));
//                    System.out.println(key);

                    String message = String.format("Input Column: {%s} Does Not Exist in Both CSV Files", key);
                    throw new CSVException(message);
                }

                if (!hashMap1.get(key).equals(hashMap2.get(key))) {
                    selection = false;
                }
            }

            boolean warning = selection && !hashMap1.equals(hashMap2);
//            String message = String.format("warning: %b, selection %b, equivalent hashmap %b", warning, selection, !hashMap1.equals(hashMap2));
//            System.out.println(message);

            if (warning){
                System.err.println("WARNING: Check Your Column Selection - Row Mismatch but Values of Selected Columns Match. This will not be considered as a Mismatch.");
                System.err.println("Selected Columns: " + Arrays.toString(selectedKeys));
                System.err.println(hashMap1);
                System.err.println(hashMap2);
                System.err.println("Please Change Your Column Selection if you wish to consider the above as a Mismatch. \n");
            }
        }

        private static LinkedHashSet<LinkedHashMap> filterMap(LinkedHashSet<LinkedHashMap> set, String[] selectedKeys) {
            LinkedHashSet<LinkedHashMap> modifiedSet = new LinkedHashSet<>();
            LinkedHashMap<String, String> modifiedMap;

            for (LinkedHashMap<String, String> hashMap : set){
                modifiedMap = new LinkedHashMap<>();

                for (String key : selectedKeys) {
                    modifiedMap.put(key, hashMap.get(key));
                }

                modifiedSet.add(modifiedMap);
            }

//            System.out.println(modifiedSet);

            return modifiedSet;
        }

        private static LinkedHashSet<LinkedHashMap> getMismatchMap(LinkedHashSet<LinkedHashMap> originalSet, LinkedHashSet<LinkedHashMap> subset) {
            LinkedHashSet<LinkedHashMap> resultSet = new LinkedHashSet<>();

            for (LinkedHashMap hashMap : originalSet) {
                for (LinkedHashMap subsetHashMap : subset){
                    if (hashMap.entrySet().containsAll(subsetHashMap.entrySet())) {
                        resultSet.add(hashMap);
                    }
                }
            }

//            System.out.println(resultSet);

            return resultSet;
        }
    }

    public static class CSVWriter {
        public static void writeToCSV(ArrayList<LinkedHashSet> arrayList, String[] args) throws IOException {
            String file1 = Paths.get(args[0]).getFileName().toString().replace(".csv", "");
            String file2 = Paths.get(args[1]).getFileName().toString().replace(".csv", "");
            String path = String.format("compare_%s_%s.csv", file1, file2);

            LinkedHashSet<LinkedHashMap> set;
            String[] filePath = new String[]{file1, file2};
            String[] keys = null;

            FileWriter writer = new FileWriter(path, false);

            for (int i = 0; i < filePath.length; i++) {
                set = arrayList.get(i);
                writer.write(filePath[i]);
                writer.write("\r\n");

                for (LinkedHashMap hashMap : set){

                    if (keys == null) {
                        keys = (String[]) hashMap.keySet().toArray(new String[hashMap.size()]);
                        writeHelper(keys, writer);
                    }

                    String[] entries = (String[]) hashMap.values().toArray(new String[hashMap.size()]);
                    writeHelper(entries, writer);
                }

                writer.write("\r\n");
                keys = null;
            }

            System.out.println(path + ": Write Success!");
            writer.close();
        }

        private static void writeHelper(String[] values, FileWriter writer) throws IOException {
            for (int j = 0; j < values.length; j++) {
                String key = values[j];
                writer.write(key);

                if (j != values.length - 1) {
                    writer.write(",");
                }
            }

            writer.write("\r\n");
        }
    }

    public static class CSVException extends Exception {
        public CSVException(String str) {
            super(str);
        }
    }
}
