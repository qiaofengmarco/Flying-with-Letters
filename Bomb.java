public class Bomb 
{
	public int x, y, width = 55, length = 55;
	public static int speed = 6;
	public boolean alive;
	public Bomb()
	{
		x = 0;
		y = 0;
		alive = true;
	}
	public Bomb(int X, int Y)
	{
		alive = true;
		x = X;
		y = Y;
	}
}