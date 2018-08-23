import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.GridBagConstraints;

public class Tournament {

	// matchup info
	private List<Matchup> matchupList = new ArrayList<>();
	private int numRounds = 0;

	// main panel
	private JPanel matchPanel;
	private GridBagLayout matchLayout;

	// colours
	private Color lblColour = Color.black;
	private Color grayLbl = new Color(90, 150, 150);
	private Color pnlColour = new Color(141, 237, 237);
	private Color headerColour = new Color(110, 195, 195);

	// header drop downs
	private JComboBox<Object> roundDropDown;
	private JComboBox<Object> outputDropDown;

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

	/**
	 * Create the application.
	 */
	public Tournament() {
		readNames();
		initialize();
	}

	/**
	 * Reads the player names in from a text file and adds them to the matchup list
	 * assume all of the names are in a list since I control the generation of the
	 * names for now
	 */
	private void readNames() {
		matchupList.clear();
		try {
			BufferedReader br = new BufferedReader(new FileReader("PlayerNames.txt"));
			String line = br.readLine();

			while (line != null) {
				String firstPlayer = line;
				line = br.readLine();
				// both players are valid
				if ((line != null && !line.isEmpty()) && (firstPlayer != null && !firstPlayer.isEmpty())) {
					matchupList.add(new Matchup(new Player(firstPlayer, "b6"), new Player(line, "b6")));
				} // only first player is valid
				else if ((line == null || line.isEmpty()) && (firstPlayer != null && !firstPlayer.isEmpty())) {
					matchupList.add(new Matchup(new Player(firstPlayer, "b6"), new Player("BYE", "b6")));
				} // only second player is valid
				else if ((line != null && !line.isEmpty()) && (firstPlayer == null || firstPlayer.isEmpty())) {
					matchupList.add(new Matchup(new Player("BYE", "b6"), new Player(line, "b6")));
				}
				line = br.readLine();
			}

			float currentSize = matchupList.size();
			int roundCount = 1;

			// get number of rounds
			while (currentSize > 1) {
				roundCount++;
				currentSize = currentSize / 2;
			}
			numRounds = roundCount;
			System.out.println(matchupList.size());
			System.out.println(numRounds);
			br.close();
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

		JPanel panelMain = new JPanel(new BorderLayout());
		window.add(panelMain);

		matchLayout = new GridBagLayout();
		matchPanel = new JPanel(matchLayout);
		matchPanel.setBackground(pnlColour);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.gridy = 0;
		// makes a 40 unit gap on the left
		gbc.insets = new Insets(5, 40, 10, 30);
		gbc.gridx = 0;
		gbc.gridwidth = 2;

		JLabel lbl = new JLabel("Round 1");
		matchLayout.setConstraints(lbl, gbc);
		matchPanel.add(lbl);

		gbc.insets = new Insets(5, 15, 10, 30);

		for (int i = 1; i < numRounds; i++) {
			gbc.gridx = i * 2;
			lbl = new JLabel("Round " + (i + 1));
			matchLayout.setConstraints(lbl, gbc);
			matchPanel.add(lbl);
		}

		// adds the initial players and buttons for the tournament
		for (int i = 0; i < matchupList.size(); i++) {
			final String player1 = matchupList.get(i).getPlayer1().getName();
			final String player2 = matchupList.get(i).getPlayer2().getName();
			addLabel(0, (i * 2) + 1, 1, false, player1);
			addLabel(0, (i * 2) + 2, 1, true, player2);
			addButton(1, (i * 2) + 1, 1, true);
			addButton(1, (i * 2) + 2, 1, true);

		}
		// gets a max x and y that will be greater than any possible x or y in the grid
		int maxX = numRounds * 3;
		int maxY = matchupList.size() * 3;

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

		// panelMain.add(createHeader(), BorderLayout.NORTH);
		panelMain.add(scrollPanel, BorderLayout.CENTER);

		window.setVisible(true);
		matchPanel.revalidate();
		matchPanel.repaint();
	}

	/**
	 * Creates the header used for file output
	 */
	private JPanel createHeader() {
		GridBagLayout headerLayout = new GridBagLayout();
		JPanel headerPanel = new JPanel(headerLayout);
		headerPanel.setBackground(headerColour);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 10, 30);
		gbc.gridx = 0;

		// label in the header
		JLabel lbl = new JLabel("Output to a File:");
		headerLayout.setConstraints(lbl, gbc);
		headerPanel.add(lbl);

		// drop down menu for type of file output
		gbc.gridx = 1;
		String[] options = { "Player List", "Matchup List" };
		outputDropDown = new JComboBox<>(options);
		headerLayout.setConstraints(outputDropDown, gbc);
		headerPanel.add(outputDropDown);

		// creates list of rounds
		List<String> roundList = new ArrayList<>();
		for (int i = 1; i < numRounds + 1; i++) {
			roundList.add("Round " + i);
		}

		// drop down menu for the rounds
		gbc.gridx = 2;
		roundDropDown = new JComboBox<>(roundList.toArray());
		headerLayout.setConstraints(roundDropDown, gbc);
		headerPanel.add(roundDropDown);

		// button to trigger file output
		gbc.gridx = 3;
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
	private class Listener implements ActionListener {

		/**
		 * Handles the actions performed when a winner button is pressed.
		 */
		public void actionPerformed(ActionEvent e) {
			String player = "ERROR";
			int x = 0;
			int y = 0;
			int height = 0;
			Component[] componentList = matchPanel.getComponents();
			// get the x and y of the button
			for (Component c : componentList) {
				if (c.equals(e.getSource())) {
					GridBagConstraints gbc = matchLayout.getConstraints(c);
					x = gbc.gridx;
					y = gbc.gridy;

				}
			}

			// get the winning player
			for (Component c : componentList) {
				GridBagConstraints gbc2 = matchLayout.getConstraints(c);
				if (gbc2.gridx == (x - 1) && gbc2.gridy == y) {
					player = ((JLabel) c).getText();
					((JLabel) c).setForeground(lblColour);
				}
			}

			// y is only changed if it is the second player in the matchup
			y = setLabelY(x, y);
			x += 1;
			height = (int) Math.pow(2, ((x + 1) / 2));

			removeOldPlayer(x, y);
			addLabel(x, y, height, true, player);
			checkTwoPlayers(x, y, height);

			matchPanel.revalidate();
			matchPanel.repaint();
		}

		/**
		 * Adds winner buttons if there are two players in a matchup
		 */
		private void checkTwoPlayers(int x, int y, int height) {
			Component[] componentList = matchPanel.getComponents();
			boolean twoPlayers = false;
			int buttonY = 0;
			// is top player
			if ((y - 1) % Math.pow(2, ((x + 3) / 2)) == 0) {
				for (Component c : componentList) {
					GridBagConstraints gbc2 = matchLayout.getConstraints(c);
					// check that there is another player in this matchup
					if (gbc2.gridx == x && gbc2.gridy == (y + height)) {
						buttonY = y + height;
						twoPlayers = true;
					}

				}
			}
			// is bottom player
			else {
				for (Component c : componentList) {
					GridBagConstraints gbc2 = matchLayout.getConstraints(c);
					// check that there is another player in this matchup
					if (gbc2.gridx == x && gbc2.gridy == (y - height)) {
						buttonY = y - height;
						twoPlayers = true;
					}

				}
			}
			if (twoPlayers) {
				x += 1;
				addButton(x, y, height, false);
				addButton(x, buttonY, height, false);

			}
		}

		/**
		 * Sets the y value to be the same as the top player in the matchup
		 */
		private int setLabelY(int x, int y) {
			Component[] componentList = matchPanel.getComponents();
			// assume set of x is 1,3,5,7,9 etc
			// is top player
			if ((y - 1) % Math.pow(2, ((x + 1) / 2)) == 0) {
				for (Component c : componentList) {
					GridBagConstraints gbc2 = matchLayout.getConstraints(c);
					// change defeated player to grey
					if (gbc2.gridx == x - 1 && gbc2.gridy == y + Math.pow(2, ((x - 1) / 2))) {
						((JLabel) c).setForeground(grayLbl);
					}
				}

			}
			// is bottom player
			else {
				for (Component c : componentList) {
					GridBagConstraints gbc2 = matchLayout.getConstraints(c);
					// change defeated player to grey
					if (gbc2.gridx == x - 1 && gbc2.gridy == y - Math.pow(2, ((x - 1) / 2))) {
						((JLabel) c).setForeground(grayLbl);
					}
				}
				y -= Math.pow(2, ((x - 1) / 2));
			}
			return y;
		}

		/**
		 * Remove old label and button if it exists
		 */
		private void removeOldPlayer(int x, int y) {
			Component[] componentList = matchPanel.getComponents();
			for (Component c : componentList) {
				GridBagConstraints gbc2 = matchLayout.getConstraints(c);
				// remove player JLabel if it exists
				if (gbc2.gridx == x && gbc2.gridy == y) {
					matchPanel.remove(c);
				}
				// remove button if it exists
				else if (gbc2.gridx == (x + 1) && gbc2.gridy == y) {
					matchPanel.remove(c);
				}

			}
		}

	}

	private class HeadListener implements ActionListener {

		/**
		 * Button listener to control outputting the players or matchups to a file
		 */
		public void actionPerformed(ActionEvent e) {
			Component[] componentList = matchPanel.getComponents();
			List<String> playerList = new ArrayList<>();
			int round = roundDropDown.getSelectedIndex() + 1;
			int height = (int) Math.pow(2, (round - 1));
			int x = (round - 1) * 2;
			int numPlayers = (int) ((matchupList.size() * 2) / height);

			// searches through the components and adds all players to playerList in order
			for (int i = 0; i < numPlayers; i++) {
				boolean foundPlayer = false;
				for (Component c : componentList) {
					GridBagConstraints gbc2 = matchLayout.getConstraints(c);
					int y = 1 + (height * i);
					if (gbc2.gridx == x && gbc2.gridy == y) {
						playerList.add(((JLabel) c).getText());
						foundPlayer = true;
					}
				}
				if (!foundPlayer) {
					playerList.add("ERROR");
				}
			}

			// player list
			if (outputDropDown.getSelectedIndex() == 0) {
				outputPlayers(round, playerList);
			}
			// matchup list
			else {
				outputMatchups(round, playerList);
			}

		}

		/**
		 * Outputs a list of players for a round to a file
		 */
		private void outputPlayers(int round, List<String> playerList) {
			Path file = Paths.get("Round" + round + ".txt");
			try {
				Files.write(file, playerList, Charset.forName("UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Outputs a list of matchups for a round to a file
		 */
		private void outputMatchups(int round, List<String> playerList) {
			List<String> outputList = new ArrayList<>();
			outputList.add("ROUND " + round);
			// adds a newline to the file for formatting
			outputList.add("");

			// adds matchups of the players to outputList
			for (int i = 1; i < playerList.size(); i += 2) {
				outputList.add(playerList.get(i - 1) + " vs " + playerList.get(i));
			}
			Path file = Paths.get("MatchupsRound" + round + ".txt");
			try {
				Files.write(file, outputList, Charset.forName("UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
