package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class AIMedium extends AI {
	Bomber _bomber;
	Enemy _e;
	Board _board;
	
	public AIMedium(Board board, Bomber bomber, Enemy e) {
		_bomber = bomber;
		_e = e;
		_board = board;
	}



	@Override
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi

		List<Bomb> bombs = this._board.getBombs();

		int width = this._board.getWidth();
		int height = this._board.getHeight();

		boolean[][] canMove = new boolean[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				canMove[i][j] = true;
				Entity entity = this._board.getEntityAt(i, j);
				if (entity != null) {
					Sprite sprite = entity.getSprite();
					if (sprite.equals(Sprite.brick) || sprite.equals(Sprite.wall)) {
						canMove[i][j] = false;
					}
				}

			}
		}

		/*
			0
		3		1
			2
		*/

		Sprite sprite = this._e.getSprite();
		int _width = width * Game.TILES_SIZE;
		int _height = height * Game.TILES_SIZE;



		return 1;
	}

}
