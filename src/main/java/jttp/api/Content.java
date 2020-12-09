package jttp.api;

import java.nio.ByteBuffer;

public class Content {

    private final long size;
    private final ByteBuffer data;
    private final boolean isLastPart;

    public Content(long size, ByteBuffer data, boolean isLastPart) {
        this.size = size;
        this.data = data;
        this.isLastPart = isLastPart;
    }

    public ByteBuffer data() {
        return data;
    }

    public long size() {
        return size;
    }

    public boolean isLastPart() {
        return isLastPart;
    }
}
