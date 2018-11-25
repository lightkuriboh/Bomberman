package uet.oop.bomberman;

import uet.oop.bomberman.gui.Frame;
import uet.oop.bomberman.database.Mongo;

public class BombermanGame {
	
	public static void main(String[] args) {
//		Mongo.ping();
//		System.out.println(Mongo.getAction("player1"));
//		Mongo.updateAction("player1", -1);
//		System.out.println(Mongo.getAction("player1"));
		new Frame();
	}
}
