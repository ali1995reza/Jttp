package jttp.standard;

import java.nio.ByteBuffer;

public class ByteBufferElementParser extends ElementReader{

    private final ByteBuffer buffer;

    public ByteBufferElementParser(ByteBuffer buffer) {
        this.buffer = buffer;
    }


    @Override
    public void onRead(byte b) {
        buffer.put(b);
    }


    @Override
    public void onElementParsed() {
        buffer.flip();
    }

    public ByteBuffer buffer() {
        Assertion.ifTrue(!isElementParsed());
        return buffer;
    }
}
