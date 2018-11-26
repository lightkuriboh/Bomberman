package client;

import signal.GameStart;
import signal.PlayerMove;

public class EventListener {

    private Client _client;

    public EventListener(Client _client) {
        this._client = _client;
    }

    public void received(String res, Object data) {
        if (data instanceof GameStart) {
            _client.startGame();
        }
        if (data instanceof PlayerMove) {
            _client.updateCmd((PlayerMove)data);
        }
    }
}
