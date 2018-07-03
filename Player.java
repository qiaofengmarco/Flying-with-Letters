import java.util.*;
public class Player
{
	public int x, y, width = 150, length = 120, speed = 6;
	public ArrayList<Bullet> bullets;
	public Player()
	{
		x = 0;
		y = 0;
		bullets = new ArrayList<Bullet>();
	}
	public Player(int X, int Y)
	{
		x = X;
		y = Y;
		bullets = new ArrayList<Bullet>();
	}
	public boolean eat(Target t)
	{
		int mid_x = x + width / 2, mid_y = y + length / 2;
		
		if ((mid_x >= t.x) && (mid_x <= t.x + t.width) && (mid_y >= t.y) && (mid_y <= t.y + t.length))
			return true;
		return false;
	}
	public boolean eat(Bomb t)
	{
		int mid_x = x + width / 2, mid_y = y + length / 2;
		
		if ((mid_x >= t.x) && (mid_x <= t.x + t.width) && (mid_y >= t.y) && (mid_y <= t.y + t.length))
			return true;
		return false;		
	}
} 