package snakegame;
import java.awt.Point;
import java.util.List;
import java.util.Random;
import snakegame.Barrier;

public class Food {
    private Point location;
    private Random random;

    public Food(int windowWidth, int windowHeight, int cellSize, List<Barrier> barriers, List<Point> snakeBody) {
        location = new Point();
        random = new Random();
        generateFood(windowWidth, windowHeight, cellSize, barriers, snakeBody);
    }

    public void generateFood(int windowWidth, int windowHeight, int cellSize, List<Barrier> barriers, List<Point> snakeBody) {
        int x, y;
        do {
            x = random.nextInt(windowWidth / cellSize) * cellSize;
            y = random.nextInt(windowHeight / cellSize) * cellSize;
            location.setLocation(x, y);
        } while (isOnBarrier(barriers) && isOnSnake(snakeBody));
    }

    public Point getLocation() {
        return location;
    }

    private boolean isOnBarrier(List<Barrier> barriers) {
        for (Barrier barrier : barriers) {
            if (barrier.collidesWith(location)) {
                return true;
            }
        }
        return false;
    }
    private boolean isOnSnake(List<Point> snakeBody) {
    for (Point point : snakeBody) {
        if (point.equals(location)) {
            return true;
        }
    }
    return false;
    }
}
