import jttp.api.ByteRequestLineParser;
import jttp.api.ElementByteParseEventListener;
import jttp.api.RequestLineParser;
import jttp.standard.ByteBufferAggregatorElementParser;
import jttp.standard.PrintString;
import jttp.standard.SByteRequestLineParser;
import jttp.standard.SHeaderByteParser;
import sun.net.www.http.HttpClient;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class TestRequest {

    public final static void main(String[] args) throws Exception
    {
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

        for(int i=0;i<reqBytes.length;i++)
        {
            System.out.println("I : "+i);
            parser.read(reqBytes , i , 1);
        }


    }
}
