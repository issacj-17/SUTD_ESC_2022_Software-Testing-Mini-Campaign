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

### Testing

#### Blackbox Testing
- Unit Testing and System Testing (via BlackBox Testing) implemented using JUnit 4.13.2 
- For System Testing, 11 partitions were identified and 2 test cases correspond to Middle and Boundary Values respectively.
- All Test Cases can be run by running the `UnitTestRunner.java` file.

#### Fuzzing
- To run the fuzzer, navigate the current working directory of the terminal to `.\Mini-Campaign\Fuzzer` Directory and install the required packages using the `requirements.txt` file.
- Enter `py fuzzer.py` into the shell at `.\Mini-Campaign\Fuzzer` Directory.
- Check the `.\Mini-Campaign\Fuzzer\testfile\output.log` file to take a look at the logs of each trial as performed by `fuzzer.py` file.
- Delete the `output.log` file once you are done.


### 3rd Party Implementation Testing
- All relevant files are located on the Root Folder.
- The 2 repositories that I worked on are https://github.com/xl-Mu/50.003-Software-Testing-Mini-Campaign
and https://github.com/PurpleDice95/ESC-HW .
- Please refer to the MS Word Document - Week 13 - 3rd Party Implementation - Bug Bounty.docx - for more details.