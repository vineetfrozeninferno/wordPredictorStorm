package WordPredictor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by kvineet on 7/4/15.
 */
public class WikipediaStreamSource implements IWordStreamBufferedReaderSource, java.io.Serializable {

    @Override
    public BufferedReader getWordStream() {
        BufferedReader returnBuffer;
        String articleText = "";
        try {
            URL wikiUrl =
                new URL(
                    "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&generator=random&grnnamespace=0");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(wikiUrl.openStream()));
            String rawJson = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                rawJson += line;
            }
            int start = rawJson.indexOf("extract\":\"") + "extract: \"".length();
            int stop = rawJson.lastIndexOf("\"");
            articleText = rawJson.substring(start, stop);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStream is = new ByteArrayInputStream(articleText.getBytes());
        returnBuffer = new BufferedReader(new InputStreamReader(is));
        return returnBuffer;
    }

    @Override
    public void setSourceURI(String URI) {

    }
}
