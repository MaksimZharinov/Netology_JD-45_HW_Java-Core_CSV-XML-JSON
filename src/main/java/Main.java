import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //task1
//        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
//        String fileName = "data.csv";
//        List<Employee> list = parseCSV(columnMapping, fileName);
//        String json = listToJson(list);
//        writeString(json, "data.json");

        //task2
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        writeString(json, "data2.json");
    }

    private static List<Employee> parseXML(String file) {
        List<Employee> list = new ArrayList<>();
        long id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(file));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    NamedNodeMap attr = element.getAttributes();
                    for (int a = 0; a < attr.getLength(); a++) {
                        String attrName = attr.item(a).getNodeName();
                        String attrValue = attr.item(a).getNodeValue();
                        switch (attrName) {
                            case "id":
                                id = Long.parseLong(attrValue);
                                break;
                            case "firstName":
                                firstName = attrValue;
                                break;
                            case "lastName":
                                lastName = attrValue;
                                break;
                            case "country":
                                country = attrValue;
                                break;
                            case "age":
                                age = Integer.parseInt(attrValue);
                                break;
                        }
                    }
                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    list.add(employee);
                }
            }
            return list;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void writeString(String json, String file) {
        try (FileWriter writer = new FileWriter(file)) {
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
