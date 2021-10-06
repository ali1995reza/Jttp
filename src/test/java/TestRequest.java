import jttp.api.ByteRequestLineParser;
import jttp.standard.ByteBufferAggregatorElementParser;
import jttp.standard.PrintString;
import jttp.standard.SByteRequestLineParser;

public class TestRequest {

    public final static void main(String[] args) throws Exception {
        ByteRequestLineParser parser =
                new SByteRequestLineParser()
                        .setMethodListener(
                                new ByteBufferAggregatorElementParser(200)
                                        .setOnCompleted(PrintString.UTF_8)
                        ).setRouteListener(
                                new ByteBufferAggregatorElementParser(200)
                                        .setOnCompleted(PrintString.UTF_8)
                        ).setVersionListener(
                                new ByteBufferAggregatorElementParser(200)
                                        .setOnCompleted(PrintString.UTF_8)
                        );

        String req = "GET / http/1.1\r\n";
        byte[] reqBytes = req.getBytes();

        for (int i = 0; i < reqBytes.length; i++) {
            System.out.println("I : " + i);
            parser.read(reqBytes, i, 1);
        }


    }
}
