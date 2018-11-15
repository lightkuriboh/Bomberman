package uet.oop.bomberman.entities.character.enemy.ai.search_state;

import uet.oop.bomberman.entities.bomb.Bomb;

import java.util.ArrayList;
import java.util.List;

public class AIMid {
    public double x, y;
    public ArrayList<BombInfo> listBomb = new ArrayList<>();
    public ArrayList<Integer> directions = new ArrayList<>();
    public boolean explode;

    public AIMid(AIMid aiMid, double _x, double _y, int dir) {
        for (BombInfo bombInfo: aiMid.listBomb) {
            this.listBomb.add(new BombInfo(bombInfo));
        }
        this.directions.addAll(aiMid.directions);
        this.directions.add(dir);

        this.x = _x;
        this.y = _y;

        this.explode = false;
    }

    public AIMid(double x, double y, List<Bomb> bombs) {
        for (Bomb bomb: bombs) {
            this.listBomb.add(new BombInfo(bomb));
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
