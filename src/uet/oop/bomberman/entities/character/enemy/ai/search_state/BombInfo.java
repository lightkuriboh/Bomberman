package uet.oop.bomberman.entities.character.enemy.ai.search_state;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;

public class BombInfo {
    public int x, y;
    public int timeToExplode;
    public int radius;
    public boolean exploding;
    public boolean exploded;
    public int[] segmentLenght = new int[4];
    public Board board;

    public BombInfo(BombInfo bombInfo, Board _board) {
        this.x = bombInfo.x;
        this.y = bombInfo.y;
        this.timeToExplode = bombInfo.timeToExplode;
        this.radius = bombInfo.radius;
        this.exploded = false;
        this.board = _board;
        for (int i = 0; i < 4; i++) {
            this.segmentLenght[i] = bombInfo.segmentLenght[i];
        }
    }

    public BombInfo(Bomb bomb, Board _board) {
        this.x = (int)bomb.getX();
        this.y = (int)bomb.getY();
        this.timeToExplode = (int)bomb.get_timeToExplode();
        this.radius = bomb.get_radius();
        this.exploding = (this.timeToExplode <= 0);
        this.board = _board;
        for (int i = 0; i < bomb.getFlameNumber(); i++) {
            this.segmentLenght[i] = new Flame(this.x, this.y, i, this.radius, _board).calculatePermitedDistance();
        }

    }

    public void update() {
        this.timeToExplode -= 32;
        this.exploding = (this.timeToExplode <= 36);
        this.exploded = (this.timeToExplode < -32);
    }
}
