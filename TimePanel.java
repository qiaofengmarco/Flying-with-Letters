import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
public class TimePanel extends TimerTask
{
	public JPanel panel;
	public JLabel label;
	public String now;
	public int minute_counter = 3, second_counter = 0; 
	public boolean pause = false;
	public TimePanel()
	{
		minute_counter = 3;
		second_counter = 0;
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setSize(135, 45);
		label = new JLabel("3 : 00", SwingConstants.CENTER);
		label.setBounds(0, 0, 135, 45);
		label.setFont(new Font("Jokerman", Font.BOLD, 30));
		label.setForeground(Color.GRAY);
		panel.add(label);
	}
	@Override
	public void run()
	{
		if (second_counter < 10)
			now = String.format("%d : 0%d", minute_counter, second_counter);
		else
			now = String.format("%d : %d", minute_counter, second_counter);
		if (second_counter == 0)
		{
			if (minute_counter > 0)
			{
				minute_counter--;
				second_counter = 59;
			}
			else
				GameFrame.gf.pause = true;
		}
		else
			second_counter--;
		label.setText(now);
	}
}