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
                    String regex = delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    columns = line.split(regex, -1);
                    validateColumns(columns);

//                    System.out.println(Arrays.toString(columns));
                }
                else {
                    String regex = delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    String[] row = line.split(regex, -1);

//                    System.out.println(Arrays.toString(row));

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
                    String regex = delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    columns = new String[line.split(regex, -1).length];
                    Arrays.setAll(columns, j -> Integer.toString(j + 1));

//                System.out.println(Arrays.toString(columns));
                }
                String regex = delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                String[] row = line.split(regex, -1);

//                System.out.println(Arrays.toString(row));

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

                if (Objects.equals(columns[j], "")) {
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
                checkColumns(set1, set2);
                result = compareAll(set1, set2);
            }
            else {
                String[] selectedColumns = formatSelectedColumns(set1, set2, selectedKeys);
                result = compareSelection(set1, set2, selectedColumns);
            }

            System.out.println("CSV Files: Compare Success!");

            return  result;
        }

        private static void checkColumns(LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2) throws CSVException {
            validate: {
                for (LinkedHashMap hashMap1 : set1) {
                    for (LinkedHashMap hashMap2 : set2) {
                        if (hashMap1.keySet().equals(hashMap2.keySet())) {
                            break validate;
                        }
                        else {
                            Set<String> keys1 = hashMap1.keySet();
                            Set<String> keys2 = hashMap2.keySet();

                            Set<String> intersect = new LinkedHashSet<>(keys1);
                            Set<String> missing = new LinkedHashSet<>();

//                            System.err.println(keys1);
//                            System.err.println(keys2);

                            intersect.retainAll(keys2);
//                            System.err.println(intersect);

                            keys1.removeAll(intersect);
                            keys2.removeAll(intersect);

//                            System.err.println(keys1);
//                            System.err.println(keys2);

                            missing.addAll(keys1);
                            missing.addAll(keys2);

                            String message = String.format("Input Column(s): %s Not in Both CSV Files", missing);

                            throw new CSVException(message);
                        }
                    }
                }
            }
        }

        private static ArrayList<LinkedHashSet> compareAll(LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2) {
            ArrayList<LinkedHashSet> arrayList = new ArrayList<>();
            LinkedHashSet<LinkedHashMap> set3 = new LinkedHashSet<>();

            for (LinkedHashMap hashMap2 : set2) {
                if (set1.contains(hashMap2)){
                    set1.remove(hashMap2);
                }
                else {
                    set3.add(hashMap2);
                }
            }

            arrayList.add(set1);
            arrayList.add(set3);

            return arrayList;
        }

        private static String[] formatSelectedColumns (LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2, String[] selectedKeys) throws CSVException {
            ArrayList<String> keys1 = new ArrayList<>();
            ArrayList<String> keys2 = new ArrayList<>();

//            System.out.println(Arrays.toString(selectedKeys));

            for (LinkedHashMap hashMap1 : set1) {
                keys1 = new ArrayList<>(hashMap1.keySet());
                break;
            }

            for (LinkedHashMap hashMap2 : set2) {
                keys2 = new ArrayList<>(hashMap2.keySet());
                break;
            }


            for (int i = 0; i < selectedKeys.length; i++) {
                String original = selectedKeys[i];
                String formatted = "\"" + selectedKeys[i] + "\"";

                if (keys1.contains(original) && keys2.contains(original)) {
                    selectedKeys[i] = original;
                }
                else if (keys1.contains(formatted) && keys2.contains(formatted)) {
                    selectedKeys[i] = formatted;
                }
                else {
                    String note = "Note: Please ensure that the Column Name Exists in Both Files and in the Same Format.";
                    String message = String.format("Input Column: {%s} Does Not Exist in Both CSV Files!\n%s", original, note);
                    throw new CSVException(message);
                }
            }

//            System.out.println(Arrays.toString(selectedKeys));

            return selectedKeys;
        }
        private static ArrayList<LinkedHashSet> compareSelection(LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2, String[] selectedKeys) {
            ArrayList<LinkedHashSet> arrayList = new ArrayList<>();

            LinkedHashSet<LinkedHashMap> modifiedSet1;
            LinkedHashSet<LinkedHashMap> modifiedSet2;
            LinkedHashSet<LinkedHashMap> modifiedSet3 = new LinkedHashSet<>();

            LinkedHashSet<LinkedHashMap> mismatchSet1;
            LinkedHashSet<LinkedHashMap> mismatchSet2;

            validate: {
                for (LinkedHashMap<String, String> hashMap1 : set1) {
                    for (LinkedHashMap<String, String> hashMap2 : set2){
                        // bottleneck 1 - CRITICAL BOTTLENECK
                        boolean err = checkRows(hashMap1, hashMap2, selectedKeys);

                        if (err){
                            break validate;
                        }
                    }
                }
            }

            // bottleneck 3
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

            // bottleneck 2
            mismatchSet1 = getMismatchMap(set1, modifiedSet1);
            mismatchSet2 = getMismatchMap(set2, modifiedSet3);

            arrayList.add(mismatchSet1);
            arrayList.add(mismatchSet2);

            return arrayList;
        }

        private static boolean checkRows(LinkedHashMap<String, String> hashMap1, LinkedHashMap<String, String> hashMap2, String[] selectedKeys) {
            boolean selection = true;

            for (String key : selectedKeys) {
                if (!hashMap1.get(key).equals(hashMap2.get(key))) {
                    selection = false;
                    break;
                }
            }

            boolean warning = selection && !hashMap1.equals(hashMap2);
//            String message = String.format("warning: %b, selection %b, equivalent hashmap %b", warning, selection, !hashMap1.equals(hashMap2));
//            System.out.println(message);

            if (warning){
                System.err.println("WARNING: Check Your Column Selection - Row Mismatch but Values of Selected Columns Match.");
                System.err.println("This will not be considered as a Mismatch.");
                System.err.println("Selected Columns: " + Arrays.toString(selectedKeys));
                System.err.println("\nCase Example:");
                System.err.println(hashMap1);
                System.err.println(hashMap2);
                System.err.println("Please Change Your Column Selection if you wish to check whether cases such as the above are considered as a Mismatch. \n");

                return true;
            }

            return false;
        }

        private static LinkedHashSet<LinkedHashMap> filterMap(LinkedHashSet<LinkedHashMap> set, String[] selectedKeys) {
            LinkedHashSet<LinkedHashMap> modifiedSet = new LinkedHashSet<>();
            LinkedHashMap<String, String> modifiedMap;

            for (LinkedHashMap<String, String> hashMap : set){
                modifiedMap = new LinkedHashMap<>();

                for (String key : selectedKeys) {
                    modifiedMap.put(key, hashMap.get(key));
                }

//                System.out.println(modifiedMap);

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
        public static void writeToCSV(ArrayList<LinkedHashSet> arrayList, String[] args, String[] selectedKeys) throws IOException {
            String file1 = Paths.get(args[0]).getFileName().toString().replace(".csv", "");
            String file2 = Paths.get(args[1]).getFileName().toString().replace(".csv", "");
            String path;

            if (selectedKeys.length == 0){
                path = String.format("compare_%s_%s_all.csv", file1, file2);
            }
            else {
                 path = String.format("compare_%s_%s_selection.csv", file1, file2);
            }



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
