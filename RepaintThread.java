
public class RepaintThread extends Thread 
{

	public static int target_counter = 0;
	public static int counter = 0;
	private boolean pause;
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
					Thread.sleep(10);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

}