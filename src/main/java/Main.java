import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
    }
    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try(CSVReader list = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csvList = new CsvToBeanBuilder<Employee>(list)
                    .withMappingStrategy(strategy)
                    .build();
            return csvList.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
