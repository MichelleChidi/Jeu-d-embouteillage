package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainMenu {
	
	private Difficulty difficulty;
	private Scores scores;
	private Rules rules;
	private CreateCard createCard;
	private Game game;
	
	private JFrame mainFrame;
	private JButton level, rulesButton, cardButton, scoresButton, play;
	private JLabel title;
	
	public MainMenu(){
		mainFrame = new JFrame( "Rush Hour" );
		mainFrame.setPreferredSize( new Dimension( 1280, 720 ) );
		
		createView();
		placeComponent();
		createController();
	}
	
	public MainMenu( JFrame frame ){
		mainFrame = frame;
		
		createView();
		placeComponent();
		createController();
	}
	
	public void display(){
		mainFrame.pack();
		mainFrame.setLocationRelativeTo( null );
		mainFrame.setVisible( true );
	}

	private void createView() {
		Dimension buttonSize = new Dimension( 150, 30 );
		
		title = new JLabel( "Rush Hour" );
		title.setFont( new Font( "Arial", Font.BOLD , 40 ) );
		
		level = new JButton( "Niveaux" );
		level.setPreferredSize( buttonSize );
		
		rulesButton = new JButton( "Règles" );
		rulesButton.setPreferredSize( buttonSize );
		
		cardButton = new JButton( "Créer" );
		cardButton.setPreferredSize( buttonSize );
		
		scoresButton = new JButton( "Scores" );
		scoresButton.setPreferredSize( buttonSize );
		
		play = new JButton( "Jouer" );
		play.setPreferredSize( buttonSize );
	}

	private void placeComponent() {
		JPanel p = new JPanel( new GridBagLayout() ); {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets( 50, 0, 0, 0 );
			p.add( title, gbc );
		}
		mainFrame.add( p, BorderLayout.NORTH );
		
		p = new JPanel( new GridBagLayout() ); {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets( 5, 5, 5, 5 );
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			p.add( level, gbc );
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			p.add( rulesButton, gbc );
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			p.add( scoresButton, gbc );
			
			gbc.gridx = 1;
			gbc.gridy = 1;
			p.add( cardButton, gbc );
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridwidth = 2;
			p.add( play, gbc );
		}
		mainFrame.add( p );
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		
		level.addActionListener( new ActionListener(){
		public void actionPerformed( ActionEvent e ){
				mainFrame.getContentPane().removeAll();
				
				difficulty = new Difficulty( mainFrame );
				mainFrame.setContentPane( difficulty.getJFrame().getContentPane() );
				mainFrame.repaint();
			}
		});
		
		scoresButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				mainFrame.getContentPane().removeAll();
				
				scores = new Scores( mainFrame );
				mainFrame.setContentPane( scores.getJFrame().getContentPane() );
				mainFrame.repaint();
			}
		});
		
		rulesButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				mainFrame.getContentPane().removeAll();
				
				rules = new Rules( mainFrame );
				mainFrame.setContentPane( rules.getJFrame().getContentPane() );
				mainFrame.repaint();
			}
		});
		
		cardButton.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				mainFrame.getContentPane().removeAll();
				
				createCard = new CreateCard( mainFrame );
				mainFrame.setContentPane( createCard.getJFrame().getContentPane() );
				mainFrame.repaint();
			}
		});
		
		play.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				mainFrame.getContentPane().removeAll();
				
				game = new Game( mainFrame );
				mainFrame.setContentPane( game.getJFrame().getContentPane() );
				mainFrame.repaint();
			}
		});
	}
	
	public JFrame getJFrame(){
		return mainFrame;
	}
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().display();
            }
        });
    }

}
