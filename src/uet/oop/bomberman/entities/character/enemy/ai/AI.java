package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.character.enemy.ai.search_state.AIMid;
import uet.oop.bomberman.entities.character.enemy.ai.search_state.BFSState;
import uet.oop.bomberman.level.Coordinates;

import java.util.ArrayList;
import java.util.Random;

public abstract class AI {
	
	protected Random random = new Random();

	public static int oo = 1000000000;

	/**
	 * Thuật toán tìm đường đi
	 * @return hướng đi xuống/phải/trái/lên tương ứng với các giá trị 0/1/2/3
	 */
	public abstract int calculateDirection();

	protected int[] dx = new int[4];
	protected int[] dy = new int[4];

	public AI() {
		dx[0] = 0; dx[1] = 1; dx[2] = 0; dx[3] = -1;
		dy[0] = -1; dy[1] = 0; dy[2] = 1; dy[3] = 0;
	}

	public static int shortestPath(Enemy _e, int startX, int startY, int x, int y, Board _board) {
		int[] dx = new int[4];
		int[] dy = new int[4];

		dx[0] = 0; dx[1] = 1; dx[2] = 0; dx[3] = -1;
		dy[0] = -1; dy[1] = 0; dy[2] = 1; dy[3] = 0;
		int width = _board.getWidth();
		int height = _board.getHeight();

		boolean[][] canMove = new boolean[height][width];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				canMove[j][i] = true;
			}
		}
		ArrayList<BFSState> queue = new ArrayList<>();
		queue.add(new BFSState(startX, startY, 0));
		while (queue.size() > 0) {
			BFSState cur = queue.get(0);
			queue.remove(0);
			if (cur.x == x && cur.y == y) {
				return cur.path;
			}
			for (int dir = 0; dir < 4; dir++) {
				int newX = cur.x + dx[dir];
				int newY = cur.y + dy[dir];

				if (_e.canMove(Coordinates.tileToPixel(newX), Coordinates.tileToPixel(newY + 1)) && canMove[newY][newX]) {
					queue.add(new BFSState(newX, newY, cur.path + 1));
					canMove[newY][newX] = false;
				}
			}
		}
		return oo;
	}
}
