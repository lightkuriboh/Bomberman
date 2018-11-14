package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class SpeedItem extends Item {

	public SpeedItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý Bomber ăn Item
		boolean res = super.collide(e);
		if (!res) {
			if (e instanceof Bomber) {
				this.remove();
				((Bomber) e).inSpeed(0.5);
			}
		}
		return res;
	}
}
