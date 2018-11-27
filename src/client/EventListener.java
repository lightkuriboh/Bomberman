package client;

import signal.AssignId;
import signal.FullPlayer;
import signal.GameStart;
import signal.PlayerMove;

public class EventListener {

    private Client _client;

    public EventListener(Client _client) {
        this._client = _client;
    }

    public void received(Object data) {
        if (data instanceof GameStart) {
            _client.startGame((GameStart) data);
        }
        if (data instanceof PlayerMove) {

            System.out.println("id: " + 0 +", move: " + ((PlayerMove)data).getDirState().get(0));
            _client.updateCmd((PlayerMove)data);
        }
        if (data instanceof AssignId) {
            _client.setId(((AssignId)data).getId());
        }
        if (data instanceof FullPlayer) {
            System.out.println("There aren\'t any available slot!");
            _client.close();
        }
    }
}
