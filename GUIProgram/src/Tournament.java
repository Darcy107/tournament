import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;

public class Tournament {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Tournament();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private List<Matchup> matchupList = new ArrayList<>();
	private JPanel matchPanel;
	private GridBagLayout matchLayout;

	/**
	 * Create the application.
	 */
	public Tournament() {
		matchupList.add(new Matchup(new Player("Leader", "g1"), new Player("Leaderasdasad1", "g1")));
		matchupList.add(new Matchup(new Player("Bob", "p2"), new Player("Leade2r", "g1")));
		matchupList.add(new Matchup(new Player("Uncle", "d2"), new Player("Leader3", "g1")));
		matchupList.add(new Matchup(new Player("Help", "b2"), new Player("Leade4r", "g1")));
		matchupList.add(new Matchup(new Player("Please", "s3"), new Player("Lea5der", "g1")));
		matchupList.add(new Matchup(new Player("Okay", "g3"),new Player("Lead6er", "g1")));
		matchupList.add(new Matchup(new Player("That", "b5"), new Player("Lead7er", "g1")));
		matchupList.add(new Matchup(new Player("Is", "s5"), new Player("Leade8r", "g1")));
		matchupList.add(new Matchup(new Player("Enough", "m"), new Player("Lea9der", "g1")));
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JFrame window = new JFrame("Test");
		window.setTitle("My Example");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        //window.setMinimumSize(new Dimension(350, 150));

        JPanel panelMain = new JPanel(new BorderLayout());
        window.add(panelMain);


        matchLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        matchPanel = new JPanel(matchLayout);
        matchPanel.setBackground(new Color(0, 220, 250));


        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 50, 5, 5);

        gbc.gridx = 0;
        JLabel lbl = new JLabel("Tournament");
        matchLayout.setConstraints(lbl, gbc);
        matchPanel.add(lbl);
        
        gbc.insets = new Insets(5, 5, 5, 5);


        
        // CHANGE THIS
        int maxX = 300;
        int maxY = 300;

        for(int i = 0; i < matchupList.size(); i++) {
        	final String player1 = matchupList.get(i).getPlayer1().getName();
            final String player2 = matchupList.get(i).getPlayer2().getName();
        	
        	gbc.insets = new Insets(5, 50, 5, 5);
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.gridy = (i*2) + 1;

            
            // player 1
            gbc.gridx = 0;
            lbl = new JLabel(player1, SwingConstants.RIGHT);
            matchLayout.setConstraints(lbl, gbc);
            matchPanel.add(lbl); 
            
            // player 2
            gbc.insets = new Insets(5, 50, 50, 5);
            gbc.gridy = (i*2) + 2;
            gbc.gridx = 0;
            lbl = new JLabel(player2, SwingConstants.RIGHT);
            matchLayout.setConstraints(lbl, gbc);
            matchPanel.add(lbl);
            
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridy = (i*2) + 1;
            gbc.gridx = 1;
            
            JButton button = new JButton("Winner");
            button.addActionListener(new Listener());
            matchLayout.setConstraints(button, gbc);
            matchPanel.add(button);

            gbc.gridy = (i*2) + 2;
            gbc.gridx = 1;
            button = new JButton("Winner");
            button.addActionListener(new Listener());
            matchLayout.setConstraints(button, gbc);
            matchPanel.add(button);

        }
        // add void panel to allow vertical scrolling
        gbc.gridy = maxY;
        gbc.gridx = 0;
        JPanel voidYPanel = new JPanel();
        voidYPanel.setBackground(new Color(15, 23, 25));
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        matchLayout.setConstraints(voidYPanel, gbc);
        matchPanel.add(voidYPanel);
        
        // add void panel to allow for horizontal scrolling
        JPanel voidXPanel = new JPanel();
        voidXPanel.setBackground(new Color(250, 250, 250));
        gbc.gridy = 0;
        gbc.gridx = maxX;
        gbc.weighty = 0.0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        matchLayout.setConstraints(voidXPanel, gbc);
        matchPanel.add(voidXPanel);

        // add scroll panel
        JScrollPane scrollPanel = new JScrollPane(matchPanel);
        panelMain.add(scrollPanel, BorderLayout.CENTER);

        window.setVisible(true); 
        matchPanel.revalidate();
        matchPanel.repaint();
	}
	
	private class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
	    {  
			String player = "error";
			int x = 0;
			int y = 0;
			int height = 0;
			boolean twoPlayers = false;
			Component[] componentList = matchPanel.getComponents();
			// get the x and y of the button
            for(Component c : componentList){
            	if (c.equals(e.getSource())) {
            		GridBagConstraints gbc = matchLayout.getConstraints(c);
            		x = gbc.gridx;
            		y = gbc.gridy;
            		
            	}
            }
            // get the winning player
            for(Component c : componentList){
            	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
                //Find the components you want to remove
            	if (gbc2.gridx == (x-1) && gbc2.gridy == y) {
            		player = ((JLabel) c).getText();
            	}

            }
            
            
            System.out.println("firstx= " + x + " as" + y);	
            // assume set of x is 1,3,5,7,8 etc
            // is top player
            if ((y-1) % Math.pow(2, ((x+1)/2)) == 0) {
            	System.out.println("yaba");
            	
            }
            // is bottom player
            else {
            	y -= Math.pow(2, ((x-1)/2));
            }
            x += 1;
            height = (int) Math.pow(2, ((x+1)/2));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 50, 5);
            gbc.gridx = x;
            gbc.gridy = y;
            gbc.gridheight = height;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel lblTitle2 = new JLabel(player, SwingConstants.RIGHT);;
            
            matchLayout.setConstraints(lblTitle2, gbc);
            
            //remove the old label if it exists
            for(Component c : componentList){
            	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
                //remove player JLabel if it exists
            	if (gbc2.gridx == x && gbc2.gridy == y) {
            		matchPanel.remove(c);
            	}
            	// remove button if it exists
            	else if (gbc2.gridx == (x + 1) && gbc2.gridy == y) {
            		matchPanel.remove(c);
            	}

            }
            
            matchPanel.add(lblTitle2);
            int buttonY = 0;
            System.out.println("button check, x=" + x + " y=" + y );
            if ((y-1) % Math.pow(2, ((x+3)/2)) == 0) {
            	System.out.println("top");
            	for(Component c : componentList){
                	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
                    //Find the components you want to remove
                	if (gbc2.gridx == x && gbc2.gridy == (y+height)) {
//                		player = ((JLabel) c).getText();
//                		System.out.println(player + "madeit");
                		buttonY = y+height;
                		twoPlayers = true;
                	}

                }	
            }
            // is bottom player
            else {
            	System.out.println("bottom");
            	for(Component c : componentList){
                	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
                    //Find the components you want to remove
                	if (gbc2.gridx == x && gbc2.gridy == (y-height)) {
//                		player = ((JLabel) c).getText();
//                		System.out.println(player + "madeit");
                		buttonY = y-height;
                		twoPlayers = true;
                	}

                }	
            }
            
            if (twoPlayers) {
            	System.out.println("twoplayers is treue");
            	x += 1;
                gbc.gridx = x;
                JButton button = new JButton("Winner");
                button.addActionListener(new Listener());
                matchLayout.setConstraints(button, gbc);
                matchPanel.add(button);
                
                gbc.gridy = buttonY;
                button = new JButton("Winner");
                button.addActionListener(new Listener());
                matchLayout.setConstraints(button, gbc);
                matchPanel.add(button);
            }
            
            System.out.println("final x: " + x + " final y:" + y + " final height: " + Math.pow(2, ((x+1)/2)));	
            
			matchPanel.revalidate();
            matchPanel.repaint();
	    }
	}

}
