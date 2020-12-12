import jttp.api.ElementByteParseEventListener;
import jttp.standard.SHeaderByteParser;
import sun.net.www.http.HttpClient;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class TestRequest {

    public final static void main(String[] args) throws Exception
    {
        String header = "Content-Length: 200\r\n";
        SHeaderByteParser parser = new SHeaderByteParser();
        byte[] data = header.getBytes(Charset.forName("ASCII"));

        parser.setHeaderNameListener(new ElementByteParseEventListener() {
            @Override
            public void onElementData(byte[] data, int offset, int len, boolean completed) {
                System.out.println(new String(data , offset , len));
            }

            @Override
            public void onElementData(ByteBuffer buffer, boolean completed) {

            }

            @Override
            public void refresh() {

            }
        }).setHeaderValueListener(new ElementByteParseEventListener() {
            @Override
            public void onElementData(byte[] data, int offset, int len, boolean completed) {
                System.out.println(new String(data , offset , len));
            }

            @Override
            public void onElementData(ByteBuffer buffer, boolean completed) {

            }

            @Override
            public void refresh() {

            }
        }).read(data);

        parser.refresh();
        parser.read(data);
    }
}
