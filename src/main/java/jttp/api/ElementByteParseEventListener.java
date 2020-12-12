package jttp.api;

import java.nio.ByteBuffer;

public interface ElementByteParseEventListener {

    void onElementData(byte[] data , int offset , int len , boolean completed);
    void onElementData(ByteBuffer buffer , boolean completed);

    void refresh();

}
