package WordPredictor;

import backtype.storm.topology.IRichBolt;

/**
 * Created by kvineet on 6/16/15.
 */
public interface MemoryStoreWriterBolt extends IRichBolt {
    boolean insertWordPair(String word1, String word2);
}
