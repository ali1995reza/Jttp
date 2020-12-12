package jttp.standard;

import jttp.api.ByteHeaderParser;
import jttp.api.ByteParser;
import jttp.api.ElementByteParseEventListener;
import jttp.api.exception.HeaderParseException;
import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.*;
import static jttp.standard.StaticExceptions.*;


public class SHeaderByteParser extends ExceptionAdaptedElementParser implements ByteHeaderParser {

    private ElementByteParseEventListener headerName;
    private ElementByteParseEventListener headerValue;
    private TolerantConfig tolerant = TolerantConfig.TOLERANT_ENABLE;

    private boolean readingHeaderName = true;
    private boolean firstByteOfHeaderValue = true;
    private boolean crDetected = false;



    private void validateHeaderName(byte b) throws HeaderParseException
    {
        if(isToken(b))
            return;

        throw new HeaderParseException("header name can just contain token bytes");
    }



    @Override
    public ByteHeaderParser setHeaderNameListener(ElementByteParseEventListener listener) {
        headerName = listener;
        return this;
    }

    @Override
    public ByteHeaderParser setHeaderValueListener(ElementByteParseEventListener listener) {
        headerValue = listener;
        return this;
    }

    private boolean readHeaderBytes(byte b)throws HeaderParseException
    {
        validateHeaderName(b);

        return b==DOUBLE_DOT;
    }

    private boolean readHeaderValueBytes(byte b) throws HttpParseException
    {

        if(b==CR){
            if(crDetected)
                throw new HeaderParseException("multiple CR detected");

            crDetected = true;

        }else if(b==LF)
        {
            if(crDetected)
                return true;

            throw LF_WITHOUT_CR_EXCEPTION;
        }else if(isCTL(b))
            throw new HeaderParseException("CTL byte in header value");

        return false;
    }



    @Override
    int doRead(byte[] buffer, int offset, int length) throws HttpParseException {
        int i = offset;
        int chunkOffset = offset;
        final int len = offset+length;
        int lenOffset = 0;
        for(;i<len;i++)
        {
            byte b = buffer[i];

            if(readingHeaderName)
            {
                if(readHeaderBytes(b))
                {
                    readingHeaderName = false;
                    headerName.onElementData(
                            buffer , chunkOffset , i-chunkOffset , true
                    );
                    chunkOffset+=i-chunkOffset+1;
                }else if(i-1==len)
                {
                    //so its chunk but not ened
                    headerName.onElementData(
                            buffer , chunkOffset , i-chunkOffset , false
                    );
                }
            }else
            {

                if(spaceCharacter(b) && firstByteOfHeaderValue)
                {
                    chunkOffset++;
                }else {
                    if(firstByteOfHeaderValue)
                        firstByteOfHeaderValue = false;

                    if(b==CR || b==LF)
                        lenOffset++;

                }

                if(readHeaderValueBytes(b)){
                    System.out.println(lenOffset);
                    headerValue.onElementData(
                            buffer , chunkOffset , i-chunkOffset-lenOffset , true
                    );
                    setElementParsed();
                }else if(i-1==len){
                    headerValue.onElementData(
                            buffer , chunkOffset , i-chunkOffset-lenOffset , false
                    );
                }

            }

        }

        return i-offset;
    }

    @Override
    int doRead(ByteBuffer buffer) throws HttpParseException {
        return 0;
    }

    @Override
    void doRefresh() {
        readingHeaderName = true;
        firstByteOfHeaderValue = true;
        crDetected = false;
    }
}
