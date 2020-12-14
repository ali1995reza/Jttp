package jttp.api;

public interface ByteRequestLineParser extends ByteParser {

    ByteRequestLineParser setMethodListener(ElementByteParseEventListener listener);
    ByteRequestLineParser setRouteListener(ElementByteParseEventListener listener);
    ByteRequestLineParser setVersionListener(ElementByteParseEventListener listener);
}
