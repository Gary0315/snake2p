package tw.gary.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.JPanel;

public class gamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE;
	static int DELAY = 90;
	public int x[] = new int[GAME_UNITS];
	public int y[] = new int[GAME_UNITS];
	public int a[] = new int[GAME_UNITS];
	public int b[] = new int[GAME_UNITS];
	public int bodyParts = 6, bodyParts1 = 6;
	public int appleEaten, appleEaten1;
	public int appleX;
	public int appleY;
	public char direction = 'R';
	public char direction1 = 'L';
	public boolean running, isPause, isCreate, p1Lose, p2Lose;
	public String gameover = "";
	Timer timer;
	java.util.Timer timer1;

	Random random;
	BufferedImage apple, snakeR, snakeL, snakeU, snakeD, body1, body2, body3, body4, body5, body6, tailR, tailL, tailU,
			tailD, p2_1, p2_2, p2_3, p2_4, p2_5, p2_6, p2_7, p2_8, p2_9, p2_10, p2_11, p2_12, p2_13,p2_14, background = null;
	public Thread t1, t2;

	public gamePanel() {

		random = new Random();
		// 設定想要的尺寸

		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		// 設定焦點
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		try {

			apple = ImageIO.read(new File("snake/apple.png"));
			snakeR = ImageIO.read(new File("snake/snakeR.png"));
			snakeL = ImageIO.read(new File("snake/snakeL.png"));
			snakeU = ImageIO.read(new File("snake/snakeU.png"));
			snakeD = ImageIO.read(new File("snake/snakeD.png"));
			tailR = ImageIO.read(new File("snake/tailR.png"));
			tailL = ImageIO.read(new File("snake/tailL.png"));
			tailU = ImageIO.read(new File("snake/tailU.png"));
			tailD = ImageIO.read(new File("snake/tailD.png"));
			body1 = ImageIO.read(new File("snake/body1.png"));
			body2 = ImageIO.read(new File("snake/body2.png"));
			body3 = ImageIO.read(new File("snake/body3.png"));
			body4 = ImageIO.read(new File("snake/body4.png"));
			body5 = ImageIO.read(new File("snake/body5.png"));
			body6 = ImageIO.read(new File("snake/body6.png"));
			
			p2_1 = ImageIO.read(new File("snake/b1.png"));
			p2_2 = ImageIO.read(new File("snake/b2.png"));
			p2_3 = ImageIO.read(new File("snake/b3.png"));
			p2_4 = ImageIO.read(new File("snake/b4.png"));
			p2_5 = ImageIO.read(new File("snake/b5.png"));
			p2_6 = ImageIO.read(new File("snake/b6.png"));
			p2_7 = ImageIO.read(new File("snake/sr.png"));
			p2_8 = ImageIO.read(new File("snake/sd.png"));
			p2_9 = ImageIO.read(new File("snake/su.png"));
			p2_10 = ImageIO.read(new File("snake/sl.png"));
			p2_11 = ImageIO.read(new File("snake/tr.png"));
			p2_12 = ImageIO.read(new File("snake/td.png"));
			p2_13 = ImageIO.read(new File("snake/tu.png"));
			p2_14 = ImageIO.read(new File("snake/tl.png"));
		    background = ImageIO.read(new File("snake/background.jpg"));

		} catch (IOException e) {
			System.out.println(e.toString());
		}

		startGame();
	};

	public void startGame() {
		isPause = true;
		running = true;
		isCreate = false;
		timer = new Timer(DELAY, this);
		timer.stop();
		timer1 = new java.util.Timer();
		TimerTask t1 = new myTask();

		t2 = new Thread() {
			@Override
			public void run() {
				File file = new File("snake/gameover.wav");
				try {
					AudioInputStream audio = AudioSystem.getAudioInputStream(file);
					Clip clip = AudioSystem.getClip();
					clip.open(audio);

					clip.start();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		};
		timer1.schedule(t1, 100, 10000);

	}

	class myTask extends TimerTask {
		@Override
		public void run() {
			if (DELAY > 30) {
				DELAY -= 3;
				timer.setDelay(DELAY);
			}
		}
	}

	public void pauseGame() {
		if (isPause) {
			timer.stop();
			isPause = false;
		} else {
			timer.start();
			isPause = true;
		}

	}

	public void start() {
		timer.start();
		newApple();
		for (int i = 0; i < bodyParts; i++) {
			if ((appleX == a[i] + 600) && (appleY == a[i])) {
				newApple();
			}
		}
		for (int i = 0; i < bodyParts; i++) {
			if ((appleX == x[i]) && (appleY == y[i])) {
				newApple();
			}
		}
		isPause = true;
		running = true;
		isCreate = true;
	}

	// 畫圖

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		g.drawImage(background, 0, 0, null);
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score:" + appleEaten, (metrics.stringWidth("Score:" + appleEaten)) / 2 - 60,
				g.getFont().getSize());
		g.drawString("Score:" + appleEaten1, (metrics.stringWidth("Score:" + appleEaten)) / 2 + 400,
				g.getFont().getSize());

		if (running && isCreate) {

			// 繪製蘋果
			g.setColor(Color.red);
			g.drawImage(apple, appleX, appleY, null);

			// 繪製蛇的身體
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					if (direction == 'R') {
						g.drawImage(snakeR, x[i], y[i], null);
					} else if (direction == 'L') {
						g.drawImage(snakeL, x[i], y[i], null);
					} else if (direction == 'U') {
						g.drawImage(snakeU, x[i], y[i], null);
					} else {
						g.drawImage(snakeD, x[i], y[i], null);
					}

				} else if (i == (bodyParts - 1)) {
					if (x[i] < x[i - 1]) {
						g.drawImage(tailR, x[i], y[i], null);
					} else if (x[i] > x[i - 1]) {
						g.drawImage(tailL, x[i], y[i], null);
					} else if (y[i] > y[i - 1]) {
						g.drawImage(tailU, x[i], y[i], null);
					} else {
						g.drawImage(tailD, x[i], y[i], null);
					}
				} else {
					if (x[i] <= x[i - 1] && x[i] <= x[i + 1] && y[i] <= y[i - 1] && y[i] <= y[i + 1]) {
						g.drawImage(body3, x[i], y[i], null);
					} else if (x[i] <= x[i + 1] && x[i] <= x[i - 1] && y[i] >= y[i + 1] && y[i] >= y[i - 1]) {
						g.drawImage(body4, x[i], y[i], null);
					} else if (x[i] >= x[i + 1] && x[i] >= x[i - 1] && y[i] <= y[i + 1] && y[i] <= y[i - 1]) {
						g.drawImage(body5, x[i], y[i], null);
					} else if (x[i] >= x[i + 1] && x[i] >= x[i - 1] && y[i] >= y[i + 1] && y[i] >= y[i - 1]) {
						g.drawImage(body6, x[i], y[i], null);
					} else if (x[i] != x[i - 1] && x[i] != x[i + 1]) {
						g.drawImage(body1, x[i], y[i], null);
					} else if (y[i] != y[i - 1] && y[i] != y[i + 1]) {
						g.drawImage(body2, x[i], y[i], null);
					}

				}
			}
			for (int i = 0; i < bodyParts1; i++) {
				if (i == 0) {
					if (direction1 == 'R') {
						g.drawImage(p2_7, a[i] + 600, b[i], null);
					} else if (direction1 == 'L') {
						g.drawImage(p2_10, a[i] + 600, b[i], null);
					} else if (direction1 == 'U') {
						g.drawImage(p2_9, a[i] + 600, b[i], null);
					} else {
						g.drawImage(p2_8, a[i] + 600, b[i], null);
					}

				} else if (i == (bodyParts1 - 1)) {
					if (a[i] < a[i - 1]) {
						g.drawImage(p2_11, a[i] + 600, b[i], null);
					} else if (a[i] > a[i - 1]) {
						g.drawImage(p2_14, a[i] + 600, b[i], null);
					} else if (b[i] > b[i - 1]) {
						g.drawImage(p2_13, a[i] + 600, b[i], null);
					} else {
						g.drawImage(p2_12, a[i] + 600, b[i], null);
					}
				} else {
					if (a[i] <= a[i - 1] && a[i] <= a[i + 1] && b[i] <= b[i - 1] && b[i] <= b[i + 1]) {
						g.drawImage(p2_3, a[i] + 600, b[i], null);
					} else if (a[i] <= a[i + 1] && a[i] <= a[i - 1] && b[i] >= b[i + 1] && b[i] >= b[i - 1]) {
						g.drawImage(p2_4, a[i] + 600, b[i], null);
					} else if (a[i] >= a[i + 1] && a[i] >= a[i - 1] && b[i] <= b[i + 1] && b[i] <= b[i - 1]) {
						g.drawImage(p2_5, a[i] + 600, b[i], null);
					} else if (a[i] >= a[i + 1] && a[i] >= a[i - 1] && b[i] >= b[i + 1] && b[i] >= b[i - 1]) {
						g.drawImage(p2_6, a[i] + 600, b[i], null);
					} else if (a[i] != a[i - 1] && a[i] != a[i + 1]) {
						g.drawImage(p2_1, a[i] + 600, b[i], null);
					} else if (b[i] != b[i - 1] && b[i] != b[i + 1]) {
						g.drawImage(p2_2, a[i] + 600, b[i], null);
					}
				}
			}

		} else if (!running && isCreate) {
			gameOver(g);
			t2.start();
		}

	}

	public void newApple() {
		// 隨機數字 (範圍 )
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
			a[i] = a[i - 1];
			b[i] = b[i - 1];
		}
		// 上下左右移動
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

		switch (direction1) {
		case 'U':
			b[0] = b[0] - UNIT_SIZE;
			break;
		case 'D':
			b[0] = b[0] + UNIT_SIZE;
			break;
		case 'L':
			a[0] = a[0] - UNIT_SIZE;
			break;
		case 'R':
			a[0] = a[0] + UNIT_SIZE;
			break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			appleEaten++;
			newApple();
			for (int i = 0; i < bodyParts; i++) {
				if ((appleX == x[i]) && (appleY == y[i])) {
					newApple();
				}
			}
			for (int i = 0; i < bodyParts; i++) {
				if ((appleX == a[i] + 600) && (appleY == a[i])) {
					newApple();
				}
			}
			t1.start();
		} else if ((a[0] + 600 == appleX) && (b[0] == appleY)) {
			bodyParts1++;
			appleEaten1++;
			newApple();
			for (int i = 0; i < bodyParts; i++) {
				if ((appleX == a[i] + 600) && (appleY == a[i])) {
					newApple();
				}
			}
			for (int i = 0; i < bodyParts; i++) {
				if ((appleX == x[i]) && (appleY == y[i])) {
					newApple();
				}
			}
			t1.start();
		}
		t1 = new Thread() {
			@Override
			public void run() {
				File file = new File("snake/score.wav");
				try {
					AudioInputStream audio = AudioSystem.getAudioInputStream(file);
					Clip clip = AudioSystem.getClip();
					clip.open(audio);

					clip.start();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		};
	}

	public void checkcCollisions() {
		for (int i = bodyParts; i > 0; i--) {
			// 是否頭與身體相撞

			for (int j = bodyParts1; j > 0; j--) {
				// 頭與身體相撞
				if (((a[0] == a[j]) && (b[0] == b[j]))
						|| (((a[0] + 600 == x[i]) && (b[0] == y[i])) || ((a[0] + 600 == x[0]) && (b[0] == y[0])))) {
					running = false;
					isCreate = true;
					p2Lose = true;
				}
				if (((x[0] == x[i]) && (y[0] == y[i]))
						|| (((x[0] == a[j] + 600) && (y[0] == b[j])) || ((x[0] == a[0] + 600) && (y[0] == b[0])))) {
					running = false;
					isCreate = true;
					p1Lose = true;
				}
			}
		}
		// 檢查是否碰撞左邊邊界
		if (x[0] < 0) {
			running = false;
			isCreate = true;
			p1Lose = true;
		}
		// 檢查是否碰撞右邊邊界
		if (x[0] >= SCREEN_WIDTH) {
			running = false;
			isCreate = true;
			p1Lose = true;
		}
		// 檢查是否碰撞上面邊界
		if (y[0] < 0) {
			running = false;
			isCreate = true;
			p1Lose = true;
		}
		// 檢查是否碰撞下面邊界
		if (y[0] >= SCREEN_HEIGHT) {
			running = false;
			isCreate = true;
			p1Lose = true;
		}

		// 檢查是否碰撞左邊邊界
		if (a[0] + 600 < 0) {
			running = false;
			isCreate = true;
			p2Lose = true;
		}
		// 檢查是否碰撞右邊邊界
		if (a[0] + 600 >= SCREEN_WIDTH) {
			running = false;
			isCreate = true;
			p2Lose = true;
		}
		// 檢查是否碰撞上面邊界
		if (b[0] < 0) {
			running = false;
			isCreate = true;
			p2Lose = true;
		}
		// 檢查是否碰撞下面邊界
		if (b[0] >= SCREEN_HEIGHT) {
			running = false;
			isCreate = true;
			p2Lose = true;
		}

		if (!running) {
			timer.stop();

		}
	}// 碰撞方法

	public void gameOver(Graphics g) {
		if (p1Lose) {
			gameover = "2p Win!!!";
		} else if (p2Lose) {
			gameover = "1p Win!!!";
		} else if (p1Lose && p2Lose) {
			gameover = "DRAW!!!";
		} else if (gameFrame.i < 0) {
			if (appleEaten > appleEaten1) {
				gameover = "1p Win!!!";
			} else if (appleEaten < appleEaten1) {
				gameover = "2p Win!!!";
			} else if (appleEaten == appleEaten1) {
				gameover = "DRAW!!!";
			}
		}
		// gameover score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score:" + appleEaten, (metrics1.stringWidth("Score:" + appleEaten)) / 2 - 60,
				g.getFont().getSize());
		g.drawString("Score:" + appleEaten1, (metrics1.stringWidth("Score:" + appleEaten)) / 2 + 400,
				g.getFont().getSize());

		// gameover.text;
		g.setColor(Color.red);
		// 設定 字型
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString(gameover, (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkcCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				// 只能90度
				if (direction1 != 'R') {
					direction1 = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				// 只能90度
				if (direction1 != 'L') {
					direction1 = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				// 只能90度
				if (direction1 != 'D') {
					direction1 = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				// 只能90度
				if (direction1 != 'U') {
					direction1 = 'D';
				}
				break;
			case KeyEvent.VK_A:
				// 只能90度
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_D:
				// 只能90度
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_W:
				// 只能90度
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_S:
				// 只能90度
				if (direction != 'U') {
					direction = 'D';
				}
				break;

			case KeyEvent.VK_SHIFT:
				// 加速
				timer.setDelay(40);
				break;

			case KeyEvent.VK_SPACE:
				if (running && !isCreate) {
					start();
				} else if (!isPause) {
					timer.start();
					isPause = true;
				} else if (isPause) {
					timer.stop();
					isPause = false;

				}
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				timer.setDelay(DELAY);
			}
		}
	}

}
