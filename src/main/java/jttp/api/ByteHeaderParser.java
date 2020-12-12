package jttp.api;

public interface ByteHeaderParser extends ByteParser {


    ByteHeaderParser setHeaderNameListener(ElementByteParseEventListener listener);
    ByteHeaderParser setHeaderValueListener(ElementByteParseEventListener listener);


}
