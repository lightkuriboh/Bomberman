package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.sound.SoundPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Bomber extends Character {

    private List<Bomb> _bombs;
    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;
    protected  int _bombRad;
    protected  double _speed;
    protected  int _bombRate;
    protected String _name;


    public static int DIR_LEFT = 1<<3;
    public static int DIR_RIGHT = 1<<1;
    public static int DIR_UP = 1<<0;
    public static int DIR_DOWN = 1<<2;
    public static int DIR_BOMB = 1<<4;
    protected int dirState;

    public Bomber(int x, int y, Board board, String name) {
        super(x, y, board);
        _bombs = new ArrayList<>();
        _sprite = Sprite.player_right;
        _bombRad = Game.getBombRadius();
        _speed = Game.getBomberSpeed();
        _bombRate = Game.getBombRate();
        _name = name;
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        int xx=Coordinates.pixelToTile(_x);
        int yy=(Coordinates.pixelToTile(_y)-1);
        checkCollide(xx,yy);
        checkCollide(xx+1,yy);
        checkCollide(xx,yy+1);
        checkCollide(xx+1,yy+1);

        checkEndGame();

        checkItem();

        //System.out.println(_speed);
        detectPlaceBomb();
    }

    protected void checkEndGame() {
        int xx = Coordinates.pixelToTile(_x+_sprite.get_realWidth()/2-1);
        int yy = Coordinates.pixelToTile(_y+_sprite.get_realHeight()/2-1)-1;
        int pos = yy * _board.getWidth() + xx;
        if (_board._entities[pos] instanceof Portal) {
                if (_board.detectNoEnemies()) {
                    SoundPlayer.playCheerSound();
                    _board.nextLevel();
                }
        }

    }

    protected void checkItem() {
        int xx = Coordinates.pixelToTile(_x+_sprite.get_realWidth()/2-1);
        int yy = Coordinates.pixelToTile(_y+_sprite.get_realHeight()/2-1)-1;
        int pos = yy * _board.getWidth() + xx;
        if (_board._entities[pos] instanceof LayeredEntity) {
            LayeredEntity cur = (LayeredEntity) _board._entities[pos];
            while (cur.getTopEntity() instanceof Item ) {
                cur.getTopEntity().collide(this);
                cur.update();

            }
        }
    }

    protected  void checkCollide(int x,int y) {

        LayeredEntity character = _board.getCharacterAtExcluding(x, y, null);
        if (character != null) {
            while (!character.isEmpty()) {
                Character character1 = (Character) character.getTopEntity();
                this.collide(character1);
                character.removeTop();
            }
        }

    }

    public void inBombRad(int i) {
        _bombRad+=i;
    }

    public void inBombRate(int i) {
        _bombRate+=i;
    }

    public void inSpeed(double i) {
        _speed+=i;
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();
        calculateYOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);

        if (_name.equals("player1")) Screen.setOffset(xScroll, 0);

//        System.out.println(Integer.toString(xScroll));
    }

    public void calculateYOffset() {
        int yScroll = Screen.calculateYOffset(_board, this);
        if (_name.equals("player1")) Screen.setOffset(Screen.xOffset, yScroll);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {

        // TODO: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // TODO:  Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng thời gian quá ngắn
        // TODO: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs về 0
        if ((dirState & DIR_BOMB) == DIR_BOMB && _bombRate > 0 && _timeBetweenPutBombs < -15) {
            int xx=Coordinates.pixelToTile(_x+_sprite.get_realWidth()/2-1);
            int yy=(Coordinates.pixelToTile(_y+_sprite.get_realHeight()/2-1)-1);
            placeBomb(xx, yy);
            this.inBombRate(-1);
            _timeBetweenPutBombs = 0;
        }


    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)

        Bomb bomb = new Bomb(x, y, _board, _bombRad);
        _bombs.add(bomb);
        _board.addBomb(bomb);
        _board.addLayeredEntity( y*(int)_board.getWidth()+x,bomb);
    }

    @Override
    public void kill() {
        if (!_alive) return;
        _alive = false;
        SoundPlayer.playDieSound();
        //_removed = true;
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            _board.endGame();
        }
    }

    public int getDirState() {
        return dirState;
    }

    public void setDirState(int dirState) {
        this.dirState = dirState;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    @Override
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu điều khiển hướng đi từ _input và gọi move() để thực hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        double newX=this._x;
        double newY=this._y;
        this._moving=false;
        if ((dirState & DIR_UP) == DIR_UP) {
            newY-=_speed;
            this._moving=true;
        }
        if ((dirState & DIR_DOWN) == DIR_DOWN) {
            newY+=_speed;
            this._moving=true;
        }
        if ((dirState & DIR_LEFT) == DIR_LEFT) {
            newX-=_speed;
            this._moving=true;
        }
        if ((dirState & DIR_RIGHT) == DIR_RIGHT) {
            newX+=_speed;
            this._moving=true;
        }
        if (Coordinates.tileToPixel(Coordinates.pixelToTile(newX)+1)-newX<_speed)
            newX=Coordinates.tileToPixel(Coordinates.pixelToTile(newX)+1);
        if (Coordinates.tileToPixel(Coordinates.pixelToTile(newY)+1)-newY<_speed)
            newY=Coordinates.tileToPixel(Coordinates.pixelToTile(newY)+1);
        if (newX-Coordinates.tileToPixel(Coordinates.pixelToTile(newX))<_speed)
            newX=Coordinates.tileToPixel(Coordinates.pixelToTile(newX));
        if (newY-Coordinates.tileToPixel(Coordinates.pixelToTile(newY))<_speed)
            newY=Coordinates.tileToPixel(Coordinates.pixelToTile(newY));
        if (this._moving) move(newX,newY);
    }

    @Override
    public boolean canMove(double x, double y) {
        // TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không
        int xx=Coordinates.pixelToTile(x);
        int yy=(Coordinates.pixelToTile(y)-1);
        int pos = yy * this._board.getWidth() + xx;
        LayeredEntity cur = null;
        if (this._board._entities[pos].getSprite().equals(Sprite.brick)) return false;
        if (this._board._entities[pos].getSprite().equals(Sprite.wall)) return false;
        if (this._board._entities[pos] instanceof LayeredEntity) {
            cur = (LayeredEntity) this._board._entities[pos];
            if (cur.getTopEntity() instanceof Bomb) {
                if (!((Bomb)cur.getTopEntity()).getCurChar().contains(this))
                            return false;
            }
        }

            int xx1=Coordinates.pixelToTile(x+_sprite.get_realWidth()-1);
            int yy1=(Coordinates.pixelToTile(y)-1);
            pos = yy1 * this._board.getWidth() + xx1;
            if (this._board._entities[pos].getSprite().equals(Sprite.brick)) return false;
            if (this._board._entities[pos].getSprite().equals(Sprite.wall)) return false;
            if (this._board._entities[pos] instanceof LayeredEntity) {
                cur = (LayeredEntity) this._board._entities[pos];
                if (cur.getTopEntity() instanceof Bomb) {
                    if (!((Bomb)cur.getTopEntity()).getCurChar().contains(this))
                        return false;
                }
            }

            xx1=Coordinates.pixelToTile(x);
            yy1=(Coordinates.pixelToTile(y+_sprite.get_realHeight()-1)-1);
            pos = yy1 * this._board.getWidth() + xx1;
            if (this._board._entities[pos].getSprite().equals(Sprite.brick)) return false;
            if (this._board._entities[pos].getSprite().equals(Sprite.wall)) return false;
            if (this._board._entities[pos] instanceof LayeredEntity) {
                cur = (LayeredEntity) this._board._entities[pos];
                if (cur.getTopEntity() instanceof Bomb) {
                    if (!((Bomb)cur.getTopEntity()).getCurChar().contains(this))
                        return false;
                }
            }

            xx1=Coordinates.pixelToTile(x+_sprite.get_realWidth()-1);
            yy1=(Coordinates.pixelToTile(y+_sprite.get_realHeight()-1)-1);
            pos = yy1 * this._board.getWidth() + xx1;
            if (this._board._entities[pos].getSprite().equals(Sprite.brick)) return false;
            if (this._board._entities[pos].getSprite().equals(Sprite.wall)) return false;
            if (this._board._entities[pos] instanceof LayeredEntity) {
                cur = (LayeredEntity) this._board._entities[pos];
                if (cur.getTopEntity() instanceof Bomb) {
                    if (!((Bomb)cur.getTopEntity()).getCurChar().contains(this))
                        return false;
                }
            }
        return true;
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                this.inBombRate(1);
            }
        }

    }

    @Override
    public void move(double xa, double ya) {
        // TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không và thực hiện thay đổi tọa độ _x, _y
        // TODO: nhớ cập nhật giá trị _direction sau khi di chuyển
        int prevdir=_direction;
        if (xa>_x) _direction=1;
        if (xa<_x) _direction=3;
        if (ya<_y) _direction=0;
        if (ya>_y) _direction=2;
        if (!canMove(xa, ya)) {
            if (canMove(_x,ya)) xa=_x; else
            if (canMove(xa,_y)) ya=_y; else
            {
                xa=_x;
                ya=_y;
            }
        }
            if (xa==_x&&ya==_y) {
                if (!canMove(_x,_y)) _direction=prevdir;
            }
            _x = xa;
            _y = ya;
    }

    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý va chạm với Flame
        // TODO: xử lý va chạm với Enemy
        boolean res = super.collide(e);

        if (!res) {
            if (e instanceof Enemy) {this.kill();} else
            if (e instanceof FlameSegment) {

                this.kill();
            }

        }
        return res;
    }

    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }
}
