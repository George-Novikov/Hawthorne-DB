import config.Configuration;
import model.constants.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hawthorne {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hawthorne.class);
    private static final Configuration CONFIG = new Configuration();

    public static void main(String[] args){
        try {
            LOGGER.info("start");

            LOGGER.info("Main file name: {}", CONFIG.readProperty(ConfigProperty.MANAGING_FILE_NAME));

            Thread.sleep(2000);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("end");
        }
    }
}
