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
	public ArrayList<Target> targets = new ArrayList<Target>();
	public ArrayList<String> words = new ArrayList<String>();
	public ArrayList<Explosion> exps = new ArrayList<Explosion>();
	public String current_word = "";
	public String current_char = "", correct_char = "";
	public boolean word_change = false, isFirst = true, empty = true, fire = false, correct = false, wrong = false, wrong_empty = false;
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
		int select = 0;
		RepaintThread.target_counter++;	
		int TargetNum;
		HashSet<Integer> tg = new HashSet<Integer>();
		if (RepaintThread.target_counter >= 60) 
		{
			if ((int)(Math.random() * 7) == 1)
				TargetNum = 2;
			else
				TargetNum = 1;
			int x = ((int)(Math.random() * w)) % www + ww / 2;
			boolean flag1 = false;
			while (TargetNum > 0)
			{
				flag1 = false;
				x = ((int)(Math.random() * w)) % www + ww / 2;	
				if (!tg.contains(new Integer(x)))
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
						tg.add(new Integer(x));
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
					targets.add(new Target(x1, -60, nextWords.charAt(select)));
				}
				else
					targets.add(new Target(x1, -60, (char)(((int)(Math.random() * 26)) + 97)));
			}
			RepaintThread.target_counter = 0;
			tg.clear();
		}	
	}
	public void targetDrop(Graphics g) 
	{
		Target t2;
		String s;
		for (Target t: targets) 
		{
			s = String.format("/image/%s.png", t.ch);
			Image image = new ImageIcon(GamePanel.class.getResource(s)).getImage();
			g.drawImage(image, t.x, t.y, 55, 55, this);
			t.y += 3;
		}
		for (int i = 0; i < targets.size(); i++)
		{
			t2 = targets.get(i);
			if (t2.y >= getHeight())
			{
				t2.alive = false;
				targets.remove(t2);
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
		if ((RepaintThread.counter > 120) && (fire)) 
		{
			Bullet b = new Bullet(GameFrame.gf.player.x + GameFrame.gf.player.width / 2 - 60 / 2,
					GameFrame.gf.player.y - 60 / 2);
			GameFrame.gf.player.bullets.add(b);
			RepaintThread.counter = 0;
		}
		for (Bullet b1: GameFrame.gf.player.bullets) 
		{
			g.drawImage(missile, b1.x, b1.y, 60, 60, this);
			b1.y -= 20;
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
		Player p;
		int pp;
		if (correct || wrong)
		{
			if (counter1 > 50)
			{
				word_change = true;
				correct = false;
				wrong = false;
				wrong_empty = false;
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
		p = GameFrame.gf.player;
		if (targets.size() > 0)
		{
			for (int i = 0; i < targets.size(); i++) 
			{
				t2 = targets.get(i);
				if (p.eat(t2)) 
				{
					String s = String.valueOf(t2.ch);
					pp = trie.searching(s);
					if (pp == 1)
					{
						score += current_word.length() * 5;
						correct_char = String.valueOf(t2.ch);
						current_word = "";
						current_char = "";
						word_change = true;
						correct = true;
						wrong = false;
						empty = true;
					}
					else if (pp == 0)
					{
						score += 5;
						current_word += t2.ch;
						current_char = String.valueOf(t2.ch);
						word_change = true;
						empty = false;
						correct = false;
						wrong = false;
					}
					else
					{
						if (score > 5 * current_word.length())
							score -= 5 * current_word.length();
						else
							score = 0;
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
			for (Target t: targets) 
			{
				if (b.hit(t)) 
				{
					b.alive = false;
					t.alive = false;
					score += 10;
					exps.add(new Explosion(t.x - 5, t.y + 6));
					break;
				}
			}
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
		
		createTarget();
		targetDrop(g);
		
		//draw player
		Image image = new ImageIcon(
				GamePanel.class.getResource("/image/dragon.png"))
				.getImage();
		g.drawImage(image, p.x, p.y, 150, 120, this);
				
		shoot(g);
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
		words.add("bananas");
		words.add("fuck");
		words.add("cat");
		words.add("bob");
		words.add("dad");
		words.add("god");
		words.add("dog");
		words.add("bad");
		words.add("flying");
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