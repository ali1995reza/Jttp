package jttp.standard;

public class HttpProtocolConstant {

    public final static byte CR = (byte)'\r'; //13
    public final static byte LF = (byte)'\n'; //10
    public final static byte SPACE = (byte)' '; //32
    public final static byte TAB = (byte)'\t';
    public final static byte DOUBLE_DOT = (byte)':';

    public final static String CRLF = "\r\n";


    public final static boolean spaceCharacter(byte b)
    {
        return b==SPACE || b==TAB;
    }

}
