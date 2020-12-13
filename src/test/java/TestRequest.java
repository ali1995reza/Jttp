import jttp.api.ElementByteParseEventListener;
import jttp.standard.SHeaderByteParser;
import sun.net.www.http.HttpClient;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class TestRequest {

    public final static void main(String[] args) throws Exception
    {
        String header = "Content-Length: 200 OK IM NOT SHIT RIGHT NOW IDIOTS\r\n";
        SHeaderByteParser parser = new SHeaderByteParser();
        byte[] data = header.getBytes(Charset.forName("ASCII"));
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        parser.setHeaderNameListener(new ElementByteParseEventListener() {
            @Override
            public void onElementData(byte[] data, int offset, int len, boolean completed) {
                System.out.println(new String(data , offset , len));
            }

            @Override
            public void onElementData(ByteBuffer buffer, boolean completed) {
                System.out.println(new String(buffer.array() , buffer.position() , buffer.remaining()));
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
                System.out.println(new String(buffer.array() , buffer.position() , buffer.remaining()));
            }

            @Override
            public void refresh() {

            }
        }).read(byteBuffer);

        parser.refresh();
        parser.read(byteBuffer);
    }
}
