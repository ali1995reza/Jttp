import jttp.standard.ElementParser;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Test {


    public static void main(String[] args) throws Exception
    {
        ElementParser parser = new ElementParser(ByteBuffer.allocate(200));

        String line = "GET / HTTP/1.1\r\n";
        System.out.println(line.getBytes(StandardCharsets.US_ASCII).length);
        int read = parser.read(line.getBytes(StandardCharsets.US_ASCII));

        System.out.println(read);

        if(parser.isElementParsed())
        {
            byte[] data = new byte[parser.buffer().remaining()];
            parser.buffer().get(data);

            System.out.println(new String(data));
        }
    }
}
