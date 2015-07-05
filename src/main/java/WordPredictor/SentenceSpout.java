package WordPredictor;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Created by kvineet on 6/17/15.
 */
public class SentenceSpout extends BaseRichSpout {

    SpoutOutputCollector outputCollector;
    BufferedReader reader;
    IWordStreamBufferedReaderSource bufferedReaderSource;
    List<String> endChars = new ArrayList<String>();
    int runCount =1;

    public SentenceSpout(IWordStreamBufferedReaderSource source) {
        endChars.add(";");
        endChars.add("!");
        endChars.add(",");
        endChars.add("?");
        endChars.add(".");
        this.bufferedReaderSource = source;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.outputCollector = spoutOutputCollector;
        this.reader = bufferedReaderSource.getWordStream();
    }

    @Override
    public void nextTuple() {
        System.out.println("Run Count = " + runCount);
        runCount++;
        List<Values> emitWordPairs = new ArrayList<Values>();
        try {
            String line;
            do {
                line = reader.readLine();
            } while (StringUtils.isEmpty(line));

            line = line.replaceAll("[^\\x20-\\x7e]", " ");
            line = line.replaceAll("-", "");
            line = line.replaceAll("\"", "");
            line = line.toLowerCase();
            String[] sentences = line.split("[.|?|;|,]");

            for(String sentence : sentences) {
                Values wordPair = new Values(sentence);
                emitWordPairs.add(wordPair);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Values wordPair : emitWordPairs) {
            outputCollector.emit(wordPair);
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
