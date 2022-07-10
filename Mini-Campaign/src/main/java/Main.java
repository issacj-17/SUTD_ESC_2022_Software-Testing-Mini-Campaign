import java.io.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Main {
    public static void main(String[] args){
        LinkedHashSet<LinkedHashMap> csvFile1;
        LinkedHashSet<LinkedHashMap> csvFile2;
        ArrayList<LinkedHashSet> result;

        try {
            System.out.println("Initialising Program");

            validateArgs(args);

            csvFile1 = parseCSV(args[0]);
            csvFile2 = parseCSV(args[1]);

            result = compareValues(csvFile1, csvFile2);

            writeToCSV(result, args);

            System.out.println("Program was Successful");
        }
        catch (IllegalArgumentException iae){
            System.out.println("Program Failed");
            iae.printStackTrace();
        }
        catch (IOException ioe) {
            System.out.println("Program Failed");
            ioe.printStackTrace();
        }
        catch (CSVException csv) {
            System.out.println("Program Failed");
            csv.printStackTrace();
        }
        finally {
            System.out.println("Terminating Program");
        }
    }

    public static void validateArgs(String[] args) throws IllegalArgumentException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Program only accepts 2 arguments.");
        }

        if (!args[0].endsWith(".csv") || !args[1].endsWith(".csv")) {
            throw new IllegalArgumentException("Input File is not in CSV format");
        }

        System.out.println("Validate Success!");
    }

    public static LinkedHashSet<LinkedHashMap> parseCSV(String filePath) throws IOException, CSVException {
        String line;
        String delimiter = ",";
        String[] columns = new String[0];

        int i = 0;

        LinkedHashSet<LinkedHashMap> hashSet = new LinkedHashSet<>();
        LinkedHashMap<String, String> hashMap;

        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        while ((line = reader.readLine()) != null) {

            if (i == 0) {
                columns = line.split(delimiter);

                validateColumns(columns);

//                System.out.println(Arrays.toString(columns));
            }
            else {
                String[] row = line.split(delimiter);

                hashMap = getLinkedHashMap(columns, row);
                hashSet.add(hashMap);

            }

            i++;
        }

        reader.close();
//        System.out.println(hashSet);
        System.out.println("Parse Success!");
        return hashSet;
    }

    private static LinkedHashMap<String, String> getLinkedHashMap(String[] columns, String[] row) {
        LinkedHashMap<String, String> hashMap;
        hashMap = new LinkedHashMap<>();

        if (row.length != columns.length) {
            System.out.println("Entry Does Not Match Column Count!");

//            System.out.println(Arrays.toString(columns));
//            System.out.println(Arrays.toString(row));
                }

        for (int j = 0; j < row.length; j++) {
            String clean = row[j].strip();
//                    System.out.println(row[j] + ", " + clean);

            if (!row[j].equals(clean)) {
                System.out.println("White Space Detected!");
                row[j] = row[j].strip();
            }

            if (j < columns.length) {
                hashMap.put(columns[j], row[j]);
            }

//                    System.out.println(hashMap);
        }
        return hashMap;
    }

    private static void validateColumns(String[] columns) throws CSVException {
        LinkedHashSet<String> set = new LinkedHashSet<>();

        for (int j = 0; j < columns.length; j++) {
            String clean = columns[j].strip();
//                    System.out.println(columns[j] + ", " + clean);

            if (!columns[j].equals(clean)) {
                System.out.println("White Space Detected!");
                columns[j] = columns[j].strip();
            }


            if (!set.add(columns[j])) {
                System.out.println(set);
                System.out.println(columns[j]);

                throw new CSVException("Duplicate Column Names Detected!");
            }
        }
    }

    public static ArrayList<LinkedHashSet> compareValues(LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2) {
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

        System.out.println("Compare Success!");
        return arrayList;
    }

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

        System.out.println("Write Success!");
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

    public static class CSVException extends Exception {
        public CSVException(String str) {
            super(str);
        }
    }
}
