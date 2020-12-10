package jttp.standard;

import jttp.api.Reader;
import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.*;
import static jttp.standard.StaticExceptions.*;

public abstract class ElementReader implements Reader {

    private boolean crDetected = false;
    private boolean elementParsed = false;
    private boolean spaceDetected = true; //start of line is true and ok !
    private int totalRedundantBytes = 0;
    protected TolerantConfig tolerant;
    private HttpParseException lastException;

    public ElementReader(TolerantConfig tolerant) {
        setTolerant(tolerant);
    }

    public ElementReader()
    {
        this(TolerantConfig.TOLERANT_ENABLE);
    }

    public ElementReader setTolerant(TolerantConfig tolerant) {
        Assertion.ifNull(tolerant);
        this.tolerant = tolerant;
        return this;
    }

    private final void increaseTolerantBytes()throws HttpParseException
    {
        if(!tolerant.isEnable() ||
                ++totalRedundantBytes>tolerant.totalTolerantBytes())
            throw MAXIMUM_TOLERANT_BYTES_REACHED;
    }

    @Override
    public int read(byte[] buffer) throws HttpParseException {
        Assertion.ifNull(buffer);
        Assertion.ifTrue(elementParsed);
        throwIfHadException();

        try {
            int i = 0;
            for (; i < buffer.length; i++) {
                byte data = buffer[i];
                if (data == CR) {
                    crDetected = true;
                } else if (data == LF) {
                    i++;
                    if(crDetected || tolerant.isEnable()) {
                        onElementParsed();
                        elementParsed = true;
                        break;
                    }else {
                        throw LF_WITHOUT_CR_EXCEPTION;
                    }

                } else {
                    if(crDetected)
                    {
                        throw CR_IN_LINE_EXCEPTION;
                    }

                    if(spaceCharacter(data))
                    {

                        if(spaceDetected)
                            increaseTolerantBytes();
                        else {
                            spaceDetected = true;
                            onRead(SPACE);
                        }
                    }else {
                        if(spaceDetected)
                            spaceDetected = false;
                        onRead(data);
                    }
                }
            }
            return i;
        }catch (HttpParseException e)
        {
            lastException = e;
            throw lastException;
        }catch (Throwable e)
        {
            lastException = new HttpParseException(e);
            throw lastException;
        }
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws HttpParseException {
        Assertion.ifNull(buffer);
        Assertion.ifTrue(elementParsed);
        throwIfHadException();

        try {
            int i = offset;

            for (; i < offset + length; i++) {
                byte data = buffer[i];
                if (data == CR) {
                    crDetected = true;
                } else if (data == LF) {
                    i++;
                    if(crDetected || tolerant.isEnable()) {
                        onElementParsed();
                        elementParsed = true;
                        break;
                    }else {
                        throw LF_WITHOUT_CR_EXCEPTION;
                    }

                } else {
                    if (crDetected) {
                        throw CR_IN_LINE_EXCEPTION;
                    }

                    if(spaceCharacter(data))
                    {
                        if(spaceDetected)
                            increaseTolerantBytes();
                        else {
                            spaceDetected = true;
                            onRead(SPACE);
                        }
                    }else {
                        if(spaceDetected)
                            spaceDetected = false;
                        onRead(data);
                    }
                }
            }
            return i - offset;
        }catch (HttpParseException e)
        {
            lastException = e;
            throw lastException;
        }catch (Throwable e)
        {
            lastException = new HttpParseException(e);
            throw lastException;
        }
    }

    @Override
    public int read(ByteBuffer buffer) throws HttpParseException {
        Assertion.ifNull(buffer);
        Assertion.ifTrue(elementParsed);
        throwIfHadException();
        try {
            int i = 0;

            while (buffer.hasRemaining()) {
                byte data = buffer.get();
                i++;
                if (data == CR) {
                    crDetected = true;
                } else if (data == LF) {
                    if(crDetected || tolerant.isEnable()) {
                        onElementParsed();
                        elementParsed = true;
                        break;
                    }else {
                        throw LF_WITHOUT_CR_EXCEPTION;
                    }

                } else {
                    if (crDetected) {
                        throw CR_IN_LINE_EXCEPTION;
                    }

                    if(spaceCharacter(data))
                    {
                        if(spaceDetected)
                            increaseTolerantBytes();
                        else {
                            spaceDetected = true;
                            onRead(SPACE);
                        }
                    }else {
                        if(spaceDetected)
                            spaceDetected = false;
                        onRead(data);
                    }
                }
            }

            return i;
        }catch (HttpParseException e)
        {
            lastException = e;
            throw lastException;
        }catch (Throwable e)
        {
            lastException = new HttpParseException(e);
            throw lastException;
        }
    }

    private final void throwIfHadException() throws HttpParseException
    {
        if(lastException!=null)
            throw lastException;
    }

    public boolean isElementParsed() {
        return elementParsed;
    }

    public ElementReader refresh()
    {
        elementParsed = false;
        crDetected = false;
        spaceDetected = true;
        lastException = null;
        totalRedundantBytes = 0;

        return this;
    }

    abstract void onRead(byte b) throws HttpParseException;
    abstract void onElementParsed() throws HttpParseException;
}
