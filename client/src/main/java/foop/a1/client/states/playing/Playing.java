package foop.a1.client.states.playing;

import foop.a1.client.dto.PositionDTO;
import foop.a1.client.main.Game;
import foop.a1.client.messages.request.UpdatePosition;
import foop.a1.client.states.playing.entities.Enemy;
import foop.a1.client.states.playing.entities.Player;
import foop.a1.client.states.State;
import foop.a1.client.states.playing.entities.SubwayEntrance;
import foop.a1.client.util.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Playing extends State {
    private Player player;
    private List<Player> players = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    private Map<String, Integer> scores;

    private List<SubwayEntrance> subwayEntrances = new ArrayList<>();

    public Playing() {
        Game.instance().subscribeToPositionUpdates(); // TODO: unsubscribe
        Game.instance().subscribeToGameOver();
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

        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g.drawString("Mice eaten: " + scores.get(Constants.SCOREBOARD_MICE_EATEN_STR), 10, 20);
        g.drawString("Mice escaped: " + scores.get(Constants.SCOREBOARD_MICE_ESCAPED_STR), 10, 40);
    }

    @Override
    public void update() {
        if (player != null) {
            var oldX = player.getPosition().getX();
            var oldY = player.getPosition().getY();
            player.update();
            if (!oldX.equals(player.getPosition().getX()) || !oldY.equals(player.getPosition().getY())) {
                // position changed, send to the server
                UpdatePosition updatePositionReq = new UpdatePosition();
                updatePositionReq.setGameId(Game.instance().getGameId());
                updatePositionReq.setNewPosition(new PositionDTO(player.getPosition().getX(), player.getPosition().getY()));
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

    public Player getPlayer() {
        return player;
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

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }
}
