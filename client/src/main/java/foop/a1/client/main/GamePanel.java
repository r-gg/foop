package foop.a1.client.main;

import foop.a1.client.inputs.KeyboardInputs;
import foop.a1.client.inputs.MouseInputs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;




public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {
        mouseInputs = new MouseInputs(this);
        this.game = game;

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void updateGame() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        for (int i = 0; i < 64; i++)
            for (int j = 0; j < 40; j++)
                g.fillRect(i * 20, j * 20, 20, 20);

        game.render(g);

    }

    public Game getGame() {
        return game;
    }

}