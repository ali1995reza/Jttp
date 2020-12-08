package jttp.api;

import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

public interface Reader {

    int read(byte[] data) throws HttpParseException;
    int read(byte[] data , int offset , int length) throws HttpParseException;
    int read(ByteBuffer byteBuffer) throws HttpParseException;
}
