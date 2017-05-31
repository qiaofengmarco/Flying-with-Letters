import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.*;

public class GradePanel extends JPanel {

	public GradePanel() 
	{
		setBackground(Color.WHITE);
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		removeAll();
		int width = getWidth(), length = getHeight();
		StringBuffer sb = new StringBuffer(GameFrame.gf.gp.score + "");
		JLabel l = new JLabel(new String(sb), SwingConstants.CENTER);
		l.setBounds(0, 0, width, length);
		l.setFont(new Font("Jokerman", Font.BOLD, 30));
		add(l);
	}

}
