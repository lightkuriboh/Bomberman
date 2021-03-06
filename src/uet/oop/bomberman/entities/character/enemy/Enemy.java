package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.ai.*;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Enemy extends Character {

	protected int _points;
	
	protected double _speed;
	protected AI _ai;

	protected final double MAX_STEPS;
	protected final double rest;
	protected double _steps;
	
	protected int _finalAnimation = 30;
	protected Sprite _deadSprite;
	
	public Enemy(int x, int y, Board board, Sprite dead, double speed, int points) {
		super(x, y, board);
		
		_points = points;
		_speed = speed;
		
		MAX_STEPS = Game.TILES_SIZE / _speed;
		rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
		_steps = MAX_STEPS;
		
		_timeAfter = 20;
		_deadSprite = dead;
	}

	public double get_speed() {
		return _speed;
	}

	@Override
	public void update() {
		animate();
		
		if(!_alive) {
			afterKill();
			return;
		}
		
		if(_alive)
			calculateMove();
	}
	
	@Override
	public void render(Screen screen) {
		
		if(_alive)
			chooseSprite();
		else {
			if(_timeAfter > 0) {
				_sprite = _deadSprite;
				_animate = 0;
			} else {
				_sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, _animate, 60);
			}
				
		}
			
		screen.renderEntity((int)_x, (int)_y - _sprite.SIZE, this);
	}
	
	@Override
	public void calculateMove() {

		double x = 0;
		double y = 0;

		if (_steps <= 0) {
			_direction = _ai.calculateDirection();
			//System.out.println(_direction + " dir");
			_steps = MAX_STEPS;
		}

		if (_direction == 0) {
			y = -1 * _speed;
		} else
			if (_direction == 1) {
				x = _speed;
			} else
				if (_direction == 2) {
					y = _speed;
				} else
					if (_direction == 3) {
						x = -1 * _speed;
					} else {
						x = 0;
						y = 0;
					}
		double newX = x + _x;
		double newY = y + _y;
		if (Coordinates.tileToPixel(Coordinates.pixelToTile(newX)+1)-newX<_speed)
			newX=Coordinates.tileToPixel(Coordinates.pixelToTile(newX)+1);
		if (Coordinates.tileToPixel(Coordinates.pixelToTile(newY)+1)-newY<_speed)
			newY=Coordinates.tileToPixel(Coordinates.pixelToTile(newY)+1);
		if (newX-Coordinates.tileToPixel(Coordinates.pixelToTile(newX))<_speed)
			newX=Coordinates.tileToPixel(Coordinates.pixelToTile(newX));
		if (newY-Coordinates.tileToPixel(Coordinates.pixelToTile(newY))<_speed)
			newY=Coordinates.tileToPixel(Coordinates.pixelToTile(newY));



		if (canMove(newX, newY)) {
			_steps -= 1 + rest;
			move(newX - _x, newY - _y);
			_moving = true;
		} else {
			_steps = 0;
			_moving = false;
		}
		// TODO: Tính toán hướng đi và di chuyển Enemy theo _ai và cập nhật giá trị cho _direction
		// TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không
		// TODO: sử dụng move() để di chuyển
		// TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
	}
	
	@Override
	public void move(double xa, double ya) {
		if(!_alive) return;

		_y += ya;
		_x += xa;
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

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Flame
		// TODO: xử lý va chạm với Bomber
		if (e instanceof FlameSegment) {
			kill();
			return false;
		}
		if (e instanceof Bomber) {
			((Bomber) e).kill();
			return false;
		}
		return true;
	}
	
	@Override
	public void kill() {
		if(!_alive) return;
		_alive = false;
		//_removed = true;
		_board.addPoints(_points);

		Message msg = new Message("+" + _points, getXMessage(), getYMessage(), 2, Color.white, 14);
		_board.addMessage(msg);
	}
	
	
	@Override
	protected void afterKill() {
		if(_timeAfter > 0) --_timeAfter;
		else {
			if(_finalAnimation > 0) --_finalAnimation;
			else
				remove();
		}
	}
	
	protected abstract void chooseSprite();
}
