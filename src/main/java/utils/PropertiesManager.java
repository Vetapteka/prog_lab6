package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

    public static Properties getProperties() {
        Properties property = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
        } catch (FileNotFoundException e) {
            System.out.println("please include the config.properties file");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("include a name for the ExitCommand in the config.properties file");
            e.printStackTrace();
        }
        return property;
    }
}



