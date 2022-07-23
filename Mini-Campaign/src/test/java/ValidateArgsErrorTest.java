import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class ValidateArgsErrorTest {

    @Before
    public void setup() {
        System.out.println("Setting up Test: " + this.getClass().getSimpleName());
    }

    @After
    public void teardown() {
        System.out.println("Tear Down Test: " + this.getClass().getSimpleName() + "\n");
    }

    @Test
    public void ValidateNoArgs() {
        String[] args = new String[0];

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Less than 2 arguments Detected", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs1() {
        String[] args = new String[]{"test"};

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Less than 2 arguments Detected", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs2() {
        String command = "file1.csv";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Less than 2 arguments Detected", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs3() {
        String command = "file1.txt file2.csv -d \",\"";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Input is not CSV File", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs4() {
        String command = "file1.csv -d";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Input is not CSV File", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs5() {
        String command = "file1.csv file2.csv -d";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Invalid argument(s) Detected", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs6() {
        String command = "file1.csv file2.csv d";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Invalid argument(s) Detected", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs7() {
        String command = "file1.csv file2.csv d ,";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Invalid Option Detected", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs8() {
        String command = "file1.csv file2.csv -d /";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Invalid Delimiter Option", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs9() {
        String command = "file1.csv file2.csv -d | -p";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Invalid argument(s) Detected", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs10() {
        String command = "file1.csv file2.csv -d | -p none";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Invalid Columns Present Option", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs11() {
        String command = "file1.csv file2.csv -c 1,2,3 -d | -p none";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("Invalid Columns Present Option", exception.getMessage());
    }

    @Test
    public void ValidateTestArgs12() {
        String command = "file1.csv file2.csv -c 1 -d | -p true t";
        String[] args = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

//        System.out.println(Arrays.toString(args));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.validateArgs(args);
        });

//        System.out.println(exception.getMessage());

        assertEquals("More than 8 arguments Detected", exception.getMessage());
    }
}

