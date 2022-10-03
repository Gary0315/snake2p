package tw.gary.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class gameFrame extends JFrame {
	private JButton pause, restart, start;
	private gamePanel panel = new gamePanel();
	private boolean isResume;
	private Timer timer;
	public static int i = 180;

	public gameFrame() {

		setLayout(new BorderLayout());
		start = new JButton("Start");
		pause = new JButton("Pause ");
		restart = new JButton("Restart");
		pause.setFocusable(false);
		restart.setFocusable(false);
		start.setFocusable(false);
		JLabel label1 = new JLabel();
		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.add(start);
		top.add(pause);
		top.add(restart);
		JLabel label = new JLabel("移動方式:上下左右,WASD");
		
		
		label.setFocusable(false);
		top.add(label);
		
		add(top, BorderLayout.NORTH);
		setBounds(0, 0, 600, 550);
		// 可以設定視窗開啟時的初始x,y位置 左上角
		this.add(panel, BorderLayout.CENTER);
		top.add(label1);
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.start();
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {

					public void run() {

						label1.setText("Time left: " + i);
						i--;

						if (i < 0) {
							panel.running = false;
							panel.isCreate = true;
							timer.cancel();
							label1.setText("Time Over");
						}
					}
				}, 0, 1000);
				
			}
		});

		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.pauseGame();
				if (!isResume) {
					pause.setText("Resume");
					isResume = true;
				} else {
					pause.setText("Pause");
					isResume = false;
				}

			}
		});

		restart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.isPause = false;
				panel.p1Lose = false;
				panel.p2Lose = false;
				panel.direction = 'R';
				panel.direction1 = 'L';
				panel.bodyParts = 6;
				panel.bodyParts1 = 6;
				panel.appleEaten = 0;
				panel.appleEaten1 = 0;
				panel.DELAY = 90;
				panel.x = new int[gamePanel.GAME_UNITS];
				panel.y = new int[gamePanel.GAME_UNITS];
				panel.a = new int[gamePanel.GAME_UNITS];
				panel.b = new int[gamePanel.GAME_UNITS];
				panel.startGame();
				i=60;

				repaint();
			}
		});
		
		this.setTitle("Snake");
		this.setResizable(false);// 設定是否可由用戶端調整大小
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		// 出現視窗
//		JOptionPane.showInputDialog(new String("name:"));

	}
}
