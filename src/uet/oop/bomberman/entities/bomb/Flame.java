package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import javax.swing.*;

public class Flame extends Entity {

	protected Board _board;
	protected int _direction;
	private int _radius;
	protected int xOrigin, yOrigin;
	protected FlameSegment[] _flameSegments = new FlameSegment[0];

	/**
	 *
	 * @param x hoành độ bắt đầu của Flame
	 * @param y tung độ bắt đầu của Flame
	 * @param direction là hướng của Flame
	 * @param radius độ dài cực đại của Flame
	 */
	public Flame(int x, int y, int direction, int radius, Board board) {
		xOrigin = x;
		yOrigin = y;
		_x = x;
		_y = y;
		_direction = direction;
		_radius = radius;
		_board = board;
		createFlameSegments();
	}

    public Flame(int x, int y, int direction, int radius, Board board, boolean Try) {
        xOrigin = x;
        yOrigin = y;
        _x = x;
        _y = y;
        _direction = direction;
        _radius = radius;
        _board = board;
    }

	public int flameLength() {
		return this._flameSegments.length;
	}

	private int calcX() {
		int x = 0;
		if (_direction == 1) {
			x = 1;
		} else
		if (_direction == 3) {
			x = -1;
		}
		return x;
	}

	private int calcY() {
		int y = 0;
		if (_direction == 0) {
			y = -1;
		} else
		if (_direction == 2) {
			y = 1;
		}
		return y;
	}

	/**
	 * Tạo các FlameSegment, mỗi segment ứng một đơn vị độ dài
	 */
	private void createFlameSegments() {
		/**
		 * tính toán độ dài Flame, tương ứng với số lượng segment
		 */
		_flameSegments = new FlameSegment[calculatePermitedDistance()+1];

		/**explode
		 * biến last dùng để đánh dấu cho segment cuối cùng
		 */
		boolean last;

		// TODO: tạo các segment dưới đây

		int x = calcX();
		int y = calcY();
		for (int i = 0; i < _flameSegments.length; i++) {
			int xx = (int)(_x + (i) * x);
			int yy = (int)(_y + (i) * y);
			_flameSegments[i] = new FlameSegment(xx, yy, _direction, i == _flameSegments.length - 1);
			//int xx = Coordinates.pixelToTile((int)(_x + (i + 1) * x));

			//int yy = Coordinates.pixelToTile((int)(_y + (i + 1) * y));
			int pos = yy*_board.getWidth()+xx;
			//System.out.println(Integer.toString((int)(_x + (i + 1) * x))+" "+Integer.toString((int)(_y + (i + 1) * y))+ " "+
			//Integer.toString(pos));
			Bomb xxx = _board.getBombAt(xx,yy);
			while (xxx!=null) {
				xxx.collide(_flameSegments[i]);
				xxx = _board.getBombAt(xx,yy);
			}
			_board._entities[pos].collide(_flameSegments[i]);

			LayeredEntity character = _board.getCharacterAtExcluding(xx, yy, null);
			if (character != null) {
				while (!character.isEmpty()) {
					character.getTopEntity().collide(_flameSegments[i]);
					//Character character1 = (Character) character.getTopEntity();
					//character1.kill();
					//if (character.getTopEntity() instanceof Bomber) System.out.println("minhminhminh");
					character.removeTop();
				}

			}
//			_flameSegments[i] = new FlameSegment((int)(_x), (int)(_y), 1, i == _flameSegments.length - 1);
//			_flameSegments[i] = new FlameSegment((int)(_x), (int)(_y), 2, i == _flameSegments.length - 1);
//			_flameSegments[i] = new FlameSegment((int)(_x), (int)(_y), 3, i == _flameSegments.length - 1);
		}
	}

	private static int calcX(int _direction) {
		int x = 0;
		if (_direction == 1) {
			x = 1;
		} else
		if (_direction == 3) {
			x = -1;
		}
		return x;
	}

	private static int calcY(int _direction) {
		int y = 0;
		if (_direction == 0) {
			y = -1;
		} else
		if (_direction == 2) {
			y = 1;
		}
		return y;
	}
	public static int calculatePermitedDistance(int _x, int _y, Board _board, int _radius, int _direction) {
		int x = calcX(_direction);
		int y = calcY(_direction);
		int len = _radius;
		for (int i = 1; i <= _radius; i++) {
			int xx = _x + i * x;
			int yy = _y + i * y;
			int pos = xx + yy * _board.getWidth();
			Sprite entity = _board._entities[pos].getSprite();
			if (entity.equals(Sprite.wall)) {
				len = i - 1;
				break;
			} else {
				if (entity.equals(Sprite.brick)) {
					len = i;
					break;
				}
			}
			if (entity.equals(Sprite.brick)) {
				len = i;
				break;
			}
		}
		return len;
	}

	/**
	 * Tính toán độ dài của Flame, nếu gặp vật cản là Brick/Wall, độ dài sẽ bị cắt ngắn
	 * @return
	 */


	public int calculatePermitedDistance() {
		// TODO: thực hiện tính toán độ dài của Flame
		int x = calcX();
		int y = calcY();
		int len = _radius;
		for (int i = 1; i <= _radius; i++) {
			int xx = (int)(_x) + i * x;
			int yy = (int)(_y) + i * y;
			int pos = xx + yy * _board.getWidth();
			Sprite entity = _board._entities[pos].getSprite();
			if (entity.equals(Sprite.wall)) {
				len = i - 1;
				break;
			} else {
				if (entity.equals(Sprite.brick)) {
					len = i;
					break;
				}
			}
			if (entity.equals(Sprite.brick)) {
				len = i;
				break;
			}
		}
		return len;
	}
	
	public FlameSegment flameSegmentAt(int x, int y) {
		for (int i = 0; i < _flameSegments.length; i++) {
			if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
				return _flameSegments[i];
		}
		return null;
	}

	@Override
	public void update() {
		for (int i = 0; i < _flameSegments.length; i++) {
			_flameSegments[i].update();
		}
	}
	
	@Override
	public void render(Screen screen) {
		for (int i = 1; i < _flameSegments.length; i++) {
			_flameSegments[i].render(screen);
		}
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Bomber, Enemy. Chú ý đối tượng này có vị trí chính là vị trí của Bomb đã nổ
		return super.collide(e);
	}
}
