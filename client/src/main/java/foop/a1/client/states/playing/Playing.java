package foop.a1.client.states.playing;

import foop.a1.client.dto.PositionDTO;
import foop.a1.client.main.Game;
import foop.a1.client.messages.request.UpdatePosition;
import foop.a1.client.states.playing.entities.Enemy;
import foop.a1.client.states.playing.entities.Player;
import foop.a1.client.states.State;
import foop.a1.client.states.playing.entities.SubwayEntrance;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Playing extends State {
    private Player player;
    private List<Player> players = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    private List<SubwayEntrance> subwayEntrances = new ArrayList<>();

    public Playing() {
        Game.instance().subscribeToPositionUpdates(); // TODO: unsubscribe
    }

    @Override
    public void draw(Graphics g) {
        if (player != null) {
            player.render(g);
        }

        for (var player : players)
            player.render(g);

        for (var enemy : enemies)
            enemy.render(g);

        for (var entrance : subwayEntrances)
            entrance.render(g);
    }

    @Override
    public void update() {
        if (player != null) {
            var oldX = player.getX();
            var oldY = player.getY();
            player.update();
            if (oldX != player.getX() || oldY != player.getY()) {
                // position changed, send to the server
                UpdatePosition updatePositionReq = new UpdatePosition();
                updatePositionReq.setGameId(Game.instance().getGameId());
                updatePositionReq.setNewPosition(new PositionDTO(player.getX(), player.getY()));
                Game.service().sendUpdatePosition(updatePositionReq);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> player.setUp(true);
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.setLeft(true);
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> player.setDown(true);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.setRight(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> player.setUp(false);
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.setLeft(false);
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> player.setDown(false);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.setRight(false);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public List<SubwayEntrance> getSubwayEntrances() {
        return subwayEntrances;
    }

    public void setSubwayEntrances(List<SubwayEntrance> subwayEntrances) {
        this.subwayEntrances = subwayEntrances;
    }
}
