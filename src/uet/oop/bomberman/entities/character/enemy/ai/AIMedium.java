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
	private final int limit = 100;

	private boolean smart = false;
	private boolean optimal = false;

	
	public AIMedium(Board board, Bomber bomber, Enemy e, boolean optimal, boolean smart) {
		_bomber = bomber;
		_e = e;
		_board = board;
		this.smart = smart;
		this.optimal = optimal;
	}

	public boolean inFlame(Sprite _sprite, int x, int y, int pre_x, int pre_y, ArrayList<BombInfo> bombs) {
		int width = this._board.getWidth();
		int height = this._board.getHeight();
		boolean[][] canMove = new boolean[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				canMove[i][j] = true;
			}
		}
		/*
			0
		3		1
			2
		*/
		boolean closer = false;

		for (BombInfo bomb: bombs) {
			if (bomb.exploding) {
				closer |= (bomb.x == x || bomb.y == y) && AI.shortestPath(this._e, bomb.x, bomb.y, x, y, _board) < AI.shortestPath(this._e, bomb.x, bomb.y, pre_x, pre_y, _board);
				for (int i = 0; i < 4; i++) {
					int len = Flame.calculatePermitedDistance(bomb.x, bomb.y, this._board, bomb.radius, i);
					for (int j = 0; j <= len; j++) {
						int xxx = bomb.x + j * dx[i];
						int yyy = bomb.y + j * dy[i];
						if (xxx > -1 && xxx < width && yyy > -1 && yyy < height) {
							canMove[yyy][xxx] = false;
						}
					}
				}
			}
		}

//		for (int i = 0; i < height; i++) {
//			for (int j = 0; j < width; j++) {
//				System.out.print(canMove[i][j] + " ");
//			}
//			System.out.println();
//		}
//		System.out.println(x + " " + y);
//		System.out.println("------------------------------------------");

		if (!canMove[y][x] && closer) return true;
		return false;
	}

	@Override
	public int calculateDirection() {
		// TODO: cài đặt thuật toán tìm đường đi

//		System.out.println(this._e.getX() + " " + this._e.getY());
//		if (this._board.getBombs().size() > 0) {
//			System.out.println(_board.getBombs().get(0).get_timeToExplode() + " time bomb");
//		}
//		System.out.println("-------------------------------------------------");

		int width = this._board.getWidth();
		int height = this._board.getHeight();

		boolean[][] canMove = new boolean[height][width];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				canMove[j][i] = true;
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

		double centerX = this._e.getX() + this._e.getSprite().get_realWidth() / 2;
		double centerY = this._e.getY() + this._e.getSprite().get_realHeight() / 2;
		int tileX = Coordinates.pixelToTile(centerX);
		int tileY = Coordinates.pixelToTile(centerY) - 1;
//		System.out.println(tileX + " " + tileY);
		queue.add(new AIMid(tileX, tileY, bombs, this._board, this._e));

		Sprite _sprite = this._e.getSprite();

		int cnt = 0;

		//System.out.println(Coordinates.pixelToTile(this._bomber.getX() + _bomber.getSprite().get_realWidth()/2) + " " + Coordinates.pixelToTile(this._bomber.getY() + _bomber.getSprite().get_realHeight() / 2));

		while (queue.size() > 0) {
			AIMid cur = queue.get(0);
			queue.remove(0);

			if (cur.explode) {
//				System.out.println("Bomb exploding!");
				if (this.smart && inFlame(_sprite, cur.x, cur.y, cur.pre_x, cur.pre_y, cur.listBomb)) {
					//System.out.println(queue.size() + " In Flame!");
					continue;
				}

			}

			candidate.add(new AIMid(cur));

			/*
				0
			3		1
				2
			 */
			for (int dir = 0; dir < 4; dir++) {
				int newX = cur.x + dx[dir];
				int newY = cur.y + dy[dir];

				if (this._e.canMove(Coordinates.tileToPixel(newX), Coordinates.tileToPixel(newY + 1)) && canMove[newY][newX]) {

					queue.add(new AIMid(cur, newX, newY, dir, this._board, this._e).update());
					canMove[newY][newX] = false;
				}
			}

			++cnt;
			if (cnt == this.limit) {
				break;
			}
		}

		AIMid ans = null;

		if (candidate.isEmpty()) {

			return new Random().nextInt(4);
		}

		double minDist = -1;



		for (AIMid aiMid: candidate) {
			double mahatan = Math.abs(Coordinates.tileToPixel(aiMid.x) - _bomber.getX()) +
					Math.abs(Coordinates.tileToPixel(aiMid.y) - _bomber.getY());
			if (this.optimal) {
				int cur = AI.shortestPath(this._e, Coordinates.pixelToTile(this._bomber.getX()), Coordinates.pixelToTile(this._bomber.getY()), aiMid.x, aiMid.y, this._board);
				if (cur != AI.oo) {
					mahatan = cur;
				}
			}
			if (minDist == -1 && aiMid.directions.size() > 0) {
				ans = aiMid;
				minDist = mahatan;
			} else {
				if (mahatan < minDist && aiMid.directions.size() > 0) {
					minDist = mahatan;
					ans = aiMid;
				}
			}

		}
//		System.out.println(candidate.size());

		if (minDist == -1) {
			return -1;
		}

		if (ans.directions.size() == 0) {
			return -1;
		}

		return ans.directions.get(0);
	}

}
