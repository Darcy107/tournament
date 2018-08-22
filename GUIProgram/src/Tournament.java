import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	
	// matchup info
	private List<Matchup> matchupList = new ArrayList<>();
	private int numRounds = 0;
	
	// main panel
	private JPanel matchPanel;
	private GridBagLayout matchLayout;
	
	// colours
	private Color lblColour = Color.black;
	private Color grayLbl = new Color(90,150,150);
	private Color pnlColour = new Color(141,237,237);
	private Color headerColour = new Color(110,195,195);

	// header drop downs
	private JComboBox roundDropDown;
    private JComboBox dropDown;
	
	
	/**
	 * Create the application.
	 */
	public Tournament() {
		readNames();
		initialize();
	}
	
	/**
	 * reads the player names in from a text file and adds them to the matchup list
	 * assume all of the names are in a list since I control the generation of the names for now
	 */
	private void readNames() {
		matchupList.clear();
		try(BufferedReader br = new BufferedReader(new FileReader("src/PlayerNames"))) {
		    String line = br.readLine();

		    while (line != null) {
		    	String firstPlayer = line;
		        line = br.readLine();
		        if (line != null) {
		        	matchupList.add(new Matchup(new Player(firstPlayer, "b6"), new Player(line, "b6")));
		        } else {
		        	matchupList.add(new Matchup(new Player(firstPlayer, "b6"), new Player("BYE", "b6")));
		        }
		        line = br.readLine();
		    
		    }
		    System.out.println(matchupList.size());
		    float currentSize = matchupList.size();
		    int roundCount = 1;
		    
		    while (currentSize > 1) {
		    	roundCount++;
		    	currentSize = currentSize/2;
		    }
		    numRounds = roundCount;
		    System.out.println(roundCount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JFrame window = new JFrame("Test");
		window.setTitle("Tournament");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1000, 800);
        //window.setMinimumSize(new Dimension(350, 150));

        JPanel panelMain = new JPanel(new BorderLayout());
        window.add(panelMain);


        matchLayout = new GridBagLayout();
        matchPanel = new JPanel(matchLayout);
        matchPanel.setBackground(pnlColour);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridy = 0;
        // makes a 50 unit gap on the left
        gbc.insets = new Insets(5, 40, 10, 30);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        JLabel lbl = new JLabel("Round 1");
        matchLayout.setConstraints(lbl, gbc);
        matchPanel.add(lbl);
        
        gbc.insets = new Insets(5, 15, 10, 30);
        
        for (int i= 1 ; i < numRounds; i++) {
        	gbc.gridx = i*2;
        	lbl = new JLabel("Round " + (i+1));
            matchLayout.setConstraints(lbl, gbc);
            matchPanel.add(lbl);
        }

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
        JPanel combinePanel = new JPanel();
        combinePanel.setLayout(new BoxLayout(combinePanel, BoxLayout.Y_AXIS));
        combinePanel.add(createHeader());
        combinePanel.add(matchPanel);
        JScrollPane scrollPanel = new JScrollPane(combinePanel);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        
        
    	//panelMain.add(createHeader(), BorderLayout.NORTH);
        panelMain.add(scrollPanel, BorderLayout.CENTER);

        window.setVisible(true); 
        matchPanel.revalidate();
        matchPanel.repaint();
	}
	
	private JPanel createHeader() {
		GridBagLayout headerLayout = new GridBagLayout();
    	JPanel headerPanel = new JPanel(headerLayout);
    	headerPanel.setBackground(headerColour);
		GridBagConstraints gbc = new GridBagConstraints();
	    gbc.weightx = 0.0;
	    gbc.weighty = 0.0;
	    gbc.gridy = 0;
	    // makes a 50 unit gap on the left
	    gbc.insets = new Insets(5, 5, 10, 30);
	    gbc.gridx = 0;
	       
	    JLabel lbl = new JLabel("Output to a File:");
	    headerLayout.setConstraints(lbl, gbc);
	    headerPanel.add(lbl);
	    
	    gbc.gridx = 1;
	    String[] options = { "Player List", "Matchup List" };
	    dropDown = new JComboBox(options);
	    headerLayout.setConstraints(dropDown, gbc);
	    headerPanel.add(dropDown);
	    
	    gbc.gridx = 2;
	    // CHANGE THIS
	    List<String> roundList = new ArrayList<>();
	    for (int i = 1; i < numRounds+1; i++) {
	    	roundList.add("Round " + i);
	    }
	    
	    gbc.gridx = 3;
	    roundDropDown = new JComboBox(roundList.toArray());
	    headerLayout.setConstraints(roundDropDown, gbc);
	    headerPanel.add(roundDropDown);
	    
	    gbc.gridx = 4;
	    JButton headButton = new JButton("Output");
        headButton.addActionListener(new HeadListener());
        headerLayout.setConstraints(headButton, gbc);
        headerPanel.add(headButton);
        
	    return headerPanel;
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
        voidPanel.setBackground(pnlColour);
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
        	gbc.insets = new Insets(2, 2, 2, 2);
        	gbc.anchor = GridBagConstraints.NORTHWEST;
        } else { 
        	gbc.insets = new Insets(2, 2, 40, 2);
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
        gbc.insets = new Insets(2, 5, 2, 2);
        // gap is only false in the first player of each pair in the first row
        if (gap) {
        	gbc.insets = new Insets(2, 5, 40, 2);
        }
        gbc.gridheight = height;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lbl = new JLabel(player, SwingConstants.RIGHT);
        lbl.setForeground(lblColour);
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
			//boolean twoPlayers = false;
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
            		((JLabel) c).setForeground(lblColour);
            	}

            }
           
            y = setLabelY(x, y);
            x += 1;
            height = (int) Math.pow(2, ((x+1)/2));
            
            removeOldPlayer(x, y);
            addLabel(x, y, height, true, player);

            
            System.out.println("button check, x=" + x + " y=" + y );
            
            checkTwoPlayers(x, y, height);
            
			matchPanel.revalidate();
            matchPanel.repaint();
	    }
	}
	
	// add the winner buttons if there are two players in the matchup
	private void checkTwoPlayers(int x, int y, int height) {
		Component[] componentList = matchPanel.getComponents();
		boolean twoPlayers = false;
		int buttonY = 0;
        if ((y-1) % Math.pow(2, ((x+3)/2)) == 0) {
        	System.out.println("top");
        	for(Component c : componentList){
            	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
                // check that there is another player in this matchup
            	if (gbc2.gridx == x && gbc2.gridy == (y+height)) {
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
        	System.out.println(buttonY);
            addButton(x, y, height, false);
            addButton(x, buttonY, height, false);
            
        }
	}
	
	private int setLabelY(int x, int y) {
		Component[] componentList = matchPanel.getComponents();
		 // assume set of x is 1,3,5,7,9 etc
        // is top player
        if ((y-1) % Math.pow(2, ((x+1)/2)) == 0) {
        	System.out.println("yaba");
        	for(Component c : componentList){
            	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
                //remove player JLabel if it exists
            	if (gbc2.gridx == x-1 && gbc2.gridy == y + Math.pow(2, ((x-1)/2))) {
            		((JLabel) c).setForeground(grayLbl);
            	}
        	}
        	
        }
        // is bottom player
        else {
        	for(Component c : componentList){
            	GridBagConstraints gbc2 = matchLayout.getConstraints(c);
                //remove player JLabel if it exists
            	if (gbc2.gridx == x-1 && gbc2.gridy == y - Math.pow(2, ((x-1)/2))) {
            		((JLabel) c).setForeground(grayLbl);
            		//System.out.println(((JLabel) c).toString());
            	}
        	}
        	y -= Math.pow(2, ((x-1)/2));
        }
        return y;
	}
	
	/**
	 * remove old label and button if it exists
	 */
	private void removeOldPlayer(int x, int y) {
		Component[] componentList = matchPanel.getComponents();
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
	}
		
	private class HeadListener implements ActionListener
	{
		
		/**
		 * Button listener to control outputting the players or matchups to a file
		 */
		public void actionPerformed(ActionEvent e)
	    {  
			System.out.println(dropDown.getSelectedItem().toString() + roundDropDown.getSelectedItem().toString());
	    }
		
	}

}
