package jttp.standard;

public class Assertion {

    public final static class  AssertionException extends RuntimeException {
        private AssertionException(String e)
        {
            super(e);
        }

        private AssertionException(Throwable e)
        {
            super(e);
        }

        private AssertionException()
        {
            super();
        }

    }


    private final static String IF_NULL_DEF_MSG =
            "object is null";

    private final static String IF_TRUE_DEF_MSG =
            "condition is true";

    public final static void ifNull(Object o , String msg)
    {
        if(o==null)
            throw new AssertionException(msg);
    }

    public final static void ifNull(Object o)
    {
        ifNull(o , IF_NULL_DEF_MSG);
    }

    public final static void ifTrue(boolean cond , String  msg)
    {
        if(cond)
            throw new AssertionException(msg);
    }

    public final static void ifTrue(boolean cond)
    {
        ifTrue(cond , IF_TRUE_DEF_MSG);
    }

    public final static <T extends Throwable> void throwIf(boolean cond , T throwable) throws T {
        if(cond)
            throw throwable;
    }

    public final static <T extends Throwable> void throwIfNot(boolean cond , T throwable) throws T {
        if(!cond)
            throw throwable;
    }
}
