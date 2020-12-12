package jttp.standard;

import jttp.api.ByteParser;
import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

public abstract class ExceptionAdaptedElementParser implements ByteParser {

    private HttpParseException exception;
    private boolean isElementParsed = false;


    private void throwLastException() throws HttpParseException{
        if(exception!=null)
            throw exception;
    }

    @Override
    public final int read(byte[] buffer) throws HttpParseException {

        throwLastException();

        if(isElementParsed())return 0;

        try{
            return doRead(buffer , 0 , buffer.length);
        }catch (HttpParseException e)
        {
            exception = e;
            throw exception;
        }catch (Throwable e)
        {
            exception = new HttpParseException(e);
            throw exception;
        }
    }

    @Override
    public final int read(byte[] buffer, int offset, int length) throws HttpParseException {

        throwLastException();

        if(isElementParsed())return 0;

        try{
            return doRead(buffer , offset , length);
        }catch (HttpParseException e)
        {
            exception = e;
            throw exception;
        }catch (Throwable e)
        {
            exception = new HttpParseException(e);
            throw exception;
        }
    }

    @Override
    public final int read(ByteBuffer buffer) throws HttpParseException {

        throwLastException();

        if(isElementParsed())return 0;

        try{
            return doRead(buffer);
        }catch (HttpParseException e)
        {
            exception = e;
            throw exception;
        }catch (Throwable e)
        {
            exception = new HttpParseException(e);
            throw exception;
        }
    }

    @Override
    public final ByteParser refresh() {
        exception = null;
        isElementParsed = false;
        doRefresh();
        return this;
    }

    @Override
    public final boolean isElementParsed() {
        return isElementParsed;
    }

    void setElementParsed()
    {
        isElementParsed = true;
    }

    abstract int doRead(byte[] buffer ,int offset , int length) throws HttpParseException;
    abstract int doRead(ByteBuffer buffer)throws HttpParseException;
    abstract void doRefresh();
}
