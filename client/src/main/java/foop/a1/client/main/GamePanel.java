package foop.a1.client.main;

import foop.a1.client.inputs.KeyboardInputs;
import foop.a1.client.inputs.MouseInputs;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

    public GamePanel() {
        var mouseInputs = new MouseInputs();

        setPanelSize();
        addKeyListener(new KeyboardInputs());

        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(832, 448);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game.instance().render(g);
    }
}