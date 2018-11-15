package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;


public class FlameSegment extends Entity {

	protected boolean _last;
	protected int _direction;
	protected int _clock;
	/**
	 *
	 * @param x
	 * @param y
	 * @param direction
	 * @param last cho biết segment này là cuối cùng của Flame hay không,
	 *                segment cuối có sprite khác so với các segment còn lại
	 */
	public FlameSegment(int x, int y, int direction, boolean last) {
		_x = x;
		_y = y;
		_last = last;
		_direction = direction;
		_clock = 0;
		switch (direction) {
			case 2:
				if(!last) {
					_sprite = Sprite.explosion_vertical;
				} else {
					_sprite = Sprite.explosion_vertical_top_last;
				}
				break;
			case 1:
				if(!last) {
					_sprite = Sprite.explosion_horizontal;
				} else {
					_sprite = Sprite.explosion_horizontal_right_last;
				}
				break;
			case 0:
				if(!last) {
					_sprite = Sprite.explosion_vertical;
				} else {
					_sprite = Sprite.explosion_vertical_down_last;
				}
				break;
			case 3:
				if(!last) {
					_sprite = Sprite.explosion_horizontal;
				} else {
					_sprite = Sprite.explosion_horizontal_left_last;
				}
				break;
		}
	}
	
	@Override
	public void render(Screen screen) {
		int xt = (int)_x << 4;
		int yt = (int)_y << 4;
		//animate();
		screen.renderEntity(xt, yt , this);
	}

	protected void chooseSprite() {
		_clock++;
		switch (_direction) {
			case 0:
				if (!_last) {
					_sprite=Sprite.movingSprite(Sprite.explosion_vertical,Sprite.explosion_vertical1,Sprite.explosion_vertical2,
							Sprite.explosion_vertical1,Sprite.explosion_vertical,_clock,50);
				}
				else {
					_sprite=Sprite.movingSprite(Sprite.explosion_vertical_top_last,Sprite.explosion_vertical_top_last1,
							Sprite.explosion_vertical_top_last2,
							Sprite.explosion_vertical_top_last1,Sprite.explosion_vertical_top_last,_clock,50);
				}
				break;
			case 2:
				if (!_last) {
					_sprite=Sprite.movingSprite(Sprite.explosion_vertical,Sprite.explosion_vertical1,Sprite.explosion_vertical2,
							Sprite.explosion_vertical1,Sprite.explosion_vertical,_clock,50);
				}
				else {
					_sprite=Sprite.movingSprite(Sprite.explosion_vertical_down_last,Sprite.explosion_vertical_down_last1,
							Sprite.explosion_vertical_down_last2,
							Sprite.explosion_vertical_down_last1,Sprite.explosion_vertical_down_last,_clock,50);
				}
				break;
			case 1:
				if (!_last) {
					_sprite=Sprite.movingSprite(Sprite.explosion_horizontal,Sprite.explosion_horizontal1,Sprite.explosion_horizontal2,
							Sprite.explosion_horizontal1,Sprite.explosion_horizontal,_clock,50);
				}
				else {
					_sprite=Sprite.movingSprite(Sprite.explosion_horizontal_right_last,Sprite.explosion_horizontal_right_last1,
							Sprite.explosion_horizontal_right_last2,
							Sprite.explosion_horizontal_right_last1,Sprite.explosion_horizontal_right_last,_clock,50);
				}
				break;
			case 3:
				if (!_last) {
					_sprite=Sprite.movingSprite(Sprite.explosion_horizontal,Sprite.explosion_horizontal1,Sprite.explosion_horizontal2,
							Sprite.explosion_horizontal1,Sprite.explosion_horizontal,_clock,50);
				}
				else {
					_sprite=Sprite.movingSprite(Sprite.explosion_horizontal_left_last,Sprite.explosion_horizontal_left_last1,
							Sprite.explosion_horizontal_left_last2,
							Sprite.explosion_horizontal_left_last1,Sprite.explosion_horizontal_left_last,_clock,50);
				}
				break;
		}
	}

	@Override
	public void update() {
		chooseSprite();
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý khi FlameSegment va chạm với Character

		return true;
	}
	

}