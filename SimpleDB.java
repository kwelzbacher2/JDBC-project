
package edu.cscc.csci2469.lab9;

import java.sql.*;
import java.util.Scanner;

/**
 * Class SimpleDB connects to a database and allows user to Insert, Retrieve, Update, or Delete from the database.
 * @author Kwelzbacher
 *
 */
public class SimpleDB {
    
    /**
     * Create instance of scanner to read user input
     */
    private static Scanner scanner  = new Scanner(System.in);
    /**
     * Main method
     * @param args argument of strings found in main method
     */
    public static void main(String[] args) {
        int id;
        
        try {
            //load the driver for MySQL
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            
            //make a connection to database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/simpleDB",  "root", "");
            System.out.println("Database connected");
            
            //prompt user
            String firstPrompt = "Do you wish to insert (I), retrieve (R), update (U), or delete (D) from database? Please type either I, "
                                                    + "R, U, D or choose Q to quit: ";
            String s = getUserInput(firstPrompt).toUpperCase();
            char answer = s.charAt(0);
            
            //while loop to prompt until quit
            while (true) {
                
                if (answer == 'I') {
                    //prompt user for each attribute to add
                    id = getId("What ID would you like to add to the database? ");
                    String last = getUserInput("What last name would you like to add to the database? ");
                    String first = getUserInput("What first name would you like to add to the database? ");
                    String email = getUserInput("What email would you like to add to the database? ");
                    
                    //create PreparedStatement to use SQL commands and fill with answers
                    PreparedStatement preparedStatement = connection.prepareStatement("insert into Info (id, lastName, firstName, email)"
                                                                                      + "values (?, ?, ?, ?)");
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, last);
                    preparedStatement.setString(3, first);
                    preparedStatement.setString(4, email);
                
                    preparedStatement.execute();
                    
                    preparedStatement.close();
                    
                } else if(answer == 'R') {
                    //prompt user for ID to obtain row 
                    id = getId("Please enter ID: ");
                    
                    //create statement for ResultSet to return data
                    Statement statement = connection.createStatement();
                    ResultSet resultset = statement.executeQuery("select id, lastName, firstName, email from Info where id = " + id);
                    //Make sure finds ID and display information or else notify ID was not found
                    if(resultset.next()) {
                        
                            System.out.println(resultset.getString(1) + "\t" + resultset.getString(2) + "\t" + resultset.getString(3)
                            + "\t" + resultset.getString(4));
                        
                    } else {
                      System.out.println("Sorry, that ID was not found");      
                    }
                    statement.close();
                    
                } else if (answer == 'U') {
                    //prompt for ID of row to update
                    id = getId("Please enter ID of row to update: ");
                    //create Statement for ResultSet to return the row specified
                    Statement statement = connection.createStatement();
                    ResultSet resultset = statement.executeQuery("select id, lastName, firstName, email from Info where id = " + id);
                    //If ID found display current row before update or else notify ID not found
                    if(resultset.next()) {
                        
                        System.out.println(resultset.getString(1) + "\t" + resultset.getString(2) + "\t" + resultset.getString(3)
                        + "\t" + resultset.getString(4));
                    
                    } else {
                        System.out.println("Sorry, that ID was not found");      
                    }
                    //prompt user for each attribute to update
                    String newLast = getUserInput("Please enter new last name: ");
                    String newFirst = getUserInput("Please enter new first name: ");
                    String newEmail = getUserInput("Please enter new email: ");
                    
                    //Create PreparedStatement to issue SQL command to update table with user input
                    PreparedStatement prepStatement = connection.prepareStatement("update Info set lastName = ?, firstName = ?, email = ?"
                                                            + "where id = ?");
                    prepStatement.setString(1, newLast);
                    prepStatement.setString(2, newFirst);
                    prepStatement.setString(3, newEmail);
                    prepStatement.setInt(4, id);
                    
                    prepStatement.executeUpdate();
                    prepStatement.close();
                    statement.close();
                    
                    
               } else if (answer == 'D') {
                   //prompt user for ID of row to delete
                    id = getId("Please enter ID of row to delete: ");
                    //create Statement for ResultSet to return the current row asking to delete
                    Statement statement = connection.createStatement();
                    ResultSet resultset = statement.executeQuery("select id, lastName, firstName, email from Info where id = " + id);
                    if(resultset.next()) {
                        
                        System.out.println(resultset.getString(1) + "\t" + resultset.getString(2) + "\t" + resultset.getString(3)
                        + "\t" + resultset.getString(4));
                        //PreparedStatement to issue SQL commands to delete the row
                        PreparedStatement preparedStmt = connection.prepareStatement("delete from Info where id = ?");
                        preparedStmt.setInt(1, id);
                        preparedStmt.execute();
                        System.out.println("Row at ID: " + id  + " was deleted.");
                        preparedStmt.close();
                    
                    } else {
                        System.out.println("Sorry, that ID was not found");      
                    }
                    statement.close();
                    
                } else if (answer == 'Q') {
                    System.out.println("Program ended");
                    //close connection and exit
                    connection.close();
                    scanner.close();
                    System.exit(0);
                }
                //prompt again until choose Q for quit
                s = getUserInput(firstPrompt).toUpperCase();
                answer = s.charAt(0);
            
            }
            
        } catch (ClassNotFoundException e) {
            
        } catch (SQLException ie) {
         
        }
        
    }
    /**
     * Method getUserInput will obtain the user input
     * @param prompt string that is what is asked of the user to answer
     * @return input which is a string of what the user answered
     */
    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.next();
        return input;
    }
    /**
     * Method getId will obtain the user input for ID
     * @param IdPrompt string is what is asked of the user to answer
     * @return userInput which is a integer of the ID the user gave
     */
    private static int getId(String IdPrompt) {
        System.out.print(IdPrompt);
        int userInput = scanner.nextInt();
        return userInput;
    }
    
}

