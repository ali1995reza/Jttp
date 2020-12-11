package jttp.standard;

import java.nio.ByteBuffer;

public class ByteBufferElementParser extends SElementReader {

    private final ByteBuffer buffer;

    public ByteBufferElementParser(ByteBuffer buffer) {
        this.buffer = buffer;
    }


    @Override
    public void onRead(byte b) {
        buffer.put(b);
    }


    @Override
    public void onElementParsed(boolean isCRLF) {
        buffer.flip();
    }

    public ByteBuffer buffer() {
        Assertion.ifTrue(!isElementParsed());
        return buffer;
    }
}
