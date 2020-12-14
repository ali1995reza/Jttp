package jttp.standard;

import jttp.api.ElementByteParseEventListener;
import jttp.api.exception.HttpParseException;
import jttp.api.exception.RequestLineParseException;

import java.nio.ByteBuffer;

public class ByteVersionParser extends ExceptionAdaptedElementParser {

    private final static byte[] UPPERCASE =
            "HTTP/1.1\r\n".getBytes();
    private final static byte[] LOWERCASE =
            "http/1.1\r\n".getBytes();

    private final static byte SIZE = 10;
    private final static byte ALPHA_SIZE = 8;

    private ElementByteParseEventListener version = ElementByteParseEventListener.EMPTY;
    private byte index = 0;

    private boolean validate(byte b) throws RequestLineParseException
    {
        if(b!=UPPERCASE[index] && b!=LOWERCASE[index])
            throw new RequestLineParseException("Http Version Parse Error");

        return ++index==SIZE;

    }


    private int currentOffset()
    {
        if(index<ALPHA_SIZE)
            return 0;

        return index-ALPHA_SIZE;
    }

    public ByteVersionParser setVersion(ElementByteParseEventListener version) {
        this.version = version==null?ElementByteParseEventListener.EMPTY:version;
        return this;
    }

    @Override
    int doRead(byte[] buffer, int offset, int length) throws HttpParseException {
        int i=offset;
        for(;i<offset+length;i++)
        {
            if(validate(buffer[i])) {

                i++;

                int len = i-offset-currentOffset();
                len=len<0?0:len;

                version.onElementData(buffer , offset , len , true);

                break;
            }
        }

        if(!isElementParsed())
        {
            int len = i-offset-currentOffset();
            if(len>0)
                version.onElementData(buffer , offset , len , false);
        }

        return i-offset;
    }

    @Override
    int doRead(ByteBuffer buffer) throws HttpParseException {
        return 0;
    }

    @Override
    void doRefresh() {
        index = 0;
    }
}
