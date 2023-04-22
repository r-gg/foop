package foop.a1.client.states.playing;

import foop.a1.client.states.playing.entities.Enemy;
import foop.a1.client.states.playing.entities.Player;
import foop.a1.client.states.State;
import foop.a1.client.states.playing.entities.SubwayEntrance;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Playing extends State {
    private final Player player;
    private List<Player> players = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    private List<SubwayEntrance> subwayEntrances = new ArrayList<>();

    public Playing() {
        player = new Player();
    }

    @Override
    public void draw(Graphics g) {
        player.render(g);

        for (var player : players)
            player.render(g);

        for (var enemy : enemies)
            enemy.render(g);

        for (var entrance : subwayEntrances)
            entrance.render(g);
    }

    @Override
    public void update() {
        player.update();
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
