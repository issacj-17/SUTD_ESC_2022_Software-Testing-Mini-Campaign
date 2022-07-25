import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Main {
    private static String delimiter;
    private static boolean columnsPresent;
    private static String[] selectedColumns;

    public static void main(String[] args){
        System.out.println("Initialising Program...");
        long start = System.nanoTime();

        try {
            run(args);
            System.out.println("Program Success!");
        }
        catch (IllegalArgumentException iae){
            System.out.println("Program Failed - IllegalArgumentException");
            System.err.println(iae.getMessage());
//            iae.printStackTrace();
        }
        catch (IOException ioe) {
            System.out.println("Program Failed - IOException");
            System.err.println(ioe.getMessage());
//            ioe.printStackTrace();
        }
        catch (CSV.CSVException csv) {
            System.out.println("Program Failed - CSVException");
            System.err.println(csv.getMessage());
//            csv.printStackTrace();
        }
        finally {
            long end = System.nanoTime();
            long duration = (long) ((end - start) / Math.pow(10, 6));
            String message = String.format("Execution Time: %sms", duration);
            System.out.println(message);
            System.out.println("Program Terminated");
        }
    }

    public static void run(String[] args) throws IOException, CSV.CSVException {
        LinkedHashSet<LinkedHashMap> csvFile1;
        LinkedHashSet<LinkedHashMap> csvFile2;
        ArrayList<LinkedHashSet> result;

        selectedColumns = new String[0];
        delimiter = ",";
        columnsPresent = true;

        validateArgs(args);

        csvFile1 = CSV.CSVParser.parseCSV(args[0], delimiter, columnsPresent);
        csvFile2 = CSV.CSVParser.parseCSV(args[1], delimiter, columnsPresent);

        result = CSV.CSVCompare.compare(csvFile1, csvFile2, selectedColumns);

        CSV.CSVWriter.writeToCSV(result, args, selectedColumns);
    }

    public static void validateArgs(String[] args) throws IllegalArgumentException {
        // java Main.java .\file1.csv .\file1.csv -d , -p true -c 1,2,3
        ArrayList<String> options = new ArrayList<>(Arrays.asList("-d", "-p", "-c"));
        ArrayList<String> delimiters = new ArrayList<>(Arrays.asList(",", "|", ";", "tab", "space"));

//        System.out.println("Arguments: " + Arrays.toString(args));

        if (args.length < 2) {
            throw new IllegalArgumentException("Less than 2 arguments Detected");
        }

        if (args.length > 8) {
            throw new IllegalArgumentException("More than 8 arguments Detected");
        }

        if (args.length % 2 == 1){
            throw new IllegalArgumentException("Invalid argument(s) Detected");
        }

        if (!args[0].endsWith(".csv") || !args[1].endsWith(".csv")) {
            throw new IllegalArgumentException("Input is not CSV File");
        }

        for (int i = 2; i < args.length; i += 2){
            final String arg = args[i];
            final String option = args[i + 1];

            if (arg.charAt(0) == '-' && arg.length() == 2 && options.contains(arg)){
                if (arg.equals(options.get(0))){
                    if (delimiters.contains(option)){
                        if (option == "space") {
                            delimiter = " ";
                        }
                        else if (option == "tab") {
                            delimiter = "\t";
                        }
                        else {
                            delimiter = option;
                        }
                    }
                    else {
                        throw new IllegalArgumentException("Invalid Delimiter Option");
                    }
                }
                else if (arg.equals(options.get(1))) {
                    if (option.equalsIgnoreCase("true")) {
                        columnsPresent = true;
                    }
                    else if (option.equalsIgnoreCase("false")) {
                        columnsPresent = false;
                    }
                    else {
                        throw new IllegalArgumentException("Invalid Columns Present Option");
                    }

                }
                else if(arg.equals(options.get(2))) {
                    String[] names = option.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    selectedColumns = names;

//                    System.out.println(Arrays.toString(names));
                }
            }
            else{
                throw new IllegalArgumentException("Invalid Option Detected");
            }
        }

        System.out.println("Arguments: Validate Success!");
    }

}
