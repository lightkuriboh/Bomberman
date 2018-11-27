package signal;

import java.io.Serializable;

public class AssignId implements Serializable {
    private int id;

    public AssignId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
