package signal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerMove implements Serializable {
    protected List<Integer> dirState;

    public List<Integer> getDirState() {
        return dirState;
    }

    public PlayerMove(List<Integer> dirState) {
        this.dirState = new ArrayList<>();
        for(int i=0;i<dirState.size();i++) this.dirState.add(dirState.get(i));
    }

}
