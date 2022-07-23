import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertTrue;

public class CSVReadWriteTest {
    @Before
    public void setup() {
        System.out.println("Setting up Test: " + this.getClass().getSimpleName());
    }

    @After
    public void teardown() {
        System.out.println("Tear Down Test: " + this.getClass().getSimpleName() + "\n");
    }

    @Test
    public void Parse() throws IOException {
        String filePath = ".\\assets\\testfile\\test-control.csv";
        String delimiter = ",";
        ArrayList<LinkedHashSet> resultArray = new ArrayList<>();
        String[] args = new String[]{filePath, filePath};

        try {
            LinkedHashSet<LinkedHashMap> result = CSV.CSVParser.parseCSV(filePath, delimiter, true);
            resultArray.add(result);
            resultArray.add(result);

            String[] keys = new String[0];
            CSV.CSVWriter.writeToCSV(resultArray, args, keys);

            Thread.sleep(50);

            File file1 = new File("compare_test-control_test-control_all.csv");
            File file2 = new File(".\\assets\\expected\\expectedReadWrite.csv");

            assertTrue("Files Are Different", FileUtils.contentEquals(file1, file2));

        } catch (IOException | CSV.CSVException | InterruptedException e) {
            throw new RuntimeException(e);

        }
    }

}
