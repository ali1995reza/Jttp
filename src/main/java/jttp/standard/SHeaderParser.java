package jttp.standard;

import jttp.api.HeaderParser;
import jttp.api.HttpHeader;
import jttp.api.exception.HeaderParseException;
import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.DOUBLE_DOT;
import static jttp.standard.HttpProtocolConstant.SP;

public class SHeaderParser extends SElementReader implements HeaderParser {

    private final ByteBuffer buffer;
    private String headerName;
    private String headerValue;
    private HttpHeader header;
    private int parts = 0;

    public SHeaderParser(ByteBuffer buffer) {
        this.buffer = buffer;
    }


    private final void newPart() throws HttpParseException {
        Assertion.ifTrue(++parts > 3);
        buffer.flip();
        if (!buffer.hasRemaining())
            throw new HeaderParseException("a empty part in header has been detected");

        String part = new String(buffer.array(), 0, buffer.remaining());
        buffer.clear();

        if (parts == 1) {
            headerName = part;
        } else if (parts == 2) {
            headerValue = part;
            header = new HttpHeader(
                    headerName,
                    headerValue
            );
        }

    }


    @Override
    void onRead(byte b) throws HttpParseException {
        if (b == DOUBLE_DOT && parts == 0) {
            newPart();
            Assertion.ifTrue(parts >= 2);
        } else if (b == SP && buffer.position() == 0) {
        } else {
            buffer.put(b);
        }
    }


    @Override
    void onElementParsed(boolean isCRLF) throws HttpParseException {

        if (isCRLF)
            return;

        newPart();

        Assertion.ifTrue(parts != 2);
    }

    @Override
    public SElementReader refresh() {
        parts = 0;
        header = null;
        headerName = null;
        headerValue = null;
        buffer.clear();
        return super.refresh();
    }

    @Override
    public HttpHeader header() {
        return header;
    }
}
