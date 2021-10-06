package jttp.standard;

public class TolerantConfig {

    public final static TolerantConfig TOLERANT_ENABLE =
            new TolerantConfig(100, true);

    public final static TolerantConfig TOLERANT_DISABLE =
            new TolerantConfig(0, false);

    private final int totalTolerantBytes;
    private final boolean enable;

    public TolerantConfig(int totalTolerantBytes, boolean enable) {
        this.totalTolerantBytes = totalTolerantBytes;
        this.enable = enable;
    }


    public boolean isEnable() {
        return enable;
    }

    public int totalTolerantBytes() {
        return totalTolerantBytes;
    }
}
