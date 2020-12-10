package jttp.api;

import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

public interface Reader {

    int read(byte[] buffer) throws HttpParseException;
    int read(byte[] buffer , int offset , int length) throws HttpParseException;
    int read(ByteBuffer buffer) throws HttpParseException;
}
