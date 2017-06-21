import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
public class ResultFrame extends JFrame
{
	JLabel label1, label2;
	JPanel p = new JPanel(), p1 = new JPanel(), p2 = new JPanel();
	long current = GameFrame.gf.gp.score, best = 0, newBest = 0;
	private File dir, f;
	public ResultFrame()
	{
		dir = new File("");
		f = new File(dir, "Bst.txt");
		if (!f.exists())
		{
			best = 0;
			try 
			{
				f.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			String line;
			try
			{
				BufferedReader reader =  new BufferedReader(new FileReader(f));
				line = reader.readLine();
				best = Integer.parseInt(line);
				reader.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}		
		}
		if (current > best)
			newBest = current;
		else
			newBest = best;
		setTitle("Result");
		setLayout(null);
		setSize(800, 435);
		setBackground(Color.WHITE);
		setLayout(null);
		
		p.setBackground(Color.WHITE);
		p.setLayout(null);
		p.setBounds(0, 0, 800, 435);
		p.add(p1);
		p.add(p2);
		add(p);
		String s1 = String.format("The Current Score: %d", current);
		String s2 = String.format("The Best Score: %d", newBest);
		
		p1.setBackground(Color.WHITE);
		p1.setLayout(null);
		p1.setSize(800, 200);
		label1 = new JLabel(s1, SwingConstants.CENTER);
		label1.setBounds(0, 0, 800, 200);
		label1.setFont(new Font("Jokerman", Font.BOLD, 50));
		label1.setForeground(Color.BLACK);
		p1.add(label1);
		
		p2.setBackground(Color.WHITE);
		p2.setLayout(null);
		p2.setSize(800, 200);
		label2 = new JLabel(s2, SwingConstants.CENTER);
		label2.setBounds(0, 0, 800, 200);
		label2.setFont(new Font("Jokerman", Font.BOLD, 50));
		label2.setForeground(Color.BLACK);
		p2.add(label2);
		p1.setLocation(0, 0);
		p2.setLocation(0, 200);
		
		if (current > best)
		{
			try 
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(f));
				writer.write(String.format("%d", newBest));
				writer.close();
			}	
			catch (IOException ee)
			{
				ee.printStackTrace();
			}
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				setVisible(false);
				dispose();
				GameFrame.gf = null;
				System.exit(0);
			}
		}*/
	}
}