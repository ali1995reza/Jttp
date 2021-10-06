package jttp.standard;

import jttp.api.exception.HttpParseException;

import static jttp.standard.HttpProtocolConstant.hexValueAsDecimal;


public class ChunkSizeParser extends SElementReader {

    private long len = 0;


    private byte validate(byte b) throws HttpParseException {
        byte s = hexValueAsDecimal(b);

        if (b < 0)
            throw new HttpParseException("chunk size error");

        return s;
    }


    @Override
    void onRead(byte b) throws HttpParseException {
        byte value = validate(b);
        len *= 16;
        len += value;
    }

    @Override
    void onElementParsed(boolean isCRLF) throws HttpParseException {
        //its ok !
    }

    public long chunkSize() {
        Assertion.ifTrue(!isElementParsed());
        return len;
    }


    @Override
    public SElementReader refresh() {
        len = 0;
        return super.refresh();
    }
}
