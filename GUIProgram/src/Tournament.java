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
		// list for testing purposes
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
        matchPanel = new JPanel(matchLayout);
        matchPanel.setBackground(new Color(0, 220, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridy = 0;
        // makes a 50 unit gap on the left
        gbc.insets = new Insets(5, 50, 5, 5);
        gbc.gridx = 0;
        
        JLabel lbl = new JLabel("Tournament");
        matchLayout.setConstraints(lbl, gbc);
        matchPanel.add(lbl);

        // CHANGE THIS to the max list size when it gets read in from a file
        int maxX = 300;
        int maxY = 300;

        // adds the initial players and buttons for the tournament
        for(int i = 0; i < matchupList.size(); i++) {
        	final String player1 = matchupList.get(i).getPlayer1().getName();
            final String player2 = matchupList.get(i).getPlayer2().getName();
            addLabel(0, (i*2) + 1, 1, false, player1);
            addLabel(0, (i*2) + 2, 1, true, player2);
            addButton(1, (i*2) + 1, 1, true);
            addButton(1, (i*2) + 2, 1, true);

        }
        // adds void panels to allow for scrolling
        addVoidPanel(0, maxY);
        addVoidPanel(maxX, 0);

        // add scroll panel
        JScrollPane scrollPanel = new JScrollPane(matchPanel);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        panelMain.add(scrollPanel, BorderLayout.CENTER);

        window.setVisible(true); 
        matchPanel.revalidate();
        matchPanel.repaint();
	}
	
	/**
	 * Adds a panel to allow for scrolling of the window
	 */
	private void addVoidPanel(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridy = y;
        gbc.gridx = x;
        // if vertical panel
        if (x == 0) {
        	gbc.fill = GridBagConstraints.VERTICAL;
        	gbc.weighty = 1.0;
        }
        // if horizontal panel
        else {
        	gbc.fill = GridBagConstraints.HORIZONTAL;
        	gbc.weightx = 1.0;
        }
        
        JPanel voidPanel = new JPanel();
        voidPanel.setBackground(new Color(255, 255, 255));
        matchLayout.setConstraints(voidPanel, gbc);
        matchPanel.add(voidPanel);
	}
	
	/**
	 * Add's a winner button to matchPanel.
	 */
	private void addButton(int x, int y, int height, boolean firstRow) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = y;
        gbc.gridx = x;
        // first row needs to be anchored to the northwest
        if (firstRow) {
        	gbc.insets = new Insets(5, 5, 5, 5);
        	gbc.anchor = GridBagConstraints.NORTHWEST;
        } else { 
        	gbc.insets = new Insets(5, 5, 50, 5);
        }
        gbc.gridheight = height;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JButton button = new JButton("Winner");
        button.addActionListener(new Listener());
        matchLayout.setConstraints(button, gbc);
        matchPanel.add(button);
	}
	
	/**
	 * Add's a player label to matchPanel.
	 */
	private void addLabel(int x, int y, int height, boolean gap, String player) { 
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = y;
        gbc.gridx = x;
        gbc.insets = new Insets(5, 5, 5, 5);
        // gap is only false in the first player of each pair in the first row
        if (gap) {
        	gbc.insets = new Insets(5, 5, 50, 5);
        }
        gbc.gridheight = height;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lbl = new JLabel(player, SwingConstants.RIGHT);
        lbl.setForeground(Color.black);
        matchLayout.setConstraints(lbl, gbc);
        matchPanel.add(lbl);
	}
	
	/**
	 * Handles the logic when a button is pressed.
	 */
	private class Listener implements ActionListener
	{
		
		/**
		 * Need to refactor this code.
		 * Code to change colour of the winning player has been commented out for now.
		 */
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
//            		((JLabel) c).setForeground(Color.red);
            	}

            }
           
            // assume set of x is 1,3,5,7,9 etc
            // is top player
            if ((y-1) % Math.pow(2, ((x+1)/2)) == 0) {
            	System.out.println("yaba");
//            	for(Component c : componentList){
//                	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
//                    //remove player JLabel if it exists
//                	if (gbc2.gridx == x-1 && gbc2.gridy == y + Math.pow(2, ((x-1)/2))) {
//                		((JLabel) c).setForeground(Color.black);
//                	}
//            	}
            	
            }
            // is bottom player
            else {
//            	for(Component c : componentList){
//                	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
//                    //remove player JLabel if it exists
//                	if (gbc2.gridx == x-1 && gbc2.gridy == y - Math.pow(2, ((x-1)/2))) {
//                		((JLabel) c).setForeground(Color.black);
//                	}
//            	}
            	y -= Math.pow(2, ((x-1)/2));
            }
            x += 1;
            height = (int) Math.pow(2, ((x+1)/2));
            
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
            addLabel(x, y, height, true, player);

            int buttonY = 0;
            System.out.println("button check, x=" + x + " y=" + y );
            if ((y-1) % Math.pow(2, ((x+3)/2)) == 0) {
            	System.out.println("top");
            	for(Component c : componentList){
                	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
                    // check that there is another player in this matchup
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
                	if (gbc2.gridx == x && gbc2.gridy == (y-height)) {
                		buttonY = y-height;
                		twoPlayers = true;
                	}

                }	
            }
            if (twoPlayers) {
            	x += 1;
                addButton(x, y, height, false);
                addButton(x, buttonY, height, false);
                
            }
            
			matchPanel.revalidate();
            matchPanel.repaint();
	    }
	}

}
