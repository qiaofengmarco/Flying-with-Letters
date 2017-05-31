import java.awt.*;
import java.applet.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
public class GameFrame
{
	public static GameFrame gf;
	public JFrame frame;
	public GamePanel gp;
	public RepaintThread repaint;
	public Player player;
	public GradePanel grade;
	public WordPanel wp;
	public TimePanel tp;
	public JPanel big, top;
	public boolean pause = false;
	public static void main(String[] args)
	{
		gf = new GameFrame();
		gf.frame.setVisible(true);
	}
	public GameFrame()
	{
		pause = false;
		frame = new JFrame();
		frame.setTitle("Flying with Letters");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = frame.getContentPane();
		frame.setResizable(false);
		frame.setSize(900, 935);
		container.setSize(900, 900);
		gp = new GamePanel();
		grade = new GradePanel();
		wp = new WordPanel();
		tp = new TimePanel();
		big = new JPanel();
		top = new JPanel();
		big.setBounds(0, 0, 900, 900);
		big.setBackground(Color.WHITE);
		big.setLayout(null);
		top.setBackground(Color.WHITE);
		top.setLayout(null);
		wp.setSize(630, 45);
		grade.setSize(135, 45);
		gp.setSize(900, 855);
		top.setSize(900, 45);
		top.setBounds(0, 0, 900, 45);
		top.add(wp);
		top.add(tp.panel);
		top.add(grade);
		wp.setLocation(0, 0);
		tp.panel.setLocation(630, 0);
		grade.setLocation(765, 0);
		gp.setFocusable(true);
		gp.setLocation(0, 45);
		top.setLocation(0, 0);
		big.add(gp);
		big.add(top);
		container.add(big);
		Timer timer = new Timer();
		timer.schedule(tp, 0, 1000);
		player = new Player(container.getWidth() / 2 -  150 / 2, container.getHeight() - 180);
		repaint = new RepaintThread();
		repaint.start();
	}
}