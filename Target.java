public class Target 
{
	public int x, y, width = 55, length = 55;
	public char ch;
	public boolean alive;
	public Target()
	{
		x = 0;
		y = 0;
		alive = true;
	}
	public Target(int X, int Y, char CH)
	{
		alive = true;
		x = X;
		y = Y;
		ch = CH;
	}
}
	
	