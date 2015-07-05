package WordPredictor;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * Created by kvineet on 7/4/15.
 */
public class PrefixSuffixSplitBolt extends BaseRichBolt {
    OutputCollector outputCollector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String sentence = tuple.getStringByField("sentence");
        String[] words = sentence.split(" ");
        List<Values> prefixSuffixes = new ArrayList<Values>();

        String prefix = "";

        for(int ctr = 0; ctr < words.length - 1; ctr++) {
            if(StringUtils.isEmpty(prefix)) {
                prefix = words[ctr];
            }
            else {
                prefix = prefix + " " + words[ctr];
            }
            String suffix = words[ctr + 1];
            prefixSuffixes.add(new Values(prefix,suffix));
            prefixSuffixes.add(new Values(words[ctr], suffix));
        }

        for (Values prefixSuffix : prefixSuffixes)
        this.outputCollector.emit(prefixSuffix);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("prefix","suffix"));
    }
}
