package jttp.standard;

import jttp.api.exception.HeaderParseException;
import jttp.api.exception.HttpParseException;

public class StaticExceptions {


    public final static HttpParseException CR_IN_LINE_EXCEPTION =
            new HeaderParseException("CR character founded in middle of line");

    public final static HttpParseException LF_WITHOUT_CR_EXCEPTION =
            new HeaderParseException("LF character detected , but not followed CR character");


    public final static HttpParseException MAXIMUM_TOLERANT_BYTES_REACHED =
            new HeaderParseException("Maximum tolerant redundant bytes reached");

    public final static HttpParseException INVALID_CHARACTER =
            new HeaderParseException("Invalid character , character values must valid in ASCII");
}
