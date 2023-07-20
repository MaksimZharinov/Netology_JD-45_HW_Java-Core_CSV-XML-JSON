import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
    }

    private static void writeString(String json) {
        try (FileWriter writer = new FileWriter("data.json")) {
            writer.write(json);
            writer.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gsonList = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gsonList.toJson(list, listType);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader list = new CSVReader(new FileReader(fileName))) {
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
