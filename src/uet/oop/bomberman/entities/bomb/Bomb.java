package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.SoundPlayer;

public class Bomb extends AnimatedEntitiy {

	protected double _timeToExplode = 180; //3 seconds
	public int _timeAfter = 50;
	protected Bomber curChar = null;
	protected Board _board;
	protected int _clock;
	protected Flame[] _flames;
	protected boolean _exploded = false;
	protected boolean _allowedToPassThru = true;
	protected int _radius;
	public Bomb(int x, int y, Board board, int radius) {
		_x = x;
		_y = y;
		_board = board;
		_sprite = Sprite.bomb;
		_radius=radius;
	}



	public int get_radius() {
		return _radius;
	}

	public double get_timeToExplode() {
		return _timeToExplode;
	}

	public Bomber getCurChar() {
		return curChar;
	}

	public void setCurChar(Bomber curChar) {
		this.curChar = curChar;
	}

	@Override
	public void update() {
		if(_timeToExplode > 0) 
			_timeToExplode--;
		else {
			if(!_exploded) 
				explode();
			else {
				++_clock;
				updateFlames();
			}
			
			if(_timeAfter > 0) 
				_timeAfter--;
			else
				remove();
		}
			
		animate();
	}
	
	@Override
	public void render(Screen screen) {
		if(_exploded) {
			_sprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2,
					Sprite.bomb_exploded1, Sprite.bomb_exploded, _clock, 50);
			renderFlames(screen);
		} else
			_sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, _animate, 60);
		
		int xt = (int)_x << 4;
		int yt = (int)_y << 4;
		
		screen.renderEntity(xt, yt , this);
	}
	
	public void renderFlames(Screen screen) {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].render(screen);
		}
	}
	
	public void updateFlames() {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].update();
		}
	}


	public boolean is_exploded() {
		return _exploded;
	}
    /**
     * Xử lý Bomb nổ
     */
	protected void explode() {
		if (_exploded) return;
		_clock=0;
		_timeToExplode=0;
		_exploded = true;
		SoundPlayer.playBombSound();

		_flames = new Flame[4];

		// TODO: xử lý khi Character đứng tại vị trí Bomb
		Flame flame1 = new Flame((int)(_x), (int)(_y), 0, _radius, _board);
		Flame flame2 = new Flame((int)(_x), (int)(_y), 1, _radius, _board);
		Flame flame3 = new Flame((int)(_x), (int)(_y), 2, _radius, _board);
		Flame flame4 = new Flame((int)(_x), (int)(_y), 3, _radius, _board);

//		if (_flames != null && _flames.length < 4) {
//		}
		_flames[0] = flame1;
		_flames[1] = flame2;
		_flames[2] = flame3;
		_flames[3] = flame4;

		// TODO: tạo các Flame
	}
	
	public FlameSegment flameAt(int x, int y) {
		if(!_exploded) return null;
		
		for (int i = 0; i < _flames.length; i++) {
			if(_flames[i] == null) return null;
			FlameSegment e = _flames[i].flameSegmentAt(x, y);
			if(e != null) return e;
		}
		
		return null;
	}

	@Override
	public boolean collide(Entity e) {
        // TODO: xử lý khi Bomber đi ra sau khi vừa đặt bom (_allowedToPassThru)
        // TODO: xử lý va chạm với Flame của Bomb khác
		boolean res = super.collide(e);
		if (!res)
			if (e instanceof FlameSegment) this.explode();
        return false;
	}
}
