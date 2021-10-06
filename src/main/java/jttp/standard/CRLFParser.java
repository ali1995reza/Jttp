package jttp.standard;

import jttp.api.exception.HttpParseException;

public class CRLFParser extends SElementReader {

    @Override
    void onRead(byte b) throws HttpParseException {
        throw new HttpParseException("none CRLF byte detected");
    }

    @Override
    void onElementParsed(boolean isCRLF) throws HttpParseException {
        if (!isCRLF)
            throw new HttpParseException("it seems its not just CRLF");
    }
}
