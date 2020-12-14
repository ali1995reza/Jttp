package jttp.standard;

import jttp.api.ElementByteParseEventListener;
import jttp.api.exception.HttpParseException;
import jttp.api.exception.RequestLineParseException;


import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.*;

public class ByteMethodParser extends ExceptionAdaptedElementParser{

    private ElementByteParseEventListener method=  ElementByteParseEventListener.EMPTY;


    public ByteMethodParser setMethodListener(ElementByteParseEventListener method) {
        this.method = method==null?ElementByteParseEventListener.EMPTY:method;
        return this;
    }

    private boolean validate(byte b) throws RequestLineParseException
    {
        if(isALPHA(b))
            return false;
        else if(spaceCharacter(b))
        {
            setElementParsed();
            return true;
        }

        throw new RequestLineParseException("just alphabets are valid in Request Method");
    }

    @Override
    int doRead(byte[] buffer, int offset, int length) throws HttpParseException {

        int i=offset;
        for(;i<offset+length;i++)
        {
            if(validate(buffer[i])) {
                method.onElementData(buffer , offset , i-offset , true);
                i++;
                break;
            }
        }

        if(!isElementParsed())
        {
            method.onElementData(buffer , offset , i-offset , false);
        }

        return i-offset;
    }

    @Override
    int doRead(ByteBuffer buffer) throws HttpParseException {
        int offset = buffer.position();
        int i = offset;
        int len = offset+buffer.remaining();
        for(;i<len;i++)
        {
            if(validate(buffer.get(i)))
                break;
        }
        return i-offset;
    }

    @Override
    void doRefresh() {

    }
}
