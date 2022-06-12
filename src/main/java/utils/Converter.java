package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.MyCollection;

import java.io.*;
import java.text.SimpleDateFormat;

/**
 * provides the ability to write a {@link MyCollection} to a JSON file and read a {@link MyCollection} from a JSON file
 */
public class Converter {
    /**
     * mapper for all methods
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * writes the {@link MyCollection} to a file
     *
     * @param myCollection class for convert
     * @param fileName     where to  write result
     * @throws FileNotFoundException if file is not found or others access errors
     */
    public static void toJson(MyCollection myCollection, String fileName) throws FileNotFoundException {
        setDateFormat();
        OutputStreamWriter writer;
        writer = new OutputStreamWriter(new FileOutputStream(fileName));
        try {
            writer.write(mapper.writeValueAsString(myCollection));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads a  {@link MyCollection} from a file
     *
     * @param fileName where to read
     * @return {@link MyCollection}
     * @throws IOException if file is invalid
     */
    public static MyCollection fromJson(String fileName) throws IOException {
        setDateFormat();
        return mapper.readValue(new File(fileName), MyCollection.class);

    }


    /**
     * sets the format for the date
     */
    private static void setDateFormat() {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        mapper.registerModule(new JavaTimeModule());
    }


}
