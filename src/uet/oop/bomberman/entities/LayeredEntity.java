package uet.oop.bomberman.entities;

import uet.oop.bomberman.entities.tile.destroyable.DestroyableTile;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.bomb.Bomb;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Chứa và quản lý nhiều Entity tại cùng một vị trí
 * Ví dụ: tại vị trí dấu Item, có 3 Entity [Grass, Item, Brick]
 */
public class LayeredEntity extends Entity {
	
	protected LinkedList<Entity> _entities = new LinkedList<>();
	public LayeredEntity(int x, int y, Entity ... entities) {
		_x = x;
		_y = y;
		
		for (int i = 0; i < entities.length; i++) {
			_entities.add(entities[i]); 
			
			if(i > 1) {
				if(entities[i] instanceof DestroyableTile)
					((DestroyableTile)entities[i]).addBelowSprite(entities[i-1].getSprite());
			}
		}
	}
	
	@Override
	public void update() {
		clearRemoved();
		if (!(getTopEntity() instanceof Bomb)) getTopEntity().update();
	}
	
	@Override
	public void render(Screen screen) {
		if (getTopEntity() instanceof Bomb){
			List<Entity> x= new ArrayList<>();
			while (getTopEntity() instanceof Bomb) {
				x.add(getTopEntity());
				removeTop();
			}
			getTopEntity().render(screen);
			while (!x.isEmpty()) {
				addTop(x.get(x.size()-1));
				x.remove(x.size()-1);
			}
		} else getTopEntity().render(screen);
	}
	
	public Entity getTopEntity() {
		
		return _entities.getLast();
	}

	public boolean isEmpty() {
		return _entities.size()==0;
	}

	private void clearRemoved() {
		Entity top  = getTopEntity();
		
		if(top.isRemoved())  {
			_entities.removeLast();
		}
	}

	public void removeTop() {
		if (_entities.size()>0) _entities.removeLast();
	}

	@Override
	public Sprite getSprite() {
		return getTopEntity().getSprite();
	}

	public void addBeforeTop(Entity e) {
		_entities.add(_entities.size() - 1, e);
	}

	public void addTop(Entity e) {
		_entities.add(_entities.size(),e);
	}
	@Override
	public boolean collide(Entity e) {
		// TODO: lấy entity trên cùng ra để xử lý va chạm
		return this.getTopEntity().collide(e);
	}

}
