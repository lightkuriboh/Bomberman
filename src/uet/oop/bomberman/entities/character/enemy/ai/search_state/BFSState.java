package uet.oop.bomberman.entities.character.enemy.ai.search_state;

public class BFSState {
    public int x, y, path;
    public BFSState(int _x, int _y, int _path) {
        this.x = _x;
        this.y = _y;
        this.path = _path;
    }
}
