# SUTD ESC 2022 - Software Testing Mini Campaign

## Issac Jose Ignatius (1004999)

### Use Case Diagram

Use Case Diagram is located in Mini-Campaign folder as `Use Case Diagram.jpg`.

### Equivalence Class Partitioning with Boundary Value Analysis

The Document is located in Mini-Campaign folder as `Blackbox Test Design.pdf`.

### Program Implementation

To run the program (located in Mini-Campaign/src/main/java folder), run the following command in terminal using the Main.java file: \
`java -classpath .\Mini-Campaign\target\classes\ Main <path-of-CSV-File-1> <path-of-CSV-File-2> [flags]`

Note: 
  - The Unique Combination provided is assumed to be the basis of comparison between 2 entries (e.g. -c 1,3,4 would mean that the program checks whether the values in columns 1, 3 and 4 of one CSV file exist in the corresponding columns 1, 3 and 4 in the other CSV file).
  - Use the Absolute path of the Input File. The Output File will be generated at the Current Working Directory of the Terminal. 

Flags:

  - -d    Delimiters used in BOTH CSV Files                                      choices: {",", "|", ";", "tab", "space"} (Default: ",")
  - -p    Boolean dictating whether columns are present in BOTH CSV Files        choices: {"true", "false"} (Case-Insensitive, Default: "true")
  - -c    Unique Input Combination    (if -p true, Indicate column names with double quotes, separated by commas with no space. Otherwise, Indicate column position using 1-based indexing, separated by commas with no space) (Default: All Columns across both CSV Files are compared)

Examples:
  - `java -classpath .\Mini-Campaign\target\classes\ Main <path-of-CSV-File-1> <path-of-CSV-File-2>`
  - `java -classpath .\Mini-Campaign\target\classes\ Main <path-of-CSV-File-1> <path-of-CSV-File-2> -d ,`
  - `java -classpath .\Mini-Campaign\target\classes\ Main <path-of-CSV-File-1> <path-of-CSV-File-2> -p false`
  - `java -classpath .\Mini-Campaign\target\classes\ Main <path-of-CSV-File-1> <path-of-CSV-File-2> -p true -c "Customer ID#","Currency"`
  - `java -classpath .\Mini-Campaign\target\classes\ Main <path-of-CSV-File-1> <path-of-CSV-File-2> -c "Customer ID#","Type","Balance"`
  - `java -classpath .\Mini-Campaign\target\classes\ Main <path-of-CSV-File-1> <path-of-CSV-File-2> -p false -c 1,3,4`
  - `java -classpath .\Mini-Campaign\target\classes\ Main <path-of-CSV-File-1> <path-of-CSV-File-2> -p false -c 1,3,4 -d |`

Program throws the Following Exceptions via a stacktrace:

- IllegalArgumentException
- IOException
- CSVException (Program-Specific)

### Note 

1. There are some implementations that I am currently not sure to define as an Exception via the Program or as a mismatch -

    - Entries do not match column count
    - Presence of whitespace in the CSV files e.g. A,B,C v.s. A, B,C

At the moment, I have left them as print statements until further clarification is obtained on the requirements of the program from the instructors. I will not clarify them openly as these may be possible bugs for the bug bounty campaign.

### Future Implementation

Priority 1 - Implement Test Cases
