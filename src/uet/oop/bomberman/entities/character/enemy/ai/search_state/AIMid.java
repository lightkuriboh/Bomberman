package uet.oop.bomberman.entities.character.enemy.ai.search_state;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.bomb.Bomb;

import java.util.ArrayList;
import java.util.List;

public class AIMid {
    public int x, y;
    public ArrayList<BombInfo> listBomb = new ArrayList<>();
    public ArrayList<Integer> directions = new ArrayList<>();
    public boolean explode;

    public AIMid(AIMid aiMid) {
        this.x = aiMid.x;
        this.y = aiMid.y;
        this.directions.addAll(aiMid.directions);
        this.explode = false;
    }

    public AIMid(AIMid aiMid, int _x, int _y, int dir, Board board) {
        for (BombInfo bombInfo: aiMid.listBomb) {
            this.listBomb.add(new BombInfo(bombInfo, board));
        }
        this.directions.addAll(aiMid.directions);
        this.directions.add(dir);

        this.x = _x;
        this.y = _y;

        this.explode = false;
    }

    public AIMid(int x, int y, List<Bomb> bombs, Board board) {
        for (Bomb bomb: bombs) {
            this.listBomb.add(new BombInfo(bomb, board));
        }
        this.x = x;
        this.y = y;
        this.explode = false;

        for (BombInfo bombInfo: this.listBomb) {
            this.explode |= bombInfo.exploding;
        }
    }

    public AIMid update() {
        this.explode = false;
        for (int i = this.listBomb.size() - 1; i >= 0; i--) {
            this.listBomb.get(i).update();
            this.explode |= this.listBomb.get(i).exploding;
            if (this.listBomb.get(i).exploded) {
                this.listBomb.remove(i);
            }
        }
        return this;
    }
}
