import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SystemTest {
    @Before
    public void setup() {
        System.out.println(String.format("Setting up Test: %s", this.getClass().getSimpleName()));
    }

    @After
    public void teardown() {
        System.out.println(String.format("Tear Down Test: %s \n", this.getClass().getSimpleName()));
    }

    @Test
    public void Test1() {
        String command = ".\\assets\\testfile\\sample_file_1.csv .\\assets\\testfile\\sample_file_1.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));
        Main.main(args);
    }

    @Test
    public void Test2() {
        String command = ".\\assets\\testfile\\sample_file_1.csv .\\assets\\testfile\\sample_file_1_copy.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));
        Main.main(args);
    }

    @Test
    public void Test3() {
        String command = ".\\assets\\testfile\\sample_file_1.csv .\\assets\\testfile\\sample_Res.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(IOException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test4() {
        String command = ".\\assets\\testfile\\sample_file_1.csv .\\assets\\testfile\\sample_file.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(IOException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test5() {
        String command = ".\\assets\\testfile\\sample_file_1.txt .\\assets\\testfile\\sample_file_1.xlsx";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(IllegalArgumentException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test6() {
        String command = ".\\assets\\testfile\\sample_file_1.csv .\\assets\\testfile\\sample_file_1.tsv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(IllegalArgumentException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test7() {
        String command = ".\\assets\\testfile\\sample_file_1.csv .\\assets\\testfile\\sample_file_1.tsv -d comma -p null";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(IllegalArgumentException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test8() {
        String command = ".\\assets\\testfile\\sample_file_1.csv .\\assets\\testfile\\sample_file_1.tsv -d -p null";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(IllegalArgumentException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test9() {
        String command = ".\\assets\\testfile\\test-control.csv .\\assets\\testfile\\test-duplicate-column-names.csv -c \"Currency\",\"Type\"";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(CSV.CSVException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test10() {
        String command = ".\\assets\\testfile\\test-empty-column-name1.csv .\\assets\\testfile\\test-empty-column-name2.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(CSV.CSVException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test11() {
        String command = ".\\assets\\testfile\\test-add-entry1.csv .\\assets\\testfile\\test-add-entry2.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(CSV.CSVException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test12() {
        String command = ".\\assets\\testfile\\test-miss-entry1.csv .\\assets\\testfile\\test-miss-entry2.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(CSV.CSVException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test13() {
        String command = ".\\assets\\testfile\\test-missing-column1.csv .\\assets\\testfile\\test-missing-column2.csv -c \"Balance\",\"Type\"";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));
        assertThrows(CSV.CSVException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test14() {
        String command = ".\\assets\\testfile\\test-missing-column1.csv .\\assets\\testfile\\test-missing-column3.csv -c \"Balance\",\"Account No.\"";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));
        assertThrows(CSV.CSVException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test15() {
        String command = ".\\assets\\testfile\\test-full-compare1.csv .\\assets\\testfile\\test-full-compare2.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(CSV.CSVException.class, () -> {
            Main.run(args);
        });
    }

    @Test
    public void Test16() {
        String command = ".\\assets\\testfile\\test-full-compare2.csv .\\assets\\testfile\\test-full-compare3.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        assertThrows(CSV.CSVException.class, () -> {
            Main.run(args);
        });

    }

    /** Note: Rerun Test17 to Test22 if they fail. There may be a bug in running the FileUtils method concurrently. */
    @Test
    public void Test17() {
        String command = ".\\assets\\testfile\\test-duplicate1.csv .\\assets\\testfile\\test-duplicate2.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        try {
            Main.run(args);

            Thread.sleep(50);

            File file1 = new File("compare_test-duplicate1_test-duplicate2_all.csv");
            File file2 = new File(".\\assets\\expected\\expected_test-duplicate1_test-duplicate2_all.csv");

            assertTrue("Files Are Different", FileUtils.contentEquals(file1, file2));

        } catch (IOException | CSV.CSVException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Test18() {
        String command = ".\\assets\\testfile\\test-duplicate3.csv .\\assets\\testfile\\test-duplicate4.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        try {
            Main.run(args);

            Thread.sleep(50);

            File file1 = new File("compare_test-duplicate3_test-duplicate4_all.csv");
            File file2 = new File(".\\assets\\expected\\expected_test-duplicate3_test-duplicate4_all.csv");

            assertTrue("Files Are Different", FileUtils.contentEquals(file1, file2));

        } catch (IOException | CSV.CSVException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Test19() {
        String command = ".\\assets\\testfile\\test-duplicate5.csv .\\assets\\testfile\\test-duplicate6.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        try {
            Main.run(args);

            Thread.sleep(50);

            File file1 = new File("compare_test-duplicate5_test-duplicate6_all.csv");
            File file2 = new File(".\\assets\\expected\\expected_test-duplicate5_test-duplicate6_all.csv");

            assertTrue("Files Are Different", FileUtils.contentEquals(file1, file2));

        } catch (IOException | CSV.CSVException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Test20() {
        String command = ".\\assets\\testfile\\test-duplicate5.csv .\\assets\\testfile\\test-duplicate7.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        try {
            Main.run(args);

            Thread.sleep(50);

            File file1 = new File("compare_test-duplicate5_test-duplicate7_all.csv");
            File file2 = new File(".\\assets\\expected\\expected_test-duplicate5_test-duplicate7_all.csv");

            assertTrue("Files Are Different", FileUtils.contentEquals(file1, file2));

        } catch (IOException | CSV.CSVException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Test21() {
        String command = ".\\assets\\testfile\\sample_file_1.csv .\\assets\\testfile\\sample_file_2.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));



        try {
            Main.run(args);

            Thread.sleep(50);

            File file1 = new File("compare_sample_file_1_sample_file_2_all.csv");
            File file2 = new File(".\\assets\\expected\\expected_sample_file_1_sample_file_2_all.csv");

            assertTrue("Files Are Different", FileUtils.contentEquals(file1, file2));

        } catch (IOException | CSV.CSVException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Test22() {
        String command = ".\\assets\\testfile\\test-control.csv .\\assets\\testfile\\test-difference2.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        System.out.println(Arrays.toString(args));

        try {
            Main.run(args);

            Thread.sleep(50);

            File file1 = new File("compare_test-control_test-difference2_all.csv");
            File file2 = new File(".\\assets\\expected\\expected_test-control_test-difference2_all.csv");

            assertTrue("Files Are Different", FileUtils.contentEquals(file1, file2));

        } catch (IOException | CSV.CSVException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
