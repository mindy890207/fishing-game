package fish;

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.net.URL;
import java.applet.AudioClip;
import java.io.*;
import java.applet.Applet;
import java.net.MalformedURLException;
import java.net.URL;
@SuppressWarnings("serial")
public class Fish extends JPanel implements Config, ActionListener{
	
	JPanel panel;
	JButton btn_start,btn_help;
	int state = 0;//0為正常 1為釣到魚 2為釣到食人魚
	int scoreX = -315 , scoreY = 0;//分數的XY座標
	int titleX = 15  , titleY = 350;//標題的XY座標
	int countDown = 60;//計時秒數
	int mode = 0;
	boolean gameStart = false;
	public static  GreenFish gFish[] = new GreenFish[3]; 
	public GreenFish fast = new GreenFish();
	public static  GreenFish garbage[] = new GreenFish[2];
    private int rod_y = 300;
	private Timer timer;
	private int intervel = 1000/100;
	private int count=-100,r=(int)(Math.random()*200+300),direct = 0; //direct0向前 1向上 2向下
	private int score=0;
	public static void main(String[] args) {
		String path = "image/Dolphin.wav";
		Music m = new Music();
		m.playMusic(path);
		for(int i = 0;i<3;i++) {
			gFish[i] = new GreenFish();
		}
		for(int i = 0;i<2;i++) {
			garbage[i] = new GreenFish();
		}
		JFrame f = new JFrame("Fishing Game");
		Fish game = new Fish();
		f.add(game);
		f.setSize(width, length);
		f.setAlwaysOnTop(true);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		JOptionPane.showMessageDialog( f, new Object[] { 
				new JLabel( "釣魚遊戲", JLabel.LEFT ),
				new JSeparator(),
				"遊戲方法 :",
				"上下移動滑鼠即可釣魚",
				new JSeparator(),
				new JLabel( "盡你所能的釣魚吧", JLabel.LEFT ),
				new JLabel( "小心食人魚!釣到他遊戲就結束!", JLabel.LEFT ),
				new JSeparator(),
			}, "Fishing help", JOptionPane.INFORMATION_MESSAGE );
		
			
	}
	public void Mode() {
		String[] options = {"模式1","模式2"};
		mode = JOptionPane.showOptionDialog(panel,"請選擇模式","開始前",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,"模式1");
	}
	public Fish(){
		panel = setpanel();
		add(panel);
		setVisible(true);
	}	

	public void paint(Graphics g) {
		paintCompenent(g);//畫背景
		paintScore(g);//畫分數
		paintTitle(g);//畫標題
		paintState(g);//畫倉鼠狀態
		paintLine(g);//畫釣魚線
		paintBait(g);//畫魚餌
		paintBall(g);//畫球球(暫時的魚) 先當作食人魚
		paintGreenFish(g);
		paintBasket(g);//畫籃子
		paintTime(g);//畫計時器
		paintFast(g);
		paintGarbage(g);
	}
	
	//畫背景
	public void paintCompenent(Graphics g) {	
		btn_start.requestFocus();
		g.drawImage(bround, 0, 0, this.getWidth(), this.getHeight(), null);
	}
	//畫釣魚線
		public void paintLine(Graphics g) {
			g.setColor(Color.BLACK);
			if(rod_y > 230) {
				g.drawLine(396, 115, 396, rod_y);
			}
			else
				if(rod_y <= 230 ) {
					g.drawLine(396, 115, 396,230);
				}
		}
	//畫分數
	public void paintScore(Graphics g) {
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 30);
		g.setColor(Color.red);
		g.setFont(font);
		g.drawImage(scoreboard, scoreX, scoreY, 100, 75, null);
		g.drawString(" " + score/10, scoreX, scoreY + 60);
		g.drawString(" " + score%10, scoreX + 45, scoreY + 60);
	}
	//畫時間
	public void paintTime(Graphics g) {

		Font font = new Font(Font.MONOSPACED, Font.BOLD, 45);
		g.setColor(Color.black);
		g.setFont(font);
		g.drawImage(clock, scoreX-645, scoreY +20, 100, 100, null);
		if(countDown>=10) {
			g.setColor(Color.black);
			g.drawString(" " + countDown/10, scoreX-650, scoreY + 88);
			g.drawString(" " + countDown%10, scoreX-624, scoreY + 88);
		}
		else {
			g.setColor(Color.red);
			g.drawString(" " + countDown/10, scoreX-650, scoreY + 88);
			g.drawString(" " + countDown%10, scoreX-624, scoreY + 88);
		}
	}
	
	//畫標題
	public void paintTitle(Graphics g) {
		g.drawImage(title, titleX, titleY, this);
	}
	
	//畫倉鼠狀態
	public void paintState(Graphics g) {
		if(mode == JOptionPane.YES_OPTION) {
			switch(state) {
			case 0 ://一般狀態
					g.drawImage(normalface, 538, 81, this);
				break;
			case 1 ://釣到魚
				g.drawImage(happyface, 538, 81, this);
				g.drawImage(love, 518, 70, 40, 40, null);
				break;
			case 2 ://釣到食人魚
				g.drawImage(sadface, 545, 88, this);
				break;
		}
		}
		if(mode == JOptionPane.NO_OPTION) {////////////////////////////////////////////魚夫照片改這
			g.drawImage(teacher, 545, 88, this);
		}
	}
	//畫魚餌
	public void paintBait(Graphics g) {
		if(mode == JOptionPane.YES_OPTION) {

			if(rod_y > 230)
				g.drawImage(hook, 370, rod_y-25, 60, 60, null);
			else
				g.drawImage(hook, 370, 205, 60, 60, null);
		}
		if(mode == JOptionPane.NO_OPTION) {////////////////////////////////////////////魚餌照片改這
			if(rod_y > 230)
				g.drawImage(grade, 370, rod_y-25, 60, 60, null);
			else
				g.drawImage(grade, 370, 205, 60, 60, null);
		}
	}
	//畫球球(暫時的魚)
	public void paintBall(Graphics g) {
		if(mode == JOptionPane.YES_OPTION) {
			g.setColor(Color.red);
			if(count%100==0) {
				direct = (int)(Math.random()*3);
			}
			switch(direct) {
			case 0:
				g.drawImage(shark,count,r,80,40,null);
				break;
			case 1:
				if(r>=500) r=500;
				g.drawImage(shark,count,r,80,40,null);r++;
				break;
			case 2:
				if(r<=300) r=300;
				g.drawImage(shark,count,r,80,40,null);r--;
				break;
			default:
				break;
			}
			if(count>810) {
				count = 0;
				r = (int)(Math.random()*200+300);
				direct = 0;
			}
		}
		if(mode == JOptionPane.NO_OPTION) {////////////////////////////////////////////食人魚照片改這
			g.setColor(Color.red);
			if(count%100==0) {
				direct = (int)(Math.random()*3);
			}
			switch(direct) {
			case 0:
				g.drawImage(learning,count,r,70,70,null);
				break;
			case 1:
				if(r>=500) r=500;
				g.drawImage(learning,count,r,70,70,null);r++;
				break;
			case 2:
				if(r<=300) r=300;
				g.drawImage(learning,count,r,70,70,null);r--;
				break;
			default:
				break;
			}
			if(count>810) {
				count = 0;
				r = (int)(Math.random()*200+300);
				direct = 0;
			}
		}
		
	}
	//計時器
	private Timer countdown = new Timer();
	TimerTask task = new TimerTask() {
		public void run() {
			countDown--;
		}
	};
	public void start () {
		countdown.scheduleAtFixedRate(task, 1000, 1000);
	}
	
	
	public void paintGreenFish(Graphics g) {
		if(mode == JOptionPane.YES_OPTION) {
			for(int i = 0;i<3;i++) {
				
				if(gFish[i].count>0&&gFish[i].count%100==0) {
					gFish[i].direct = (int)(Math.random()*3);
				}
				switch(gFish[i].direct) {
				case 0:
					g.drawImage(fishA, gFish[i].count,gFish[i].r,60,45,null);
					break;
				case 1:
					if(gFish[i].r>=500) gFish[i].r=500;
					g.drawImage(fishA, gFish[i].count,gFish[i].r,60,45,null);gFish[i].r++;
					break;
				case 2:
					if(gFish[i].r<=300) gFish[i].r=300;
					g.drawImage(fishA,gFish[i].count,gFish[i].r,60,45,null);gFish[i].r--;
					break;
				default:
					break;
				}
				if(gFish[i].count>810) {
					gFish[i].count = -20;
					gFish[i].r = (int)(Math.random()*200+300);
					gFish[i].direct = 0;
				}
			}
		}
		if(mode == JOptionPane.NO_OPTION) {////////////////////////////////////////////一般魚餌照片改這
			for(int i = 0;i<3;i++) {
				
				if(gFish[i].count>0&&gFish[i].count%100==0) {
					gFish[i].direct = (int)(Math.random()*3);
				}
				switch(gFish[i].direct) {
				case 0:
					g.drawImage(saltyfish, gFish[i].count,gFish[i].r,70,70,null);
					break;
				case 1:
					if(gFish[i].r>=500) gFish[i].r=500;
					g.drawImage(saltyfish, gFish[i].count,gFish[i].r,70,70,null);gFish[i].r++;
					break;
				case 2:
					if(gFish[i].r<=300) gFish[i].r=300;
					g.drawImage(saltyfish,gFish[i].count,gFish[i].r,70,70,null);gFish[i].r--;
					break;
				default:
					break;
				}
				if(gFish[i].count>810) {
					gFish[i].count = -20;
					gFish[i].r = (int)(Math.random()*200+300);
					gFish[i].direct = 0;
				}
			}
		}
		
	}
	//化垃圾
	public void paintGarbage(Graphics g) {
		if(mode == JOptionPane.YES_OPTION) {

			for(int i = 0;i<2;i++) {
				
				if(garbage[i].count>0&&garbage[i].count%100==0) {
					garbage[i].direct = (int)(Math.random()*3);
				}
				switch(garbage[i].direct) {
				case 0:
					g.drawImage(poopoo, garbage[i].count,garbage[i].r,60,50,null);
					break;
				case 1:
					if(garbage[i].r>=500) garbage[i].r=500;
					g.drawImage(poopoo, garbage[i].count,garbage[i].r,60,50,null);garbage[i].r++;
					break;
				case 2:
					if(garbage[i].r<=300) garbage[i].r=300;
					g.drawImage(poopoo,garbage[i].count,garbage[i].r,60,50,null);garbage[i].r--;
					break;
				default:
					break;
				}
				if(garbage[i].count>810) {
					garbage[i].count = -20;
					garbage[i].r = (int)(Math.random()*200+300);
					garbage[i].direct = 0;
				}
			}
		}
		if(mode == JOptionPane.NO_OPTION) {////////////////////////////////////////////垃圾照片改這

			for(int i = 0;i<2;i++) {
				
				if(garbage[i].count>0&&garbage[i].count%100==0) {
					garbage[i].direct = (int)(Math.random()*3);
				}
				switch(garbage[i].direct) {
				case 0:
					g.drawImage(yitzu, garbage[i].count,garbage[i].r,70,70,null);
					break;
				case 1:
					if(garbage[i].r>=500) garbage[i].r=500;
					g.drawImage(yitzu, garbage[i].count,garbage[i].r,70,70,null);garbage[i].r++;
					break;
				case 2:
					if(garbage[i].r<=300) garbage[i].r=300;
					g.drawImage(yitzu,garbage[i].count,garbage[i].r,70,70,null);garbage[i].r--;
					break;
				default:
					break;
				}
				if(garbage[i].count>810) {
					garbage[i].count = -20;
					garbage[i].r = (int)(Math.random()*200+300);
					garbage[i].direct = 0;
				}
			}
		}
	}
	//化特殊魚
	public void paintFast(Graphics g) {
		if(mode == JOptionPane.YES_OPTION) {

			if(fast.count%100==0) {
				fast.direct = (int)(Math.random()*3);
			}
			switch(fast.direct) {
			case 0:
				g.drawImage(fishB,fast.count,fast.r,60,45,null);
				break;
			case 1:
				if(fast.r>=500) fast.r=500;
				g.drawImage(fishB,fast.count,fast.r,60,45,null);fast.r++;
				break;
			case 2:
				if(fast.r<=300) fast.r=300;
				g.drawImage(fishB,fast.count,fast.r,60,45,null);fast.r--;
				break;
			default:
				break;
			}
			if(fast.count>810) {
				fast.count = -100;
				fast.r = (int)(Math.random()*200+300);
				fast.direct = 0;
			}
		}
		if(mode == JOptionPane.NO_OPTION) {////////////////////////////////////////////特殊魚照片改這
			if(fast.count%100==0) {
				fast.direct = (int)(Math.random()*3);
			}
			switch(fast.direct) {
			case 0:
				g.drawImage(pinhan,fast.count,fast.r,70,70,null);
				break;
			case 1:
				if(fast.r>=500) fast.r=500;
				g.drawImage(pinhan,fast.count,fast.r,70,70,null);fast.r++;
				break;
			case 2:
				if(fast.r<=300) fast.r=300;
				g.drawImage(pinhan,fast.count,fast.r,70,70,null);fast.r--;
				break;
			default:
				break;
			}
			if(fast.count>810) {
				fast.count = -100;
				fast.r = (int)(Math.random()*200+300);
				fast.direct = 0;
			}
		}
		
	}
	//畫籃子
	public void paintBasket(Graphics g) {
		if(score==0)
			g.drawImage(basket, 650, 180, this);
		else if(1 <= score && score <= 2)
			g.drawImage(basket1, 650, 159, this);
		else if(3 <= score && score <= 4)	
			g.drawImage(basket2, 650, 159, this);
		else if(5 <= score && score <= 6)	
			g.drawImage(basket3, 650, 159, this);
		else if(7 <= score && score <= 8)	
			g.drawImage(basket4, 650, 135, this);
		else if(9 <= score && score <= 10)	
			g.drawImage(basket5, 650, 135, this);
		else if(11 <= score && score <= 12)	
			g.drawImage(basket6, 650, 135, this);
		else if(13 <= score && score <= 14)	
			g.drawImage(basket7, 650, 105, this);
		else if(15 <= score && score <= 16)	
			g.drawImage(basket8, 650, 105, this);
		else if(17 <= score )	//我把<=18拿掉了
			g.drawImage(basket9, 650, 80, this);
	}
	
	//作Button用的
	public JPanel setpanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, length));
		
		btn_start = new JButton("Start");

		btn_start.setFont(new Font("monospaced", Font.BOLD, 40));
		btn_start.setBorderPainted(false);
		btn_start.setBounds(550, 450, 170, 70);
		btn_start.setActionCommand("S");
		btn_start.addActionListener(this);

		panel.setLayout(null);
		panel.add(btn_start);
		return panel;
	}
	public void action() {
			MouseAdapter l = new MouseAdapter() {
				public void mouseMoved(MouseEvent e) {
					rod_y = e.getY();
				}
				public void mouseClicked(MouseEvent e) {
					
					
				}
			};
			this.addMouseListener(l);
			this.addMouseMotionListener(l);
			
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					if(rod_y+38>r && rod_y-38<r&&count>385-80&&count<385) {
						state = 2;
					    repaint();
						badFish();//要判斷釣到什麼魚，食人魚的話要call badFish  //先設紅色為bad fish
					}	
					for(int i = 0;i<3;i++) {
						if(rod_y>gFish[i].r-38 && rod_y<gFish[i].r+38&&gFish[i].count>385-65&&gFish[i].count<385+35) {
							gFish[i].get =true;getFish();
						}	 
					}
					for(int i = 0;i<2;i++) {
						if(rod_y>garbage[i].r-38 && rod_y<garbage[i].r+38&&garbage[i].count>385-65&&garbage[i].count<385+35) {
							garbage[i].get =true;getGarbage();
						}	 
					}
					if(rod_y>fast.r-38 && rod_y<fast.r+38&&fast.count>385-65&&fast.count<385+35) {
						fast.get =true;getFish();
					}	 
					if(countDown < 0) {
						timeOver();
					}
					if (gameStart ==true) {
						repaint();
						count++;
						
						//綠色魚是否在游泳，是的話count++
						if(gFish[0].count==200&&gFish[1].count==-100) gFish[1].start = true;
						if(gFish[1].count==300&&gFish[2].count==-100) gFish[2].start = true;
						if(gFish[0].count==-100&&count==50) gFish[0].start = true;
						if(gFish[2].count==500&&garbage[0].start==false) garbage[0].start =true;
						if(garbage[0].count==500&&garbage[1].start==false) garbage[1].start =true;
						for(int i = 0;i<3;i++) {
							if(gFish[i].start==true) gFish[i].count++;
						}
						for(int i = 0;i<2;i++) {
							if(garbage[i].start==true) garbage[i].count++;
						}
						if(fast.start==true)fast.count = fast.count+3;
					}
					
				}
			} , intervel, intervel);
			timer.schedule(new TimerTask() {
				public void run() {
					state = 0;
				}
			} ,2000,2000);
		
		
	}
	public void getGarbage() {
		for(int i = 0;i<2;i++) {
			if(garbage[i].get==true) {
				garbage[i].get = false;
				garbage[i].count = -100;
				garbage[i].start= false;
				garbage[i].r = (int)(Math.random()*200+300);
				countDown = countDown-5; 
				
			}
		}
		state = 2;
	}
	public void getFish() {
		if(fast.start==false) fast.start = true;
		if(fast.get==true) {
			fast.get = false;
			fast.count = -100;
			fast.start= false;
			fast.r = (int)(Math.random()*200+300);
			score = score+5;
		}
		for(int i = 0;i<3;i++) {
			if(gFish[i].get==true) {
				gFish[i].get = false;
				gFish[i].count = -100;
				gFish[i].start= false;
				gFish[i].r = (int)(Math.random()*200+300);
				score++; 
				
			}
		}
		
		//count = 0; //next circle
		//r = (int)(Math.random()*400+200);
		//r=-100;先把這個功能保留
		state = 1;
	}
	public void badFish() {
		
		count = -100; //next circle
		r = (int)(Math.random()*400+200);
		//r=-100;
		state = 2;
		gameover();
	}
	public int getScore() {
		return score;
	}
	public void timeOver() {
		if(mode == JOptionPane.YES_OPTION) {
		JDialog.setDefaultLookAndFeelDecorated(true);
		 JOptionPane.showMessageDialog(panel,"時間到  你得到 " + score + " 分",
                "遊戲結束 ", JOptionPane.INFORMATION_MESSAGE);
		}
		if(mode == JOptionPane.NO_OPTION) {
			if(score>59) {
				JDialog.setDefaultLookAndFeelDecorated(true);
				JOptionPane.showMessageDialog(panel,"你的java總成績是 " + score + " 分 ， 恭喜你過了！",
	            "學分到手 ", JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				JDialog.setDefaultLookAndFeelDecorated(true);
				JOptionPane.showMessageDialog(panel,"你的java總成績是 " + score + " 分 ， 明年再來吧！",
		        "重修就好 ", JOptionPane.INFORMATION_MESSAGE);
			}	 
		}
		r=(int)(Math.random()*400+250);
		state = 0;//倉鼠正常臉
		score = 0;//分數歸零
		scoreX  = -315;//讓分數跑到正常的位子
		titleX  = 15;//標題走開
		rod_y = 300;
		count=-100;
		mode = 0;
		direct = 0;
		repaint();
		btn_start.setVisible(true);
		gameStart =false;
		countDown = 60;
		for(int i=0;i<3;i++) {
			gFish[i].count = -100;
			gFish[i].start = false;
		}
		for(int i=0;i<2;i++) {
			garbage[i].count = -100;
			garbage[i].start = false;
		}
		fast.count = -100;
		fast.start = false;
		timer.cancel();
	}
	public void gameover() {
		if(mode == JOptionPane.YES_OPTION) {
		JDialog.setDefaultLookAndFeelDecorated(true);
		 JOptionPane.showMessageDialog(panel,"你釣到食人魚  你好廢",
                "GAME OVER", JOptionPane.ERROR_MESSAGE);
		}
		if(mode == JOptionPane.NO_OPTION) {
			JDialog.setDefaultLookAndFeelDecorated(true);
			 JOptionPane.showMessageDialog(panel,"閔政成功拿到java學分！",
	                "CONGRATULATION", JOptionPane.ERROR_MESSAGE);
			}
		r=(int)(Math.random()*200+300);
		state = 0;//倉鼠正常臉
		score = 0;//分數歸零
		scoreX  = -315;//讓分數跑到正常的位子
		titleX  = 15;//標題走開
		rod_y = 300;
		count=-100;
		direct = 0;
		mode = 0;
		repaint();
		btn_start.setVisible(true);
		gameStart =false;
		countDown = 60;
		for(int i=0;i<3;i++) {
			gFish[i].count = -100;
			gFish[i].start = false;
		}
		for(int i=0;i<2;i++) {
			garbage[i].count = -100;
			garbage[i].start = false;
		}
		fast.count = -100;
		fast.start = false;
		timer.cancel();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == "S") {
			gameStart =true;
			this.Mode();
			state = 0;//倉鼠正常臉
			score = 0;//分數歸零
			scoreX += 1000;//讓分數跑到正常的位子
			titleX += 1000;//標題走開
			rod_y = 300;
			btn_start.setVisible(false);
			this.action();
			this.start();
        }
	}
}