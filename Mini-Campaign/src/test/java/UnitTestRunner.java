import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.JUnit4TestAdapter;

// This section declares all the test classes in the program.
@RunWith (Suite.class)

//BlackBox Not Included due to errors in executing assertions
@Suite.SuiteClasses ({ CSVReadWriteTest.class, ValidateArgsParamTest.class, ValidateArgsErrorTest.class })  // Add test classes here.

public class UnitTestRunner
{
    // Execution begins at main().  In this test class, we will execute
    // a text test runner that will tell you if any of your tests fail.
    public static void main (String[] args) {
        JUnit4TestAdapter suite = new JUnit4TestAdapter (UnitTestRunner.class);
        junit.textui.TestRunner.run(suite);
    }
}
