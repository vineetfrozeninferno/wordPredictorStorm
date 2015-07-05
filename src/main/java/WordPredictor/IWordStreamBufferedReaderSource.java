package WordPredictor;

import java.io.BufferedReader;

/**
 * Created by kvineet on 6/20/15.
 */
public interface IWordStreamBufferedReaderSource {
    BufferedReader getWordStream();
    void setSourceURI(String URI);
}
