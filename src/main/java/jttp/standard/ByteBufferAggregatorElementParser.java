package jttp.standard;

import jttp.api.ElementByteParseEventListener;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class ByteBufferAggregatorElementParser implements ElementByteParseEventListener {

    private final ByteBuffer buffer;
    private Consumer<ByteBuffer> onCompleted;

    public ByteBufferAggregatorElementParser(int cap) {
        buffer = ByteBuffer.allocate(cap);

    }


    public ByteBufferAggregatorElementParser setOnCompleted(Consumer<ByteBuffer> onCompleted) {
        this.onCompleted = onCompleted;
        return this;
    }

    @Override
    public void onElementData(byte[] data, int offset, int len, boolean completed) {

        System.out.println("OFF : "+offset);
        System.out.println("LEN : "+len);

        buffer.put(data , offset , len);

        if(completed){
            buffer.flip();
            callOnCompleted();
        }
    }

    @Override
    public void onElementData(ByteBuffer buffer, boolean completed) {
        buffer.put(buffer);

        if(completed){
            buffer.flip();
            callOnCompleted();
        }
    }

    private void callOnCompleted()
    {
        Consumer<ByteBuffer> listener = onCompleted;

        if(listener==null)
            return;

        listener.accept(buffer);
    }

    @Override
    public void refresh() {
        buffer.clear();
    }
}
