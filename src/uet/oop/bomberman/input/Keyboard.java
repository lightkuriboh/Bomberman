package uet.oop.bomberman.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Tiếp nhận và xử lý các sự kiện nhập từ bàn phím
 */
public class Keyboard implements KeyListener {
	
	private boolean[] keys = new boolean[120]; //120 is enough to this game
	public boolean up, down, left, right, space, enter, A, D, S, W;
	
	public void update() {
		up = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_RIGHT];
		space = keys[KeyEvent.VK_SPACE];
		enter = keys[KeyEvent.VK_ENTER];
		A = keys[KeyEvent.VK_A];
		D = keys[KeyEvent.VK_D];
		S = keys[KeyEvent.VK_S];
		W = keys[KeyEvent.VK_W];
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

}
