public class Bullet
{
	public int x, y, width = 75, length = 75;
	public boolean alive;
	public Bullet()
	{
		x = 0;
		y = 0;
		alive = true;
	}
	public Bullet(int X, int Y)
	{
		x = X;
		y = Y;
		alive = true;
	}
	public boolean hit(Target t)
	{
		int mid_x = x + width / 2;
		int mid_y = y + length / 2;
		if ((mid_x >= t.x) && (mid_x <= t.x + t.width) && (y >= t.y) && (y <= t.y + t.length))
			return true;
		return false;		
	}
}