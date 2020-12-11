package jttp.standard;

import jttp.api.HttpMethod;
import jttp.api.HttpVersion;
import jttp.api.RequestLine;
import jttp.api.RequestLineParser;
import jttp.api.exception.HttpParseException;
import jttp.api.exception.RequestLineParseException;

import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.*;

public class SRequestLineParser extends SElementReader implements RequestLineParser {

    private final ByteBuffer buffer;
    private HttpMethod method;
    private String route;
    private HttpVersion version;
    private RequestLine line;
    private int parts = 0;

    public SRequestLineParser(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    void onRead(byte b) throws HttpParseException {
        if(b== SP)
        {
            newPart();

            Assertion.ifTrue(parts>=3);
        }else {
            buffer.put(b);
        }
    }

    @Override
    void onElementParsed(boolean isCRLF) throws HttpParseException {
        //skip isCRLF
        newPart();
        Assertion.ifTrue(parts!=3);
    }


    private void newPart() throws HttpParseException
    {
        Assertion.ifTrue(++parts>3);
        buffer.flip();
        if(!buffer.hasRemaining())
            throw new RequestLineParseException("a empty part in request line has been detected");

        String part = new String(buffer.array() , 0 , buffer.remaining());
        buffer.clear();

        if(parts==1)
        {
            method = HttpMethod.getByName(part , tolerant.isEnable());
        }else if(parts==2)
        {
            route = part;
        }else if(parts==3)
        {
            version = HttpVersion.getByName(part , tolerant.isEnable());
            line = new RequestLine(method , route , version);
        }
    }

    @Override
    public SElementReader refresh() {
        method = null;
        route = null;
        version = null;
        line = null;
        parts = 0;
        buffer.clear();
        return super.refresh();
    }

    public RequestLine line() {
        return line;
    }
}
