package refactor_adressbook.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Component
public class ResourcesUtil {

    /**
     * Load property file from resources by file name.
     *
     * @param fileName name of property file
     * @return loaded <tt>Properties</tt>
     * @throws IOException if an error occurred when reading from the
     *                     input stream
     */
    public Properties loadPropertiesFromResources(String fileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        }
        return properties;
    }
}
