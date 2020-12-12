package jttp.standard;

public class HttpProtocolConstant {


    private static byte[] hexValueMap; //0 will skip !

    private final static void initMap()
    {
        byte[] map = new byte[128];

        for(int i=0;i<map.length;i++)
        {

            if(i=='0')
                map[i] = 0;
            else if(i=='1')
                map[i] = 1;
            else if(i=='2')
                map[i] = 2;
            else if(i=='3')
                map[i] = 3;
            else if(i=='4')
                map[i] = 4;
            else if(i=='5')
                map[i] = 5;
            else if(i=='6')
                map[i] = 6;
            else if(i=='7')
                map[i] = 7;
            else if(i=='8')
                map[i] = 8;
            else if(i=='9')
                map[i] = 9;
            else if(i=='a' || i=='A')
                map[i] = 10;
            else if(i=='b' || i=='B')
                map[i] = 11;
            else if(i=='c' || i=='C')
                map[i] = 12;
            else if(i=='d' || i=='D')
                map[i] = 13;
            else if(i=='e' || i=='E')
                map[i] = 14;
            else if(i=='d' || i=='F')
                map[i] = 15;
            else
                map[i] = -1;

        }

        hexValueMap = map;
    }

    static {
        initMap();
    }


    public final static byte CR = '\r'; //13
    public final static byte LF = '\n'; //10
    public final static byte SP = ' '; //32
    public final static byte HT = '\t';
    public final static byte DEL = 127;
    public final static byte QUOTE_MARK = '\"';
    public final static byte DOUBLE_DOT = (byte)':';

    public final static String CRLF = "\r\n";


    public final static boolean isCHAR(byte b)
    {
        return b>=START_CHAR && b<=END_CHAR;
    }

    public final static boolean spaceCharacter(byte b)
    {
        return b== SP || b== HT;
    }

    public final static boolean isCTL(byte b)
    {
        return (b>=START_CTL && b<=END_CTL) || b==DEL;
    }

    public final static boolean isNotCTL(byte b)
    {
        return b>END_CTL && b!=DEL;
    }

    public final static boolean isDigit(byte b)
    {
        return b>=START_DIGIT && b<=END_DIGIT;
    }

    public final static boolean isUPALPHA(byte b)
    {
        return b>=START_UPALPHA && b<=END_UPALPHA;
    }

    public final static boolean isLOALPHA(byte b)
    {
        return b>=START_LOALPHA && b<=END_LOALPHA;
    }

    public final static boolean isALPHA(byte b)
    {
        return isUPALPHA(b) || isLOALPHA(b);
    }

    public final static boolean isHex(byte b)
    {
        return (b>=START_HEX_UP && b<=END_HEX_UP) ||
               (b>=START_HEX_LO && b<=START_HEX_LO) ||
               (b>=START_DIGIT && b<=END_DIGIT);
    }


    public final static boolean isToken(byte b)
    {
        //b>32 sp
        return b>32 && b!=DEL;
    }

    public final static byte hexValueAsDecimal(byte b)
    {
        return hexValueMap[b];
    }





    //private
    private final static byte START_CHAR = 0;
    private final static byte END_CHAR = 127;

    private final static byte START_DIGIT = '0';
    private final static byte END_DIGIT = '9';

    private final static byte START_CTL = 0;
    private final static byte END_CTL = 31;

    private final static byte START_UPALPHA = 'A';
    private final static byte END_UPALPHA = 'Z';

    private final static byte START_LOALPHA = 'a';
    private final static byte END_LOALPHA = 'z';


    private final static byte START_HEX_UP = 'A';
    private final static byte END_HEX_UP = 'F';
    private final static byte START_HEX_LO = 'a';
    private final static byte END_HEX_LO = 'f';
}
