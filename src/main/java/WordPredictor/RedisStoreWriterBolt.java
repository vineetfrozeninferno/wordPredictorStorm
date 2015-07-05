package WordPredictor;

import java.util.Map;
import java.util.Set;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class RedisStoreWriterBolt implements MemoryStoreWriterBolt {

    OutputCollector outputCollector;

    @Override
    public boolean insertWordPair(String word1, String wordFollowing) {
        Jedis jedis = new Jedis("localhost");
        Set<Tuple> followingWords = jedis.zrangeWithScores(word1, Long.MIN_VALUE, Long.MAX_VALUE);

        Long status = 0l;

        boolean insertSucceeded = false;
        for (Tuple words : followingWords) {
            if(words.getElement().equals(wordFollowing)) {
                double score = words.getScore();
                score++;
                status = jedis.zadd(word1, score, wordFollowing);
                insertSucceeded = true;
            }
        }
        if(!insertSucceeded) {
            status = jedis.zadd(word1, 1, wordFollowing);
        }
        if(1 == status) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    @Override
    public void execute(backtype.storm.tuple.Tuple tuple) {
        String word1 = tuple.getStringByField("prefix");
        String word2 = tuple.getStringByField("suffix");
        insertWordPair(word1, word2);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
