import java.util.*;
public class RepaintThread extends Thread 
{

	public static int target_counter = 0;
	public static int counter = 0;
	private boolean pause;
	private ResultFrame rf;
	public void run() 
	{
		while (true)
		{
			pause = GameFrame.gf.pause;
			if (!pause)
			{
				GameFrame.gf.big.repaint();
				try 
				{
					Thread.sleep(20);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				GameFrame.gf.frame.setVisible(false);
				rf = new ResultFrame();
				rf.setVisible(true);
				break;
			}
		}
		interrupt();
	}
}