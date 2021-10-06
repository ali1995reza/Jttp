package jttp.api;

import java.nio.ByteBuffer;

public interface ElementByteParseEventListener {

    ElementByteParseEventListener EMPTY = new ElementByteParseEventListener() {
        @Override
        public void onElementData(byte[] data, int offset, int len, boolean completed) {

        }

        @Override
        public void onElementData(ByteBuffer buffer, boolean completed) {

        }

        @Override
        public void refresh() {

        }
    };

    void onElementData(byte[] data, int offset, int len, boolean completed);

    void onElementData(ByteBuffer buffer, boolean completed);

    void refresh();

}
