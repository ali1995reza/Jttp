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
    private boolean firstByte = true;
    private boolean crDetected = false;

    private int lenOffset , offsetOffset = 0;



    private void validateHeaderName(byte b) throws HeaderParseException
    {
        if(isToken(b))
            return;

        throw new HeaderParseException("header name can just contain token bytes");
    }



    @Override
    public ByteHeaderParser setHeaderNameListener(ElementByteParseEventListener listener) {
        headerName = listener==null?ElementByteParseEventListener.EMPTY:listener;
        return this;
    }

    @Override
    public ByteHeaderParser setHeaderValueListener(ElementByteParseEventListener listener) {
        headerValue = listener==null?ElementByteParseEventListener.EMPTY:listener;
        return this;
    }

    private boolean readHeaderBytes(byte b)throws HeaderParseException
    {
        validateHeaderName(b);


        return b==DOUBLE_DOT;
    }

    private boolean readHeaderValueBytes(byte b) throws HttpParseException
    {

        if(b==LF){


            if(crDetected) {
                --lenOffset;
                return true;
            }

            throw LF_WITHOUT_CR_EXCEPTION;



        }else if(b==CR)
        {
            if(crDetected)
                throw new HeaderParseException("multiple CR detected");

            crDetected = true;

            --lenOffset;

        }else if(isCTL(b)) {
            throw new HeaderParseException("CTL byte in header value");
        }else{

            if(crDetected)
                throw new HeaderParseException("CR in header value");

            if(firstByte && spaceCharacter(b))
            {
                ++offsetOffset;
            }else {
                firstByte = false;
            }

        }

        return false;
    }



    @Override
    int doRead(byte[] buffer, int offset, int length) throws HttpParseException {
        int i = offset;
        int chunkOffset = offset;
        final int len = offset+length;
        for(;i<len;i++)
        {
            byte b = buffer[i];

            if(readingHeaderName)
            {
                if(readHeaderBytes(b))
                {

                    readingHeaderName = false;
                    headerName.onElementData(
                            buffer , chunkOffset+offsetOffset , i-chunkOffset+lenOffset , true
                    );
                    chunkOffset+=i-chunkOffset;

                    offsetOffset = 1;
                    lenOffset = 0;
                }else if(i-1==len)
                {
                    //so its chunk but not ened
                    headerName.onElementData(
                            buffer , chunkOffset+offsetOffset , i-chunkOffset+lenOffset , false
                    );
                }
            }else
            {

                if(readHeaderValueBytes(b)){

                    headerValue.onElementData(
                            buffer , chunkOffset+offsetOffset , i-chunkOffset+lenOffset , true
                    );
                    setElementParsed();
                }else if(i-1==len){

                    headerValue.onElementData(
                            buffer , chunkOffset+offsetOffset , i-chunkOffset+lenOffset , false
                    );
                }

            }

        }

        offsetOffset = 0;
        lenOffset = 0;

        return i-offset;
    }

    @Override
    int doRead(ByteBuffer buffer) throws HttpParseException {

        int offset = buffer.position();
        int i = offset;
        int chunkOffset = offset;
        final int len = offset + buffer.remaining();
        for (; i < len; i++) {
            byte b = buffer.get(i);

            if (readingHeaderName) {
                if (readHeaderBytes(b)) {

                    readingHeaderName = false;

                    ByteBuffer byteBuffer = buffer.duplicate();
                    byteBuffer.position(chunkOffset + offsetOffset);
                    byteBuffer.limit(byteBuffer.position()+i - chunkOffset + lenOffset);

                    headerName.onElementData(
                            byteBuffer, true
                    );
                    chunkOffset += i - chunkOffset;

                    offsetOffset = 1;
                    lenOffset = 0;
                } else if (i - 1 == len) {
                    //so its chunk but not ened
                    ByteBuffer byteBuffer = buffer.duplicate();
                    byteBuffer.position(chunkOffset + offsetOffset);
                    byteBuffer.limit(byteBuffer.position()+i - chunkOffset + lenOffset);

                    headerName.onElementData(
                            byteBuffer, false
                    );
                }
            } else {

                if (readHeaderValueBytes(b)) {

                    ByteBuffer byteBuffer = buffer.duplicate();
                    byteBuffer.position(chunkOffset + offsetOffset);
                    byteBuffer.limit(byteBuffer.position()+i - chunkOffset + lenOffset);

                    headerValue.onElementData(
                            byteBuffer, true
                    );
                    setElementParsed();
                } else if (i - 1 == len) {

                    ByteBuffer byteBuffer = buffer.duplicate();
                    byteBuffer.position(chunkOffset + offsetOffset);
                    byteBuffer.limit(byteBuffer.position()+i - chunkOffset + lenOffset);

                    headerValue.onElementData(
                            byteBuffer, false
                    );
                }

            }
        }


        return i - offset;
    }

    @Override
    void doRefresh() {
        readingHeaderName = true;
        firstByte = true;
        crDetected = false;
        offsetOffset = 0;
        lenOffset = 0;
    }
}
