import jttp.api.HttpHeader;
import jttp.standard.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Test {


    public static void main(String[] args) throws Exception
    {
        SRequestLineParser parser = new SRequestLineParser(ByteBuffer.allocate(200));
        parser.setTolerant(TolerantConfig.TOLERANT_ENABLE);

        String line = "GET       / HTTP/1.1\r\n";
        int read = parser.read(line.getBytes(StandardCharsets.US_ASCII));
        SHeaderParser headerParser = new SHeaderParser(ByteBuffer.allocate(200));
        String header = "Content-Length: HelloWorld\r\n";

        headerParser.read(header.getBytes());

        if(parser.isElementParsed())
        {
            System.out.println(parser.line());
        }

        if(headerParser.isElementParsed())
        {
            System.out.println(headerParser.header());
        }
    }
}
