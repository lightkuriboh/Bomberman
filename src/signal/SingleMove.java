package signal;

import java.io.Serializable;

public class SingleMove implements Serializable {
    private int dirState;

    public SingleMove(int dirState) {
        this.dirState = dirState;
    }

    public int getDirState() {
        return dirState;
    }
}
