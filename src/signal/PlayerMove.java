package signal;

import java.io.Serializable;
import java.util.List;

public class PlayerMove implements Serializable {
    protected List<Integer> dirState;

    public List<Integer> getDirState() {
        return dirState;
    }

}
