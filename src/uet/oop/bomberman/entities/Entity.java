package uet.oop.bomberman.entities;

import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.graphics.IRender;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

/**
 * Lớp đại diện cho tất cả thực thể trong game (Bomber, Enemy, Wall, Brick,...)
 */
public abstract class Entity implements IRender {

	protected double _x, _y;
	protected boolean _removed = false;
	protected Sprite _sprite;

	/**
	 * Phương thức này được gọi liên tục trong vòng lặp game,
	 * mục đích để xử lý sự kiện và cập nhật trạng thái Entity
	 */
	@Override
	public abstract void update();

	/**
	 * Phương thức này được gọi liên tục trong vòng lặp game,
	 * mục đích để cập nhật hình ảnh của entity theo trạng thái
	 */
	@Override
	public abstract void render(Screen screen);
	
	public void remove() {
		//System.out.println(this instanceof Bomb);
		_removed = true;
	}
	
	public boolean isRemoved() {
		return _removed;
	}
	
	public Sprite getSprite() {
		return _sprite;
	}

	/**
	 * Phương thức này được gọi để xử lý khi hai entity va chạm vào nhau
	 * @param e
	 * @return
	 */
	public boolean collide(Entity e) {
		double x = _x;
		double y = _y;
		if (this instanceof Flame ||this instanceof FlameSegment||
			this instanceof Tile || this instanceof Bomb) {
			x = Coordinates.tileToPixel(x);
			y = Coordinates.tileToPixel(y+1);
		}
		double x1 = x + this._sprite.get_realWidth()-1;
		double y1 = y + this._sprite.get_realHeight()-1;
		double xx = e.getX();
		double yy = e.getY();
		if (e instanceof Flame ||e instanceof FlameSegment||
			e instanceof Tile|| e instanceof Bomb) {
			xx = Coordinates.tileToPixel(xx);
			yy = Coordinates.tileToPixel(yy+1);
		}
		double xx1 = xx + e.getSprite().get_realWidth()-1;
		double yy1 = yy + e.getSprite().get_realHeight()-1;
		/*if (this instanceof Brick)
		System.out.println(Double.toString(this._sprite.get_realHeight())+" "+
                Double.toString(yy) + " " +
                Double.toString(y1)+" " +
                Double.toString(yy1));*/
		if (Math.min(xx1,x1)>=Math.max(x,xx)&&Math.min(yy1,y1)>=Math.max(y,yy)) return false; else return true;
	};
	
	public double getX() {
		return _x;
	}
	
	public double getY() {
		return _y;
	}
	
	public int getXTile() {
		return Coordinates.pixelToTile(_x + _sprite.SIZE / 2);
	}
	
	public int getYTile() {
		return Coordinates.pixelToTile(_y - _sprite.SIZE / 2);
	}
}
