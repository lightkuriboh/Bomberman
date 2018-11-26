package uet.oop.bomberman.entities.character.enemy;


import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.graphics.Sprite;

public class Kuriboh extends Enemy {

    public Kuriboh(int x, int y, Board board) {
        super(x, y, board, Sprite.minvo_dead, Game.getBomberSpeed() / 1.0, 200);

        _sprite = Sprite.balloom_left1;

        _ai = new AIMedium(board, _board.getBomber(), this, true, true);
//        _ai = new AILow();

        //_direction  = _ai.calculateDirection();
        _direction = 1;
    }

    @Override
    protected void chooseSprite() {
        switch(_direction) {
            case 0:
            case 1:
                if(_moving)
                    _sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, _animate, 60);
                else
                    _sprite = Sprite.doll_left1;
                break;
            case 2:
                _sprite = Sprite.oneal_left1;
            case 3:
                if(_moving)
                    _sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, _animate, 60);
                else
                    _sprite = Sprite.kondoria_left1;
                break;
        }
    }
}
