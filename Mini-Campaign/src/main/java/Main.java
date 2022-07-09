import java.io.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Main {
    public static void main(String[] args){
        LinkedHashSet<LinkedHashMap> csvFile1 = new LinkedHashSet<>();
        LinkedHashSet<LinkedHashMap> csvFile2 = new LinkedHashSet<>();
        ArrayList<LinkedHashSet> result = new ArrayList<>();

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
        finally {
            System.out.println("Terminating Program");
        }
    }

    public static void validateArgs(String[] args) throws IllegalArgumentException{
        if (args.length != 2) {
            throw new IllegalArgumentException("Program only accepts 2 arguments.");
        }

        if (!args[0].endsWith(".csv") || !args[1].endsWith(".csv")) {
            throw new IllegalArgumentException("Input File is not in CSV format");
        }

        System.out.println("Validate Success!");
    }

    public static LinkedHashSet parseCSV(String filePath) throws IOException {
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

//                for (String ele : columns) {
//                    System.out.print(ele);
//                }
//
//                System.out.println();
            }
            else {
                String[] row = line.split(delimiter);

                for (int j = 0; j < row.length; j++) {
                    row[j] = row[j].strip();
                }

                if (row.length == columns.length) {
                    hashMap = new LinkedHashMap<>();

                    for (int j = 0; j < row.length; j++) {
                        hashMap.put(columns[j], row[j]);
                    }

                    hashSet.add(hashMap);
                }
            }
            i++;
        }

        reader.close();
//        System.out.println(hashSet);
        System.out.println("Parse Success!");
        return hashSet;
    }

    public static ArrayList<LinkedHashSet> compareValues(LinkedHashSet<LinkedHashMap> set1, LinkedHashSet<LinkedHashMap> set2) {
        ArrayList<LinkedHashSet> arrayList = new ArrayList<>();
        LinkedHashSet<LinkedHashMap> set = new LinkedHashSet<>();

        for (LinkedHashMap hashMap : set2) {
            if (set1.contains(hashMap)){
                set1.remove(hashMap);
            }
            else {
                set.add(hashMap);
            }
        }

        arrayList.add(set1);
        arrayList.add(set);

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

//                System.out.println(hashMap.entrySet());
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

}
