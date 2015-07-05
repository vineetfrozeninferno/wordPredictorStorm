package WordPredictor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by kvineet on 6/20/15.
 */
public class NetTextWordStreamSource implements IWordStreamBufferedReaderSource, java.io.Serializable {

    String urlToOpen;
    @Override
    public BufferedReader getWordStream() {
        try {
            URL url = new URL(urlToOpen);
            return new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setSourceURI(String URI) {
        this.urlToOpen = URI;
    }
}
