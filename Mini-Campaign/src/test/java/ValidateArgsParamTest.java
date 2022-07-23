import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

@RunWith(Parameterized.class)
public class ValidateArgsParamTest {

    @Parameters
    public static Collection<Object[]> generateData(){
        List<Object[]> data = new ArrayList<>();

        String file1 = "test1.csv";
        String file2 = "test2.csv";

        ArrayList<String> options = new ArrayList<>(Arrays.asList("-d", "-p", "-c"));
        ArrayList<String> delimiters = new ArrayList<>(Arrays.asList("", ",", "|", ";", "tab", "space"));
        ArrayList<String> columnsPresent = new ArrayList<>(Arrays.asList("", "true", "false"));
        ArrayList<String> columns = new ArrayList<>(Arrays.asList("", "1,2,3", "name,age,gender", "\"1, 2, 3\"", "\"name, age, gender\"", "name", "1"));

        Map<String, ArrayList<String>> possibleOptions = new HashMap<>();
        possibleOptions.put(options.get(0), delimiters);
        possibleOptions.put(options.get(1), columnsPresent);
        possibleOptions.put(options.get(2), columns);

        for (int i = 0; i < delimiters.size(); i++) {
            List<String> listArgs = new ArrayList<>();

            listArgs.add(file1);
            listArgs.add(file2);

            String key = options.get(0);
            ArrayList<String> values = possibleOptions.get(key);
            listArgs = addArguments(listArgs, key, values, i);

            for (int j = 0; j < columnsPresent.size(); j++) {
                key = options.get(1);
                values = possibleOptions.get(key);
                List<String> result1 = addArguments(listArgs, key, values, j);

                for (int k = 0; k < columns.size(); k++){
                    key = options.get(2);
                    values = possibleOptions.get(key);
                    List<String> result2 = addArguments(result1, key, values, k);

                    String[] args = new String[result2.size()];
                    result2.toArray(args);
//                    System.out.println(Arrays.toString(args));
                    data.add(new Object[]{args});
                }
            }
        }

        return data;
    }

    public static List<String> addArguments(List<String> args, String option, ArrayList<String> possibleOptions, int index) {
        List<String> result = new ArrayList<>(args);
            String value = possibleOptions.get(index);

            if (!Objects.equals(value, "")){
                result.add(option);
                result.add(value);
            }

            return result;
    }

    private String[] args;

    public ValidateArgsParamTest(String[] args){
        this.args = args;
    }

    @Before
    public void setup() {
        System.out.println(String.format("Setting up Test: %s", this.getClass().getSimpleName()));
    }

    @After
    public void teardown() {
        System.out.println(String.format("Tear Down Test: %s \n", this.getClass().getSimpleName()));
    }

    @Test
    public void ValidateTestArgs() {
//        System.out.println(Arrays.toString(this.args));
        Main.validateArgs(this.args);
    }

}
