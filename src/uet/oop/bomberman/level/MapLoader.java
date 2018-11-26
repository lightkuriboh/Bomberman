package uet.oop.bomberman.level;

import signal.GameStart;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.exceptions.LoadLevelException;

/**
 * Load và lưu trữ thông tin bản đồ các màn chơi
 */
public abstract class MapLoader {

	protected int _width = 20, _height = 20; // default values just for testing
	protected int _level;
	protected Board _board;

	public MapLoader(Board board, GameStart gameStart) throws LoadLevelException {
		_board = board;
		loadLevel(gameStart);
	}

	public abstract void loadLevel(GameStart gameStart) throws LoadLevelException;

	public abstract void createEntities();

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

	public int getLevel() {
		return _level;
	}

}
