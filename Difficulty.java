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

public class Difficulty {
	
	private MainMenu mainMenu;
	
	private JFrame mainFrame;
	private JButton easy, normal, hard, expert, back;
	private JLabel selection;
	
	public Difficulty( JFrame frame ){
		mainFrame = frame;
		createView();
		placeComponent();
		createController();
	}
	
	private void createView(){
		Dimension buttonSize = new Dimension( 300, 50 );
		
		selection = new JLabel( "Sélection de la difficulté" );
		selection.setFont( new Font( "Arial", Font.BOLD , 40 ) );
		
		easy = new JButton( "Facile" );
		easy.setPreferredSize( buttonSize );
		
		normal = new JButton( "Normal" );
		normal.setPreferredSize( buttonSize );
		
		hard = new JButton( "Difficile" );
		hard.setPreferredSize( buttonSize );
		
		expert = new JButton( "Expert" );
		expert.setPreferredSize( buttonSize );
		
		back = new JButton( "Retour" );
	}

	private void placeComponent(){		
		JPanel p = new JPanel( new GridBagLayout() );{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets( 50, 0, 0, 0 );
			p.add( selection, gbc );
		}
		mainFrame.add( p, BorderLayout.NORTH );
		
		p = new JPanel( new GridBagLayout() ); {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets( 10, 10, 10, 10 );
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			p.add( easy, gbc );
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			p.add( normal, gbc );
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			p.add( hard, gbc );
			
			gbc.gridx = 0;
			gbc.gridy = 3;
			p.add( expert, gbc );
		}
		mainFrame.add( p );
		
		p = new JPanel();{
			p.add( back );
		}
		mainFrame.add( p, BorderLayout.SOUTH );
	}
	
	private void createController(){
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		easy.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				
			}
		});
		
		normal.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				
			}
		});
		
		hard.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				
			}
		});
		
		expert.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				
			}
		});
		
		back.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				mainFrame.getContentPane().removeAll();
				
				mainMenu = new MainMenu( mainFrame );
				mainFrame.setContentPane( mainMenu.getJFrame().getContentPane() );
				mainFrame.repaint();
			}
		});
	}
	
	public JFrame getJFrame(){
		return mainFrame;
	}
}
