/*  Name:  Jennifer Nguyen
     Course: CNT 4714 – Spring 2022 
     Assignment title: Project Three: Two-Tier Client-Server Application Development With MySQL and JDBC
     Date: Sunday March 27, 2022
*/ 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.Box;


public class Display extends JFrame {
	
	static final String DEFAULT_QUERY = "SELECT * FROM riders";
	private Results tableModel = null;
	
	private JTextArea queryArea;
	
	public Display() throws IOException 
	 {   
	      super( "Database Search" );
	     
				 
			   
		         // Whole GUI
		 		JPanel searchPanel = new JPanel();
		 		
		 		searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		 		searchPanel.setLayout(new GridLayout(3,1,20,20));
		 		
		 		// Upper Panel
		 		JPanel upperPanel = new JPanel();
		 		upperPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		 		upperPanel.setLayout(new GridLayout(1,2,20,20));
		 		
		 		// Connection Details
		 		JPanel detailpanel = new JPanel();
		 		detailpanel.setLayout(new GridLayout(4, 1, 10, 10));
		 		JLabel connectDetailLabel = new JLabel("<html>" + "<B>" + "Connection Details" + "</B>" + "</html>", SwingConstants.LEFT);
		 		connectDetailLabel.setForeground(Color.BLUE);
		 		JLabel propLabel = new JLabel("Properties File", SwingConstants.LEFT);
		 		JLabel userLabel = new JLabel("Username", SwingConstants.LEFT);
		 		JLabel passLabel = new JLabel("Password", SwingConstants.LEFT);
		 		
		 		JPanel condetailpanel = new JPanel();
		 		condetailpanel.setLayout(new GridLayout(1,1));
		 		condetailpanel.add(connectDetailLabel);
		 		detailpanel.add(condetailpanel);
		 		
		 		JPanel proppanel = new JPanel();
		 		proppanel.setLayout(new GridLayout(1,2));
		 		
		 		String[] propFiles = {"root.properties", "client.properties"};
				JComboBox<String> propertiesdrop = new JComboBox<>(propFiles);
				
		 		proppanel.add(propLabel);
		 		proppanel.add(propertiesdrop);
		 		detailpanel.add(proppanel);
		 		
		 		JPanel userpanel = new JPanel();
		 		userpanel.setLayout(new GridLayout(1,2));
		 		JTextField userTextField = new JTextField();
		 		userpanel.add(userLabel);
		 		userpanel.add(userTextField);
		 		detailpanel.add(userpanel);
		 		
		 		JPanel passpanel = new JPanel();
		 		passpanel.setLayout(new GridLayout(1,2));
		 		JPasswordField  passTextField = new JPasswordField ();
		 		passTextField.setEchoChar('*');
		 		passpanel.add(passLabel);
		 		passpanel.add(passTextField);
		 		detailpanel.add(passpanel);
		 		
		 		upperPanel.add(detailpanel);
		 		
		 		// SQL Command
		 		JPanel querypanel = new JPanel();
		 		JLabel queryLabel = new JLabel("<html>" + "<B>" + "Enter A SQL Command:" + "</B>" + "</html>", SwingConstants.LEFT);
		 		queryArea = new JTextArea( "", 12, 100 );
		        queryArea.setWrapStyleWord(true);
		        queryArea.setLineWrap(true);
		        JScrollPane scrollPane = new JScrollPane(queryArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		         
		 		queryLabel.setForeground(Color.BLUE);
		 		JButton clearSQLBut = new JButton("Clear SQL Command");
		 		JButton executeSQLBut = new JButton("Execute SQL Command");
		 		executeSQLBut.setEnabled(false);
		 	
		 		querypanel.setLayout(new GridLayout(3,1));
		 		querypanel.add(queryLabel);
		 		querypanel.add(scrollPane);
		 		
		 		JPanel sqlBut = new JPanel();
		 		sqlBut.setLayout(new FlowLayout());
		 		
		 		sqlBut.add(clearSQLBut);
		 		sqlBut.add(executeSQLBut);
		 		
		 		querypanel.add(sqlBut);
		 		
		 		upperPanel.add(querypanel);
		 			
		 		
		 		// Lower Panel
		 		JPanel lowerPanel = new JPanel();
		 		lowerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		 		lowerPanel.setLayout(new GridLayout(2,1,20,20));
		 		
		 		// DB Connection
		 		JPanel connectionpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 		
				JLabel DBConnectField = new JLabel("Database not Connected");
				JButton DBConnectButton = new JButton("Connect to Database");
		 		connectionpanel.add(DBConnectButton);
		 		connectionpanel.add(DBConnectField);
		 		
		 		lowerPanel.add(connectionpanel);
	
	
				// Result Panel
		 		JPanel resultpanel = new JPanel(new GridLayout(1,1,20,20));
		 		
		 		JTextArea temp = new JTextArea( "", 12, 100 );
		        JScrollPane scrollyPane = new JScrollPane(temp, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		        
				JLabel resultLabel = new JLabel("<html>" + "<B>" + "SQL Execution Result Window" + "</B>" + "</html>", SwingConstants.LEFT);
				resultLabel.setForeground(Color.BLUE);
				resultpanel.add(resultLabel);
				
				lowerPanel.add(resultpanel);
		 	
				// Put all elements together
		 		searchPanel.add(upperPanel, BorderLayout.NORTH);
		 		searchPanel.add(lowerPanel, BorderLayout.SOUTH);
		 		searchPanel.add(scrollyPane);
		 		
		 		
		 		add(searchPanel);
		         
		 		
		 		clearSQLBut.addActionListener(new ActionListener() 
		 	    {
		 			public void actionPerformed(ActionEvent ae)
		 			{
		 				queryArea.setText("");
		 			}
		 			
		 	    });
		 		
		 		DBConnectButton.addActionListener(new ActionListener() 
		 	    {
		 			public void actionPerformed(ActionEvent ae)
		 			{
		 			   Properties properties = new Properties();
		 			   FileInputStream filein = null;
		 			   MysqlDataSource dataSource = null;
		 				
			 				try 
			 				{
			 					filein = new FileInputStream(propertiesdrop.getItemAt(propertiesdrop.getSelectedIndex()));
							
			 			    
								properties.load(filein);
								
				 			    dataSource = new MysqlDataSource();
				 			    dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
				 			    dataSource.setUser(userTextField.getText());
				 			    dataSource.setPassword(String.valueOf(passTextField.getPassword())); 
				 				
			 					
			 					DBConnectField.setText("Connected to " + properties.getProperty("MYSQL_DB_URL"));
			 					executeSQLBut.setEnabled(true);
			 				}
		 					catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		 			}
		 			
		 	    });
		 		
		 		executeSQLBut.addActionListener( 
		         
		            new ActionListener() 
		            {
		               public void actionPerformed( ActionEvent event )
		               {
		                  try 
		                  {
		                	  // make result table
								tableModel = new Results(userTextField.getText(), String.valueOf(passTextField.getPassword()), propertiesdrop.getSelectedIndex(), queryArea.getText());
							
								JTable resultTable = new JTable( tableModel );
		                	  	resultTable.setGridColor(Color.BLACK);
		                	  
		                	  	// set on GUI
		                	  	scrollyPane.setViewportView(resultTable);
		     				
		                  } 
		                  catch ( SQLException sqlException ) 
		                  {
		                     JOptionPane.showMessageDialog( null, 
		                        sqlException.getMessage(), "Database error", 
		                        JOptionPane.ERROR_MESSAGE );
		                  }
		                     
		                  catch (ClassNotFoundException e) 
		                  {
								// TODO Auto-generated catch block
								e.printStackTrace();
						}
		        
		               } 
		            }    
		         ); 
		         setSize( 600, 300 ); 
		         setVisible( true ); 
	     
	      
	      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
	      
	      
	      // ensure database connection is closed when user quits application
	      addWindowListener(new WindowAdapter() 
	         {
	            // disconnect from database and exit when window has closed
	            public void windowClosed( WindowEvent event )
	            {
	               tableModel.disconnectFromDatabase();
	               System.exit( 0 );
	            } 
	         } 
	      );
}
	   
	
	
	   // execute application
	   public static void main( String args[] ) throws IOException 
	   {
		   Display startframe = new Display();
	       startframe.pack(); // fit windows for screen
	       startframe.setSize(1000,600);
	       startframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   }
	} 
	   

