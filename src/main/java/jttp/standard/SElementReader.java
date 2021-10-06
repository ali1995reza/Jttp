package jttp.standard;

import jttp.api.ElementReader;
import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.*;
import static jttp.standard.StaticExceptions.*;

public abstract class SElementReader implements ElementReader {

    protected TolerantConfig tolerant;
    private boolean crDetected = false;
    private boolean elementParsed = false;
    private boolean spaceDetected = false;
    private boolean readAnyOtherBytesExpectCRLF = false;
    private int totalRedundantBytes = 0;
    private HttpParseException lastException;

    public SElementReader(TolerantConfig tolerant) {
        setTolerant(tolerant);
    }

    public SElementReader() {
        this(TolerantConfig.TOLERANT_ENABLE);
    }

    public SElementReader setTolerant(TolerantConfig tolerant) {
        Assertion.ifNull(tolerant);
        this.tolerant = tolerant;
        return this;
    }

    private final void increaseTolerantBytes() throws HttpParseException {
        if (!tolerant.isEnable() ||
                ++totalRedundantBytes > tolerant.totalTolerantBytes())
            throw MAXIMUM_TOLERANT_BYTES_REACHED;
    }


    private final void validate(byte b) throws HttpParseException {
        if (isCHAR(b))
            return;

        throw INVALID_CHARACTER;
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
                validate(data);
                if (data == CR) {
                    crDetected = true;
                } else if (data == LF) {
                    i++;
                    if (crDetected || tolerant.isEnable()) {
                        onElementParsed(!readAnyOtherBytesExpectCRLF);
                        elementParsed = true;
                        break;
                    } else {
                        throw LF_WITHOUT_CR_EXCEPTION;
                    }

                } else {
                    if (!readAnyOtherBytesExpectCRLF)
                        readAnyOtherBytesExpectCRLF = true;
                    if (crDetected) {
                        throw CR_IN_LINE_EXCEPTION;
                    }

                    if (spaceCharacter(data)) {

                        if (spaceDetected)
                            increaseTolerantBytes();
                        else {
                            spaceDetected = true;
                            onRead(SP);
                        }
                    } else {
                        if (spaceDetected)
                            spaceDetected = false;
                        onRead(data);
                    }
                }
            }
            return i;
        } catch (HttpParseException e) {
            lastException = e;
            throw lastException;
        } catch (Throwable e) {
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
                validate(data);
                if (data == CR) {
                    crDetected = true;
                } else if (data == LF) {
                    i++;
                    if (crDetected || tolerant.isEnable()) {
                        onElementParsed(!readAnyOtherBytesExpectCRLF);
                        elementParsed = true;
                        break;
                    } else {
                        throw LF_WITHOUT_CR_EXCEPTION;
                    }

                } else {
                    if (!readAnyOtherBytesExpectCRLF)
                        readAnyOtherBytesExpectCRLF = true;
                    if (crDetected) {
                        throw CR_IN_LINE_EXCEPTION;
                    }

                    if (spaceCharacter(data)) {
                        if (spaceDetected)
                            increaseTolerantBytes();
                        else {
                            spaceDetected = true;
                            onRead(SP);
                        }
                    } else {
                        if (spaceDetected)
                            spaceDetected = false;
                        onRead(data);
                    }
                }
            }
            return i - offset;
        } catch (HttpParseException e) {
            lastException = e;
            throw lastException;
        } catch (Throwable e) {
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
                validate(data);
                i++;
                if (data == CR) {
                    crDetected = true;
                } else if (data == LF) {
                    if (crDetected || tolerant.isEnable()) {
                        onElementParsed(!readAnyOtherBytesExpectCRLF);
                        elementParsed = true;
                        break;
                    } else {
                        throw LF_WITHOUT_CR_EXCEPTION;
                    }

                } else {
                    if (!readAnyOtherBytesExpectCRLF)
                        readAnyOtherBytesExpectCRLF = true;

                    if (crDetected) {
                        throw CR_IN_LINE_EXCEPTION;
                    }

                    if (spaceCharacter(data)) {
                        if (spaceDetected)
                            increaseTolerantBytes();
                        else {
                            spaceDetected = true;
                            onRead(SP);
                        }
                    } else {
                        if (spaceDetected)
                            spaceDetected = false;
                        onRead(data);
                    }
                }
            }

            return i;
        } catch (HttpParseException e) {
            lastException = e;
            throw lastException;
        } catch (Throwable e) {
            lastException = new HttpParseException(e);
            throw lastException;
        }
    }

    private final void throwIfHadException() throws HttpParseException {
        if (lastException != null)
            throw lastException;
    }

    @Override
    public boolean isElementParsed() {
        return elementParsed;
    }

    @Override
    public boolean isElementCRLF() {
        return elementParsed && !readAnyOtherBytesExpectCRLF;
    }

    public SElementReader refresh() {
        elementParsed = false;
        crDetected = false;
        spaceDetected = true;
        lastException = null;
        readAnyOtherBytesExpectCRLF = false;
        totalRedundantBytes = 0;

        return this;
    }

    abstract void onRead(byte b) throws HttpParseException;

    abstract void onElementParsed(boolean isCRLF) throws HttpParseException;
}
