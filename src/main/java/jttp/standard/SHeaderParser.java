package jttp.standard;

import jttp.api.HeaderParser;
import jttp.api.HttpHeader;
import jttp.api.exception.HeaderParseException;
import jttp.api.exception.HttpParseException;
import jttp.api.exception.RequestLineParseException;

import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.*;

public class SHeaderParser extends ElementReader implements HeaderParser {

    private final ByteBuffer buffer;
    private String headerName;
    private String headerValue;
    private HttpHeader header;
    private int parts = 0;

    public SHeaderParser(ByteBuffer buffer) {
        this.buffer = buffer;
    }


    private final void newPart() throws HttpParseException
    {
        Assertion.ifTrue(++parts>3);
        buffer.flip();
        if(!buffer.hasRemaining())
            throw new HeaderParseException("a empty part in request line has been detected");

        String part = new String(buffer.array() , 0 , buffer.remaining());
        buffer.clear();

        if(parts==1)
        {
            headerName = part;
        }else if(parts==2)
        {
            headerValue = part;
            header = new HttpHeader(
                    headerName ,
                    headerValue
            );
        }

    }


    @Override
    void onRead(byte b) throws HttpParseException {
        if(b==DOUBLE_DOT)
        {
            newPart();
            Assertion.ifTrue(parts>=2);
        }else if(b==SPACE && buffer.position()==0){
        }else {
            buffer.put(b);
        }
    }


    @Override
    void onElementParsed() throws HttpParseException {
        newPart();

        Assertion.ifTrue(parts!=2);
    }

    @Override
    public ElementReader refresh() {
        parts = 0;
        header = null;
        headerName = null;
        headerValue = null;
        return super.refresh();
    }

    @Override
    public HttpHeader header() {
        return header;
    }
}
