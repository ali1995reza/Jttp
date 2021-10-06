package jttp.standard;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class PrintString implements Consumer<ByteBuffer> {


    public final static PrintString ASCII =
            new PrintString(StandardCharsets.US_ASCII);

    public final static PrintString UTF_8 =
            new PrintString(StandardCharsets.UTF_8);


    private final Charset charset;

    private PrintString(Charset charset) {

        this.charset = charset;
    }


    @Override
    public void accept(ByteBuffer buffer) {
        System.out.println(new String(buffer.array(), buffer.position(), buffer.remaining(), charset));
    }
}
