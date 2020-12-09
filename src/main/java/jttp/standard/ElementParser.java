package jttp.standard;

import jttp.api.Reader;
import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.*;

public class ElementParser implements Reader {

    private final ByteBuffer buffer;
    private boolean crDetected = false;
    private boolean elementParsed = false;
    private boolean hasError;

    public ElementParser(ByteBuffer buffer , boolean duplicate) {
        this.buffer = duplicate?buffer.duplicate():buffer;
        this.buffer.clear();
    }

    public ElementParser(ByteBuffer buffer)
    {
        this(buffer , false);
    }

    @Override
    public int read(byte[] data) throws HttpParseException {
        Assertion.ifNull(data);
        Assertion.ifTrue(elementParsed);
        int i = 0;

        for(;i<data.length;i++)
        {
            if(data[i] == CR)
            {
                crDetected = true;
                buffer.put(CR);
            }else if(data[i] == LF && crDetected)
            {
                buffer.position(buffer.position()-1);
                elementParsed = true;
                buffer.flip();

                break;
            }else {
                buffer.put(data[i]);
            }
        }
        return i;
    }

    @Override
    public int read(byte[] data, int offset, int length) throws HttpParseException {
        Assertion.ifNull(data);
        Assertion.ifTrue(offset+length>data.length);
        Assertion.ifTrue(elementParsed);

        int i = offset;

        for(;i<length;i++)
        {
            if(data[i] == CR)
            {
                crDetected = true;
                buffer.put(CR);
            }else if(data[i] == LF && crDetected)
            {
                buffer.position(buffer.position()-1);
                elementParsed = true;
                buffer.flip();

                break;
            }else {
                buffer.put(data[i]);
            }
        }
        return i-offset;
    }

    @Override
    public int read(ByteBuffer buffer) throws HttpParseException {
        Assertion.ifNull(buffer);
        Assertion.ifTrue(elementParsed);
        int i = 0;

        while (buffer.hasRemaining())
        {
            byte data = buffer.get();
            if(data == CR)
            {
                crDetected = true;
                this.buffer.put(CR);
            }else if(data == LF && crDetected)
            {
                buffer.position(buffer.position()-1);
                elementParsed = true;
                this.buffer.flip();

                break;
            }else {
                this.buffer.put(data);
            }
        }
        return i;
    }

    public boolean isElementParsed() {
        return elementParsed;
    }

    public ByteBuffer buffer() {
        return buffer;
    }
}
