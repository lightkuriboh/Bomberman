package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.character.enemy.ai.search_state.AIMid;
import uet.oop.bomberman.entities.character.enemy.ai.search_state.BombInfo;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIMedium extends AI {
	private Bomber _bomber;
	private Enemy _e;
	private Board _board;
	private final int limit = 1000;
	int[] dx = new int[4];
	int[] dy = new int[4];
	
	public AIMedium(Board board, Bomber bomber, Enemy e) {
		_bomber = bomber;
		_e = e;
		_board = board;
		dx[0] = 0; dx[1] = 1; dx[2] = 0; dx[3] = -1;
		dy[0] = -1; dy[1] = 0; dy[2] = 1; dy[3] = 0;
	}

	public boolean inFlame(Sprite _sprite, double x, double y, ArrayList<BombInfo> bombs) {
		int width = this._board.getWidth();
		int height = this._board.getHeight();
		boolean[][] canMove = new boolean[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				canMove[i][j] = true;
			}
		}
		/*
			0
		3		1
			2
		*/

		for (BombInfo bomb: bombs) {
			if (bomb.exploding) {
				for (int i = 0; i < 4; i++) {
					int len = bomb.segmentLenght[i];
					for (int j = 1; j <= len; j++) {
						int xxx = bomb.x + j * dx[i];
						int yyy = bomb.y + j * dy[i];
						if (xxx > -1 && xxx < width && yyy > -1 && yyy < height) {
							canMove[xxx][yyy] = false;
						}
					}
				}
			}
		}

		int xx0= Coordinates.pixelToTile(x);
		int yy0=(Coordinates.pixelToTile(y)-1);

		if (!canMove[xx0][yy0]) return true;

		int xx1=Coordinates.pixelToTile(x+ _sprite.get_realWidth()-1);
		int yy1=(Coordinates.pixelToTile(y)-1);

		if (!canMove[xx1][yy1]) return true;

		int xx2=Coordinates.pixelToTile(x);
		int yy2=(Coordinates.pixelToTile(y+_sprite.get_realHeight()-1)-1);

		if (!canMove[xx2][yy2]) return true;

		int xx3=Coordinates.pixelToTile(x+_sprite.get_realWidth()-1);
		int yy3=(Coordinates.pixelToTile(y+_sprite.get_realHeight()-1)-1);

		if (!canMove[xx3][yy3]) return true;

		return false;
	}

	@Override
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi



		int width = this._board.getWidth() * Game.TILES_SIZE;
		int height = this._board.getHeight() * Game.TILES_SIZE;

		boolean[][] canMove = new boolean[width + Game.TILES_SIZE + 1][height + Game.TILES_SIZE + 1];

		for (int i = 0; i < width + Game.TILES_SIZE + 1; i++) {
			for (int j = 0; j < height + Game.TILES_SIZE + 1; j++) {
				canMove[i][j] = true;
			}
		}

		/*
			0
		3		1
			2
		*/

		List<Bomb> bombs = this._board.getBombs();

		ArrayList<AIMid> queue = new ArrayList<>();
		ArrayList<AIMid> candidate = new ArrayList<>();

		queue.add(new AIMid(this._e.getX(), this._e.getY(), bombs));

		Sprite _sprite = this._e.getSprite();

		int cnt = 0;

		while (queue.size() > 0) {
			AIMid cur = queue.get(0);
			queue.remove(0);

			if (cur.explode && inFlame(_sprite, cur.x, cur.y, cur.listBomb)) {
				break;
			}

			candidate.add(cur);

			for (int dir = 0; dir < 4; dir++) {
				double newX = cur.x + dx[dir] * this._e.get_speed();
				double newY = cur.y + dy[dir] * this._e.get_speed();
				System.out.print(cur.x);
				System.out.print(" ");
				System.out.print(cur.y);
				System.out.print(" | ");
				System.out.print(newX);
				System.out.print(" ");
				System.out.println(newY);
				if (this._e.canMove(newX, newY)
						&& (cur.directions.get(cur.directions.size() - 1) % 2 != dir % 2
						|| cur.directions.get(cur.directions.size() - 1) != dir)) {
					queue.add(new AIMid(cur, newX, newY, dir).update());
				}
			}

			++cnt;
			if (cnt == this.limit) {
				break;
			}
		}

		AIMid ans = null;

		if (candidate.isEmpty()) {
			return new Random().nextInt();
		}
		if (ans.directions.isEmpty()) {
			return new Random().nextInt();
		}

		double minDist = -1;

		for (AIMid aiMid: candidate) {
			double mahatan = Math.abs(ans.x - _bomber.getX()) + Math.abs(ans.y - _bomber.getY());
			if (minDist == -1) {
				ans = aiMid;
				minDist = mahatan;
			} else {
				if (mahatan < minDist) {
					minDist = mahatan;
					ans = aiMid;
				}
			}
		}
		return ans.directions.get(0);
	}

}
