package WordPredictor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kvineet on 6/20/15.
 */
public class FileWordStreamSource implements IWordStreamBufferedReaderSource, java.io.Serializable {

    String fileNameToOpen;
    @Override
    public BufferedReader getWordStream() {
        File file = new File(fileNameToOpen);
        try {
            InputStream is = new FileInputStream(file);
            return new BufferedReader(new InputStreamReader(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setSourceURI(String URI) {
        this.fileNameToOpen = URI;
    }
}
