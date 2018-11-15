package uet.oop.bomberman.entities.character.enemy.ai.search_state;

import uet.oop.bomberman.entities.bomb.Bomb;

public class BombInfo {
    public int x, y;
    public int timeToExplode;
    public int radius;
    public boolean exploding;
    public boolean exploded;
    public int[] segmentLenght = new int[4];

    public BombInfo(BombInfo bombInfo) {
        this.x = bombInfo.x;
        this.y = bombInfo.y;
        this.timeToExplode = bombInfo.timeToExplode;
        this.radius = bombInfo.radius;
        this.exploded = false;
    }

    public BombInfo(Bomb bomb) {
        this.x = (int)bomb.getX();
        this.y = (int)bomb.getY();
        this.timeToExplode = (int)bomb.get_timeToExplode();
        this.radius = bomb.get_radius();
        this.exploding = (this.timeToExplode == 0);
        for (int i = 0; i < 4; i++) {
            this.segmentLenght[i] = bomb.getLengthFlameAt(i);
        }
    }

    public void update() {
        this.timeToExplode--;
        this.exploding = (this.timeToExplode == 0);
        this.exploded = (this.timeToExplode < 0);
    }
}
