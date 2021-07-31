/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class represents the CustomersMenu which provides all available
 * operations to be performed on customers
 *
 * @author
 */
public class CustomerMenu extends Stage {

    Optional<String> result;
    int id;

    /**
     * Constructor to create a JavaFx stage with required UI elements, to be
     * used to perform different operations.
     */
    CustomerMenu() {
        // Declaring required buttons for the form
        Button addNewCustomer = null, updateExistingCustomer = null, deleteExistingCustomer = null, displayCustomers = null, backToStarterMenu = null;

        String language = Locale.getDefault().getLanguage();

        Scene scene;

        // Setting up above declared buttons
        displayCustomers = new Button("View all Customers");
        displayCustomers.setMinWidth(200);
        
        displayCustomers.setFont(new Font("Open Sans",15));
        displayCustomers.setStyle("-fx-background-color: #EB984E ");
        displayCustomers.setTextFill(Color.WHITE);
        
        
        addNewCustomer = new Button("Add new Customers");
        addNewCustomer.setMinWidth(200);
        
        
        addNewCustomer.setFont(new Font("Open Sans",15));
        addNewCustomer.setStyle("-fx-background-color: #EB984E ");
        addNewCustomer.setTextFill(Color.WHITE);

        updateExistingCustomer = new Button("Update existing customers");
        updateExistingCustomer.setMinWidth(200);
        
        updateExistingCustomer.setFont(new Font("Open Sans",15));
        updateExistingCustomer.setStyle("-fx-background-color: #EB984E ");
        updateExistingCustomer.setTextFill(Color.WHITE);

        deleteExistingCustomer = new Button("Delete existing customers");
        deleteExistingCustomer.setMinWidth(200);
        
        deleteExistingCustomer.setFont(new Font("Open Sans",15));
        deleteExistingCustomer.setStyle("-fx-background-color: #EB984E ");
        deleteExistingCustomer.setTextFill(Color.WHITE);

        backToStarterMenu = new Button("Back to starting menu ");
        backToStarterMenu.setMinWidth(200);
        
        backToStarterMenu.setFont(new Font("Open Sans",15));
        backToStarterMenu.setStyle("-fx-background-color: #EB984E ");
        backToStarterMenu.setTextFill(Color.WHITE);
        
        

        // setting up action on back button
        backToStarterMenu.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                // Move back to main menu and hide this frame
                toStarterMenu();

            }
        });
        // setting up action on view customers button
        displayCustomers.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // Initialize the ShowCustomerRecords frame and display it and hide current frame
                ShowCustomerRecords vc = new ShowCustomerRecords();
                vc.show();
                tohide();

            }
        });

        // setting up action on Add customers button
        addNewCustomer.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                // Initializing AddNewCustomerRecord frame to be visible and hiding this frame
                AddNewCustomerRecord c = new AddNewCustomerRecord(new Customer());
                c.show();
                tohide();

            }
        });

        // setting up action on Update customers button
        updateExistingCustomer.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                // Setting up dialog for taking customer id as input which needs to updated
                TextInputDialog td = new TextInputDialog();
                td.setHeaderText("Enter id to update customer: ");

                result = td.showAndWait();

                // take the input and store in the id variable
                result.ifPresent(input -> id = Integer.parseInt(input));
                // Check if customer with given id exists
                Customer c = checkCustomer(id);

                // If it doesn't exist, display custom message
                if (c == null) {
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Requested customer doesn't exist with id:" + id + ", please try again.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                } // If it exists Display the AddNewCustomerRecord frame with current details to update and hide this frame
                else {
                    AddNewCustomerRecord ac = new AddNewCustomerRecord(c);
                    ac.show();
                    tohide();
                }

            }
        });

        // Settig up action on delete customers button
        deleteExistingCustomer.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                // Show input dialog to get the customer id as input which needs to be deleted
                TextInputDialog td = new TextInputDialog();
                td.setHeaderText("Enter id to delete customer: ");

                result = td.showAndWait();

                // Get the input and store it in the id variable
                result.ifPresent(input -> id = Integer.parseInt(input));
                // Check if customer exists
                Customer c = checkCustomer(id);
                // If doesn't exists, display custom message
                if (c == null) {
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Requested customer doesn't exist with id:" + id + ", please try again.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                }
                // if it exists, then delete the customer with given id and display custom message
                if (deleteCustomer(id)) {

                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Requested customer with id:" + id + " deleted successfully.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();

                }
                else{
                    Dialog<String> dialog = new Dialog<>();
                        //Setting the title
                        dialog.setTitle("Dialog");
                        ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                        //Setting the content of the dialog
                        dialog.setContentText("Some problem occured while deleting appointment,try again.");
                        //Adding buttons to the dialog pane
                        dialog.getDialogPane().getButtonTypes().add(btype);
                        dialog.showAndWait();
                
                }
            }
        });
        VBox vBox = new VBox();

        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMargin(displayCustomers, new Insets(20, 20, 20, 20));
        vBox.setMargin(addNewCustomer, new Insets(20, 20, 20, 20));
        vBox.setMargin(updateExistingCustomer, new Insets(20, 20, 20, 20));
        vBox.setMargin(deleteExistingCustomer, new Insets(20, 20, 20, 20));
        vBox.setMargin(backToStarterMenu, new Insets(20, 20, 20, 20));
        //retrieving the observable list of the VBox 
        ObservableList list = vBox.getChildren();

        //Adding all the nodes to the observable list 
        list.addAll(displayCustomers, addNewCustomer, updateExistingCustomer, deleteExistingCustomer, backToStarterMenu);

        scene = new Scene(vBox, 500, 500);
        this.setScene(scene);
        this.setTitle("Customers Menu");
        
        this.setResizable(false);

    }

    /**
     * This method is used to navigate back to MainMenuForm and hiding current
     * form
     */
    public void toStarterMenu() {

        StartingMenu m = new StartingMenu();
        m.show();
        tohide();

    }

    /**
     * This method is used to hide current form
     */
    void tohide() {
        this.hide();
    }

    /**
     * This method is used to check if customer with given id exists in database
     *
     * @param id referring the customer id
     * @return Customer details if exists, otherwise null
     */
    public Customer checkCustomer(int id) {
        Connection con = DbProvider.makeConnection();
        Statement stmt;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(" select * from customers where Customer_ID = " + id);
            if (rs.next()) {
                Customer c = new Customer();
                c.setId(Integer.parseInt(rs.getString("Customer_Id")));
                c.setName(rs.getString("Customer_Name"));
                c.setAddress(rs.getString("Address"));
                c.setPhone(rs.getString("Phone"));
                c.setPostalCode(rs.getString("Postal_Code"));
                ResultSet rs2 =stmt.executeQuery("Select * from first_level_divisions where Division_ID="+rs.getInt("Division_ID"));
                rs2.next();
                int country_id = rs2.getInt("Country_ID");
                c.setDivision(rs2.getString("Division"));
                
                rs2 =stmt.executeQuery("Select Country from countries where Country_ID="+country_id);
                rs2.next();
                
            
                c.setCountry(rs2.getString("country"));
                return c;

            }

        } catch (SQLException ex) {
             ;
            return null;
        }
        return null;
    }

    /**
     * This method is used to delete customer from the database with given id
     *
     * @param id referring customer id which needs to be deleted
     * @return true if successfully deleted the customer otherwise false
     */
    public boolean deleteCustomer(int id) {
        Connection con = DbProvider.makeConnection();
        Statement stmt;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate("delete from appointments where Customer_ID = " + id);
            int a = stmt.executeUpdate(" delete from customers where Customer_ID = " + id);
            if (a == 0) {
                return false;
            }

        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

}
