/*  Name:  Jennifer Nguyen
     Course: CNT 4714 – Spring 2022 
     Assignment title: Project Three: Two-Tier Client-Server Application Development With MySQL and JDBC
     Date: Sunday March 27, 2022
*/ 

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import java.util.Properties;
import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class Results extends AbstractTableModel{
		private Display display;
		private Connection connection;
		private Statement statement;
	    private ResultSet resultSet;
	    private ResultSetMetaData metaData;
	    private int numberOfRows;
	  
	    private boolean connectedToDatabase = false;
	    
	    ResultSet OPresultSet;
	    Statement OPstatement;
	   
	   public Results(String user, String pass, int fileNum, String query ) throws SQLException, ClassNotFoundException
	   {         
		   Properties properties = new Properties();
		   MysqlDataSource dataSource = null;
		   FileInputStream filein = null;
		   
		   
		   String[] propFiles = {"root.properties", "client.properties"};
		   try 
		   {
			 filein = new FileInputStream(propFiles[fileNum]);
		     properties.load(filein);
		     dataSource = new MysqlDataSource();
		     dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
		     dataSource.setUser(user);
		     dataSource.setPassword(pass); 
		     
		     Connection OPconnection = DriverManager.getConnection(
						properties.getProperty("MYSQL_DB_OPURL"), properties.getProperty("MYSQL_DB_OPUSER"), properties.getProperty("MYSQL_DB_OP_PASS"));
			
				//OPconnection = OPdataSource.getConnection();
			 OPstatement = OPconnection.createStatement();
		     
		     Connection connection = dataSource.getConnection();
		          
		     statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
		            
		     connectedToDatabase = true;
		     
		     
		           
		     if(query.contains("insert") || query.contains("delete") | query.contains("update"))
		     {
		    	 if (user.equals("root"))
		    		 setUpdate(query);
		    	 else
		    	 {
		    		 String outputMessage = "UPDATE command denied to user '" + user + "'";
		    		 JOptionPane.showMessageDialog(null, outputMessage, "Database Error", JOptionPane.ERROR_MESSAGE);
		    		 return;
		    	 }
		    		 
		     }
		     else
		    	 setQuery( query );
		     
		    
		  } //end try
		      catch ( SQLException sqlException ) 
		      {
		         sqlException.printStackTrace();
		         System.exit( 1 );
		      } // end catch
		      catch (IOException e) {
		         e.printStackTrace();
		      }  
	   } 
	   
	  
	   
	   public Class getColumnClass( int column ) throws IllegalStateException
	   {
	      // ensure database connection is available
	      if ( !connectedToDatabase ) 
	         throw new IllegalStateException( "Not Connected to Database" );
	      // determine Java class of column
	      try 
	      {
	    	  String className = metaData.getColumnClassName( column + 1 );
	         
	         // return Class object that represents className
	         return Class.forName( className );
	      } // end try
	      catch ( Exception exception ) 
	      {
		        exception.printStackTrace();
	      } // end catch
	      
	      return Object.class; // if problems occur above, assume type Object
	   } // end method getColumnClass
	   // get number of columns in ResultSet
	   
	   
	   public int getColumnCount() throws IllegalStateException
	   {   
	      // ensure database connection is available
	      if ( !connectedToDatabase ) 
	         throw new IllegalStateException( "Not Connected to Database" );
	      // determine number of columns
	      try 
	      {
	         return metaData.getColumnCount(); 
	      } // end try
	      catch ( SQLException sqlException ) 
	      {
	         sqlException.printStackTrace();
	      } // end catch
	      
	      return 0; // if problems occur above, return 0 for number of columns
	   } // end method getColumnCount
	   
	   
	   
	   // get name of a particular column in ResultSet
	   public String getColumnName( int column ) throws IllegalStateException
	   {    
	      // ensure database connection is available
	      if ( !connectedToDatabase ) 
	         throw new IllegalStateException( "Not Connected to Database" );
	      // determine column name
	      try 
	      {
	         return metaData.getColumnName( column + 1 );  
	      } // end try
	      catch ( SQLException sqlException ) 
	      {
	         sqlException.printStackTrace();
	      } // end catch
	      
	      return ""; // if problems, return empty string for column name
	   } // end method getColumnName
	   
	   
	   
	   // return number of rows in ResultSet
	   public int getRowCount() throws IllegalStateException
	   {      
	      // ensure database connection is available
	      if ( !connectedToDatabase ) 
	         throw new IllegalStateException( "Not Connected to Database" );
	 
	      return numberOfRows;
	   } // end method getRowCount
	   // obtain value in particular row and column
	   
	   
	   public Object getValueAt( int row, int column )  throws IllegalStateException
	   {
	      // ensure database connection is available
	      if ( !connectedToDatabase ) 
	         throw new IllegalStateException( "Not Connected to Database" );
	      // obtain a value at specified ResultSet row and column
	      try 
	      {
	    	 resultSet.next(); 
	         resultSet.absolute( row + 1 );
	         return resultSet.getObject( column + 1 );
	      } // end try
	      catch ( SQLException sqlException ) 
	      {
	         sqlException.printStackTrace();
	      } // end catch
	      
	      return ""; 
	   } 
	   
	   
	   
	   // set new database query string
	   public void setQuery( String query ) 
	      throws SQLException, IllegalStateException 
	   {
	      // ensure database connection is available
	      if ( !connectedToDatabase ) 
	         throw new IllegalStateException( "Not Connected to Database" );
	      // specify query and execute it
	      try 
	      {
	    	  resultSet = statement.executeQuery( query );
			  int up = OPstatement.executeUpdate("Update `operationscount` set `num_queries` = `num_queries` +1");

	      }
	      catch ( SQLException sqlException ) 
	      {
	    	  String outputMessage = "Invalid Query";
	    	  JOptionPane.showMessageDialog(null, outputMessage, "Database Error", JOptionPane.ERROR_MESSAGE);
	    	
	    	  sqlException.printStackTrace();
	      }
	      // obtain meta data for ResultSet
	      metaData = resultSet.getMetaData();
	      // determine number of rows in ResultSet
	      resultSet.last();                   // move to last row
	      numberOfRows = resultSet.getRow();  // get row number      
	      
	      // notify JTable that model has changed
	      fireTableStructureChanged();
	   } 
	   
	   
	// set new database update-query string
	   public void setUpdate( String query )throws SQLException, IllegalStateException 
	   {
		   int res, up;
	      // ensure database connection is available
	      if ( !connectedToDatabase ) 
	         throw new IllegalStateException( "Not Connected to Database" );
	     
	      try 
	      {
		      res = statement.executeUpdate( query );
			  up = OPstatement.executeUpdate("Update `operationscount` set `num_updates` = `num_updates` +1");
	      }
	      catch ( SQLException sqlException ) 
	      {
	    	  String outputMessage = "Invalid Query";
	    	  JOptionPane.showMessageDialog(null, outputMessage, "Database Error", JOptionPane.ERROR_MESSAGE);
	    	
	    	  sqlException.printStackTrace();
	      }

	      fireTableStructureChanged();
	   } 
	   
	   
	   // close Statement and Connection               
	   public void disconnectFromDatabase()            
	   {              
	      if ( !connectedToDatabase )                  
	         return;
	      // close Statement and Connection            
	      else try                                          
	      {                                            
	         statement.close();                        
	         connection.close();                       
	      } // end try                                 
	      catch ( SQLException sqlException )          
	      {                                            
	         sqlException.printStackTrace();           
	      } // end catch                               
	      finally  // update database connection status
	      {                                            
	         connectedToDatabase = false;              
	      }             
	   } 
}
