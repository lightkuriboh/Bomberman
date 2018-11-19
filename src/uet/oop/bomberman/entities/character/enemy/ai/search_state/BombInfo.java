package uet.oop.bomberman.entities.character.enemy.ai.search_state;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class BombInfo {
    public int x, y;
    public int timeToExplode;
    public int radius;
    public boolean exploding;
    public boolean exploded;
    public Board board;
    private Enemy e;

    public BombInfo(BombInfo bombInfo, Board _board, Enemy _e) {
        this.x = bombInfo.x;
        this.y = bombInfo.y;
        this.timeToExplode = bombInfo.timeToExplode;
        this.radius = bombInfo.radius;
        this.exploded = false;
        this.board = _board;
        this.e = _e;
    }

    public BombInfo(Bomb bomb, Board _board, Enemy _e) {
        this.x = (int)bomb.getX();
        this.y = (int)bomb.getY();
        this.timeToExplode = (int)bomb.get_timeToExplode();
        this.radius = bomb.get_radius();
        this.exploding = (this.timeToExplode <= 0);
        this.board = _board;
        this.e = _e;
    }

    public void update() {
        this.timeToExplode -= 32;
        this.exploding = (this.timeToExplode <= (double)36 * (double)Game.getBomberSpeed() / (double)this.e.get_speed());
        this.exploded = (this.timeToExplode < -31);
    }
}
