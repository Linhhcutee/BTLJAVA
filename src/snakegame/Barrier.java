/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snakegame;

/**
 *
 * @author linhca
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Barrier {

    private Rectangle rect;
    private Color color;

    public Barrier(int x, int y, int width, int height, Color color) {
        rect = new Rectangle(x, y, width, height);
        this.color = color;
    }

    public boolean collidesWith(Point p) {
        return rect.contains(p);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    
}
