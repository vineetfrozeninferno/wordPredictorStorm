package WordPredictor;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * Created by kvineet on 6/17/15.
 */
public class wordPredictorMain {
    public static void main(String[] args) throws Exception {
        WikipediaStreamSource wss = new WikipediaStreamSource();
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("WordPairSource", new SentenceSpout(wss), 12);
        builder.setBolt("prefixSuffixSplitterBolt", new PrefixSuffixSplitBolt(), 15).shuffleGrouping("WordPairSource");
        builder.setBolt("redisSerialize", new RedisStoreWriterBolt(), 35).shuffleGrouping("prefixSuffixSplitterBolt");

        Config conf = new Config();
        conf.setDebug(false);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        } else {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", conf, builder.createTopology());
            Utils.sleep(200000);
            cluster.killTopology("test");
            cluster.shutdown();
        }
    }
}
