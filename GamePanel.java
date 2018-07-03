import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.Timer;
public class GamePanel extends JPanel implements ActionListener
{
	private ArrayList<Target> targets = new ArrayList<Target>();
	private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	private ArrayList<String> words = new ArrayList<String>();
	private ArrayList<Double> word_scale = new ArrayList<Double>();
	private ArrayList<Explosion> exps = new ArrayList<Explosion>();
	public String current_word = "";
	public String current_char = "", correct_char = "";
	public boolean word_change = false, isFirst = true, empty = true, fire = false, correct = false, wrong = false, wrong_empty = false, touch_bomb = false;
	public int target_get = 0, target_loss = 0, counter = 0, wrong_time = 0, counter1 = 0;
	public long score = 0;
	public double correctness;
	private Trie trie;
	private String nextWords;
	private Timer timer;
	private Map<String, String> pressedKeys = new HashMap<String, String>();
	public void createTarget()
	{
		int w = getWidth();
		int ww = GameFrame.gf.player.width;
		int www = w - ww - 15;
		if (isFirst || word_change)
			nextWords = trie.possibleNext();
		int select = 0, TargetNum, BombNum = 0;
		double randomTemp;
		RepaintThread.target_counter++;	
		HashSet<Integer> tg = new HashSet<Integer>();
		HashSet<Integer> bm = new HashSet<Integer>();
		if (RepaintThread.target_counter >= 30) 
		{
			randomTemp = Math.random();
			if ((int)(randomTemp * 7) == 1)
				TargetNum = 2;
			else
				TargetNum = 1;
			if ((int)(randomTemp * 100) == 5)
				BombNum = 2;
			else if (((int)(randomTemp * 100) == 7) || ((int)(randomTemp * 100) == 9))
				BombNum = 1;
			else
				BombNum = 0;
			int x = 0;
			boolean flag1 = false, flag2 = false;
			while (TargetNum > 0)
			{
				flag1 = false;
				x = ((int)(Math.random() * w)) % www + ww / 2;
				Integer x_temp = new Integer(x);
				if ((tg.size() == 0) || (!tg.contains(x_temp)))
				{
					for (Integer x1: tg)
					{
						if (((int)Math.abs(x1.intValue() - x)) < 40)
						{
							flag1 = true;
							break;
						}
					}
					if (!flag1)
					{
						TargetNum--;
						tg.add(x_temp);
					}
				}
			}
			for (Integer x1: tg)
			{
				if (((int)(Math.random() * 10)) <= 4)
				{
					if (nextWords.length() == 1)
						select = 0;
					else
						select = (int)(Math.random() * nextWords.length());
					targets.add(new Target(x1.intValue(), -60, nextWords.charAt(select)));
				}
				else
					targets.add(new Target(x1.intValue(), -60, (char)(((int)(Math.random() * 26)) + 97)));
			}
			while (BombNum > 0)
			{
				flag2 = false;
				x = ((int)(Math.random() * w)) % www + ww / 2;
				Integer x_temp = new Integer(x);
				if ((bm.size() == 0) || ((!tg.contains(x_temp)) && (!bm.contains(x_temp))))
				{
					for (Integer x1: tg)
					{
						if (((int)Math.abs(x1.intValue() - x)) < 40)
						{
							flag2 = true;
							break;
						}
					}
					for (Integer x1: bm)
					{
						if (((int)Math.abs(x1.intValue() - x)) < 40)
						{
							flag2 = true;
							break;
						}
					}
					if (!flag2)
					{
						BombNum--;
						bm.add(x_temp);
					}
				}
			}
			for (Integer x1: bm)
			{
				bombs.add(new Bomb(x1, -60));
			}
			RepaintThread.target_counter = 0;
			tg.clear();
			bm.clear();
		}	
	}
	public void targetDrop(Graphics g) 
	{
		Target t2;
		Bomb b2;
		String s;
		Image image;
		int height = getHeight();
		for (Target t: targets) 
		{
			s = String.format("/image/%s.png", t.ch);
			image = new ImageIcon(GamePanel.class.getResource(s)).getImage();
			g.drawImage(image, t.x, t.y, 55, 55, this);
			t.y += Target.speed;
		}
		for (int i = 0; i < targets.size(); i++)
		{
			t2 = targets.get(i);
			if (t2.y >= height)
			{
				t2.alive = false;
				targets.remove(t2);
				i = 0;
			}
		}
		for (Bomb b: bombs)
		{
			image = new ImageIcon(GamePanel.class.getResource("/image/bomb.png")).getImage();
			g.drawImage(image, b.x, b.y, 60, 60, this);
			b.y += Bomb.speed;
		}
		for (int i = 0; i < bombs.size(); i++)
		{
			b2 = bombs.get(i);
			if (b2.y >= height)
			{
				b2.alive = false;
				bombs.remove(b2);
				i = 0;
			}
		}		
	}
	public void shoot(Graphics g) 
	{
		Bullet b2;
		RepaintThread.counter += 10;
		Image missile = new ImageIcon(
				GamePanel.class.getResource("/image/fire.png")).getImage();
		if ((RepaintThread.counter > 80) && (fire)) 
		{
			Bullet b = new Bullet(GameFrame.gf.player.x + GameFrame.gf.player.width / 2 - 60 / 2,
					GameFrame.gf.player.y - 60 / 2);
			GameFrame.gf.player.bullets.add(b);
			RepaintThread.counter = 0;
		}
		for (Bullet b1: GameFrame.gf.player.bullets) 
		{
			g.drawImage(missile, b1.x, b1.y, 60, 60, this);
			b1.y -= Bullet.speed;
		}
		for (int i = 0; i < GameFrame.gf.player.bullets.size(); i++)
		{
			b2 = GameFrame.gf.player.bullets.get(i);
			if (b2.y >= getHeight())
			{
				b2.alive = false;
				GameFrame.gf.player.bullets.remove(b2);
				i = 0;
			}
		}
	}
	public void boom(Graphics g) 
	{
		Explosion e;
		Image explosion = new ImageIcon(
					GamePanel.class.getResource("/image/exp.png"))
					.getImage();
		for (Explosion exp: exps) 
		{
			g.drawImage(explosion, exp.x, exp.y, 70, 49, this);
			exp.life_time--;
		}
		for (int i = 0; i < exps.size(); i++)
		{
			e = exps.get(i);
			if (e.life_time <= 0)
			{
				exps.remove(e);
				i = 0;
			}
		}
	}
	public void paintComponent(Graphics g) 
	{
		Bullet b2;
		Target t2;
		Bomb bm2;
		Player p;
		int pp;
		if (correct || wrong || touch_bomb)
		{
			if (counter1 > 50)
			{
				word_change = true;
				correct = false;
				wrong = false;
				wrong_empty = false;
				touch_bomb = false;
				counter1 = 0;
				correct_char = "";
				if (wrong_time > 0)
					wrong_time = 0;
			}
			else
			{
				counter1++;
				word_change = false;
			}
		}
		else
			word_change = false;
		super.paintComponent(g);
		if (counter < 1)
			counter++;
		else
			isFirst = false;
		
		//eat the corret one?
		Double this_scale;
		p = GameFrame.gf.player;


		createTarget();
		targetDrop(g);
		
		//draw player
		Image image = new ImageIcon(
				GamePanel.class.getResource("/image/dragon.png"))
				.getImage();
		g.drawImage(image, p.x, p.y, 150, 120, this);
				
		shoot(g);
		
		if (bombs.size() > 0)
		{
			for (int i = 0; i < bombs.size(); i++)
			{
				bm2 = bombs.get(i);
				if (p.eat(bm2))
				{
					touch_bomb = true;
					bombs.remove(bm2);
					break;
				}
			}
		}
		
		if ((targets.size() > 0) && !(touch_bomb))
		{
			for (int i = 0; i < targets.size(); i++) 
			{
				t2 = targets.get(i);
				if (p.eat(t2)) 
				{
					pp = trie.searching(t2.ch);
					if (pp >= 0)
					{
						this_scale = word_scale.get(pp);
						score += (int) ((current_word.length() + 1) * 100 * this_scale.doubleValue());
						word_scale.set(pp, this_scale.doubleValue() * 0.5);
						correct_char = String.valueOf(t2.ch);
						current_word = "";
						current_char = "";
						word_change = true;
						correct = true;
						wrong = false;
						empty = true;
					}
					else if (pp == -1)
					{
						//score += 10;
						current_word += t2.ch;
						current_char = String.valueOf(t2.ch);
						word_change = true;
						empty = false;
						correct = false;
						wrong = false;
					}
					else if (pp == -2)
					{
						/*if (score > 10 * current_word.length())
							score -= 10 * current_word.length();
						else
							score = 0;*/
						if (current_word.equals(""))
							wrong_empty = true;
						current_word = "";
						current_char = "";
						word_change = true;
						empty = true;
						correct = false;
						wrong = true;
						wrong_time++;
					}
					t2.alive = false;
					targets.remove(t2);
					break;
				}
			}
		}

		boom(g);

		//bullet hit?
		for (Bullet b: p.bullets) 
		{
			for (Bomb b1: bombs)
			{
				if (touch_bomb || b.hit(b1))
				{
					b.alive = false;
					b1.alive = false;
					touch_bomb = true;
					exps.add(new Explosion(b1.x - 5, b1.y + 6));
				}
			}
			for (Target t: targets) 
			{
				if (touch_bomb)
				{
					b.alive = false;
					t.alive = false;
					exps.add(new Explosion(t.x - 5, t.y + 6));					
				}
				else if (b.hit(t)) 
				{
					b.alive = false;
					t.alive = false;
					exps.add(new Explosion(t.x - 5, t.y + 6));
					break;
				}
			}
		}
		if ((p.bullets.size() == 0) && (touch_bomb))
		{
			for (Bomb b1: bombs)
				exps.add(new Explosion(b1.x - 5, b1.y + 6));
			for (Target t: targets) 
				exps.add(new Explosion(t.x - 5, t.y + 6));					
		}
		if (touch_bomb)
		{
			current_word = "";
			current_char = "";
			word_change = true;
			correct = false;
			wrong = false;
			empty = true;
			trie.resetNode();
			targets.clear();
			bombs.clear();
		}

		//remove
		for (int i = 0; i < p.bullets.size(); i++)
		{
			b2 = GameFrame.gf.player.bullets.get(i);
			if (!b2.alive)
			{
				GameFrame.gf.player.bullets.remove(b2);
				i = 0;
			}
		}
		for (int i = 0; i < targets.size(); i++)
		{
			t2 = targets.get(i);
			if (!t2.alive)
			{
				targets.remove(t2);
				i = 0;
			}
		}
		for (int i = 0; i < bombs.size(); i++)
		{
			bm2 = bombs.get(i);
			if (!bm2.alive)
			{
				bombs.remove(bm2);
				i = 0;
			}
		}
		
	}
	private class AnimationAction extends AbstractAction implements ActionListener
	{
		private String key_code;
		private boolean pressed;
		public AnimationAction(String kc, boolean p)
		{
			key_code = kc;
			pressed = p;
		}
		public void actionPerformed(ActionEvent e)
		{
			handleKeyEvent(key_code, pressed);
		}
	}
	private void handleKeyEvent(String key_code, boolean pressed)
	{
		if (pressed)
			pressedKeys.put(key_code, key_code);
		else
		{
			if (key_code.equals("SPACE"))
				fire = false;
			pressedKeys.remove(key_code);
		}
   		if (pressedKeys.size() == 1)
   		{
   			timer.start();
   		}
   		if (pressedKeys.size() == 0)
   		{
   			timer.stop();
   		}		
	}
	public void removekeys()
	{
		if (pressedKeys.size() > 0)
			pressedKeys.clear();
	}
	public void actionPerformed(ActionEvent e)
	{
		for (String s: pressedKeys.values())
		{
			if (s.equals("UP") || s.equals("W"))
			{
				if (GameFrame.gf.player.y >= GameFrame.gf.player.speed)
					GameFrame.gf.player.y -= GameFrame.gf.player.speed;				
			} 
			else if (s.equals("DOWN") || s.equals("S"))
			{
				if (GameFrame.gf.player.y + GameFrame.gf.player.length <= getHeight() - GameFrame.gf.player.speed)
					GameFrame.gf.player.y += GameFrame.gf.player.speed;				
			}
			else if (s.equals("LEFT") || s.equals("A"))
			{
				if (GameFrame.gf.player.x >= GameFrame.gf.player.speed)
					GameFrame.gf.player.x -= GameFrame.gf.player.speed;					
			}
			else if (s.equals("RIGHT") || s.equals("D"))
			{
				if (GameFrame.gf.player.x + GameFrame.gf.player.width <= getWidth() - GameFrame.gf.player.speed)
					GameFrame.gf.player.x += GameFrame.gf.player.speed;				
			}
			else if (s.equals("SPACE"))
			{
				fire = true;
			}
		}
		//removekeys();
	}
	public GamePanel()
	{
		trie = new Trie();
		words.add("apple");
		word_scale.add(new Double(1.0));
		words.add("bananas");
		word_scale.add(new Double(1.0));
		words.add("fuck");
		word_scale.add(new Double(1.0));
		words.add("cat");
		word_scale.add(new Double(1.0));
		words.add("bob");
		word_scale.add(new Double(1.0));
		words.add("dad");
		word_scale.add(new Double(1.0));
		words.add("god");
		word_scale.add(new Double(1.0));
		words.add("dog");
		word_scale.add(new Double(1.0));
		words.add("bad");
		word_scale.add(new Double(1.0));
		words.add("flying");
		word_scale.add(new Double(1.0));
		for (String w: words)
			trie.insert(w.toLowerCase());
		setBackground(Color.WHITE);
		setFocusable(true);
		timer = new Timer(10, this);
		timer.setInitialDelay(0);
		InputMap im = this.getInputMap();
		ActionMap am = this.getActionMap();
		Action PressedSpaceAction = new AnimationAction("SPACE", true);
		Action ReleasedSpaceAction = new AnimationAction("SPACE", false);
		Action PressedUpAction = new AnimationAction("UP", true);
		Action ReleasedUpAction = new AnimationAction("UP", false);
		Action PressedDownAction = new AnimationAction("DOWN", true);
		Action ReleasedDownAction = new AnimationAction("DOWN", false);
		Action PressedLeftAction = new AnimationAction("LEFT", true);
		Action ReleasedLeftAction = new AnimationAction("LEFT", false);
		Action PressedRightAction = new AnimationAction("RIGHT", true);
		Action ReleasedRightAction = new AnimationAction("RIGHT", false);
		Action PressedWAction = new AnimationAction("W", true);
		Action ReleasedWAction = new AnimationAction("W", false);
		Action PressedDAction = new AnimationAction("D", true);
		Action ReleasedDAction = new AnimationAction("D", false);
		Action PressedSAction = new AnimationAction("S", true);
		Action ReleasedSAction = new AnimationAction("S", false);
		Action PressedAAction = new AnimationAction("A", true);
		Action ReleasedAAction = new AnimationAction("A", false);
		im.put(KeyStroke.getKeyStroke("pressed SPACE"),"pressed space");
		im.put(KeyStroke.getKeyStroke("released SPACE"), "released space");
        im.put(KeyStroke.getKeyStroke("pressed UP"), "UP");
        im.put(KeyStroke.getKeyStroke("pressed DOWN"), "DOWN");
        im.put(KeyStroke.getKeyStroke("pressed LEFT"), "LEFT");
        im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "RIGHT");		
        im.put(KeyStroke.getKeyStroke("pressed W"), "W");
        im.put(KeyStroke.getKeyStroke("pressed S"), "S");
        im.put(KeyStroke.getKeyStroke("pressed A"), "A");
		im.put(KeyStroke.getKeyStroke("pressed D"), "D");
		im.put(KeyStroke.getKeyStroke("released UP"), "released UP");
		im.put(KeyStroke.getKeyStroke("released DOWN"), "released DOWN");
		im.put(KeyStroke.getKeyStroke("released LEFT"), "released LEFT");
		im.put(KeyStroke.getKeyStroke("released RIGHT"), "released RIGHT");
		im.put(KeyStroke.getKeyStroke("released W"), "released W");
		im.put(KeyStroke.getKeyStroke("released A"), "released A");
		im.put(KeyStroke.getKeyStroke("released S"), "released S");
		im.put(KeyStroke.getKeyStroke("released D"), "released D");
		am.put("pressed space", PressedSpaceAction);
		am.put("released space", ReleasedSpaceAction);
		am.put("UP", PressedUpAction);
		am.put("DOWN", PressedDownAction);
		am.put("LEFT", PressedLeftAction);
		am.put("RIGHT", PressedRightAction);
		am.put("released UP", ReleasedUpAction);
		am.put("released DOWN", ReleasedDownAction);
		am.put("released LEFT", ReleasedLeftAction);
		am.put("released RIGHT", ReleasedRightAction);
		am.put("W", PressedWAction);
		am.put("A", PressedAAction);
		am.put("S", PressedSAction);
		am.put("D", PressedDAction);
		am.put("released W", ReleasedWAction);
		am.put("released A", ReleasedAAction);
		am.put("released S", ReleasedSAction);
		am.put("released D", ReleasedDAction);
	}
}