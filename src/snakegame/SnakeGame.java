    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
     */
    package snakegame;

    import java.awt.Color;
    import java.awt.Dimension;
    import java.awt.Graphics;
    import java.awt.Point;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.KeyEvent;
    import java.awt.event.KeyListener;
    import java.util.ArrayList;
    import javax.swing.JPanel;
    import javax.swing.Timer;

    public class SnakeGame extends JPanel implements ActionListener, KeyListener {

        // xác định các biến trò chơi
        private static final int WINDOW_WIDTH = 640;
        private static final int WINDOW_HEIGHT = 480;
        private static final int CELL_SIZE = 10;
        private static final int GAME_SPEED = 100;
        private static final int MAX_SCORE = (WINDOW_WIDTH / CELL_SIZE) * (WINDOW_HEIGHT / CELL_SIZE) - 1;

        private Timer timer;
        private ArrayList<Point> snakeBody;
        private Food food;
        private String direction;
        private int score;
        private boolean gameOver;
        private ArrayList<Barrier> barriers;
        private boolean paused = false;

        public SnakeGame() {
            // khởi tạo các biến trò chơi
            setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            setBackground(Color.BLACK);
            setFocusable(true);
            addKeyListener(this);
            timer = new Timer(GAME_SPEED, this);
            snakeBody = new ArrayList<Point>();
            snakeBody.add(new Point(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
            barriers = new ArrayList<Barrier>();
            barriers.add(new Barrier(300, 50, 90, 20, Color.GRAY));
            barriers.add(new Barrier(200, 430, 90, 20, Color.GRAY));
            food = new Food(WINDOW_WIDTH,WINDOW_HEIGHT,CELL_SIZE, barriers, snakeBody);
            direction = "RIGHT";
            score = 0;
            gameOver = false;
            resetGame();
            // bắt đầu vòng lặp trò chơi
            timer.start();
            grow(3);
        }
        public void resetGame() {
            snakeBody = new ArrayList<Point>();
            snakeBody.add(new Point(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
            food = new Food(WINDOW_WIDTH, WINDOW_HEIGHT, CELL_SIZE,barriers, snakeBody);
            direction = "RIGHT";
            score = 0;
            gameOver = false;
            timer.start();
        }



            @Override
        public void actionPerformed(ActionEvent e) {
            // di chuyển con rắn theo hướng hiện tại
            Point head = snakeBody.get(0);
            Point newHead = new Point(head.x, head.y);
            if (direction.equals("UP")) {
                newHead.y -= CELL_SIZE;
            } else if (direction.equals("DOWN")) {
                newHead.y += CELL_SIZE;
            } else if (direction.equals("LEFT")) {
                newHead.x -= CELL_SIZE;
            } else if (direction.equals("RIGHT")) {
                newHead.x += CELL_SIZE;
            }
            //check chạm vật cản
            for (Barrier barrier : barriers) {
                if (barrier.collidesWith(snakeBody.get(0))) {
                    gameOver = true;
                break;
                }
            }
            wrapAround(newHead);
            // kiểm tra con rắn có ăn ăn thức ăn không
            if (newHead.equals(food.getLocation())) {
                snakeBody.add(0, newHead);
                score++;
                food.generateFood(WINDOW_WIDTH, WINDOW_HEIGHT, CELL_SIZE, barriers, snakeBody);
            } else {
                // di chuyển cơ thể răn và kiểm tra
            if (snakeBody.contains(newHead)) {
                    gameOver = true;
                } else {
                    snakeBody.add(0, newHead);
                    snakeBody.remove(snakeBody.size() - 1);
                }
            }

            if (gameOver == true) {
                timer.stop();
            }
            repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_W && !direction.equals("DOWN")) {
                direction = "UP";
            } else if (keyCode == KeyEvent.VK_S && !direction.equals("UP")) {
                direction = "DOWN";
            } else if (keyCode == KeyEvent.VK_A && !direction.equals("RIGHT")) {
                direction = "LEFT";
            } else if (keyCode == KeyEvent.VK_D && !direction.equals("LEFT")) {
                direction = "RIGHT";
            }
            if(keyCode == KeyEvent.VK_SPACE && gameOver == true){
                    resetGame();
            }
            if(keyCode == KeyEvent.VK_ENTER){
                if(paused){
                    timer.start();
                    paused = false;
                }else{
                    timer.stop();
                    paused = true;
                }
                repaint();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

//        private boolean hitWall(Point head) {
//            // kiểm tra con rắn nếu đâm vào tường
//            return head.x < 0 || head.x >= WINDOW_WIDTH || head.y < 0 || head.y >= WINDOW_HEIGHT;
//        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // vẽ thân rắn
            g.setColor(Color.GREEN);
            boolean isHead = true;
            for (Point p : snakeBody) {
                if (isHead) {
                   g.setColor(Color.pink);
                isHead = false;
            } else {
            g.setColor(Color.GREEN);
        }
        g.fillRect(p.x, p.y, CELL_SIZE, CELL_SIZE);
    }
            // vẽ mồi
            g.setColor(Color.RED);
            g.fillRect(food.getLocation().x, food.getLocation().y, CELL_SIZE, CELL_SIZE);
            //thêm vật cản
            for (Barrier barrier : barriers) {
                barrier.draw(g);
            }
            // hiển thị điểm
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 10, 20);
            g.drawString("Pause: Enter", 10, 35);
            // thông báo game over
            if (gameOver == true) {
                g.drawString("Game Over!", WINDOW_WIDTH / 2 - 30, 230);
                g.drawString("Score: "+score, WINDOW_WIDTH / 2 - 30, 250);
                g.drawString("Press space to reset", WINDOW_WIDTH / 2 - 30, 270);
                timer.stop();
            }
            if(paused){
                g.drawString("press enter to continue", WINDOW_WIDTH / 2 - 35, 230);
            }
        }
        public int getScore()
        {
            return this.score;
        }
        public void grow(int amount) {
        for (int i = 0; i < amount; i++) {
            Point tail = snakeBody.get(snakeBody.size() - 1);
            snakeBody.add(new Point(tail.x, tail.y));
        }
    }
    private void wrapAround(Point head) {
    // Kiểm tra xem con rắn có đi ra khỏi màn hình hay không và cập nhật vị trí của nó
    if (head.x < 0) {
        head.x = WINDOW_WIDTH - CELL_SIZE;
    } else if (head.x >= WINDOW_WIDTH) {
        head.x = 0;
    } else if (head.y < 0) {
        head.y = WINDOW_HEIGHT - CELL_SIZE;
    } else if (head.y >= WINDOW_HEIGHT) {
        head.y = 0;
    }
}
        
}

