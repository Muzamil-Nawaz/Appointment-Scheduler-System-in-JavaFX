package appointmentscheduler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class is used to display all the customers present in database with
 * complete details in table view form
 *
 * @author
 */
public class ShowCustomerRecords extends Stage {

    /**
     * Constructor to create the instance of Stage whenever initialized and
     * display the requested data
     */
    ShowCustomerRecords() {
        // TableView representing the table in form
        TableView tableView = new TableView();

        // TableColumns representing each column in the TableVeiw 
        TableColumn<Customer, String> column1 = new TableColumn<>("Id");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> column2 = new TableColumn<>("Customer Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> column3 = new TableColumn<>("Address");
        column3.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customer, String> column4 = new TableColumn<>("Division");
        column4.setCellValueFactory(new PropertyValueFactory<>("division"));

        TableColumn<Customer, String> column5 = new TableColumn<>("Country");
        column5.setCellValueFactory(new PropertyValueFactory<>("country"));

        TableColumn<Customer, String> column6 = new TableColumn<>("Postal Code");
        column6.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        TableColumn<Customer, String> column7 = new TableColumn<>("Phone no");
        column7.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Adding above declared columns to the tableview
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);
        tableView.getColumns().add(column6);
        tableView.getColumns().add(column7);

        // Getting all the customers data from the database using getCustomers method
        ArrayList<Customer> customers = getCustomers();

        // Back button initialization
        Button back = new Button("Back to customers menu");
        // Setting up action on back button
        back.setOnAction((event) -> {
            // hiding the current form
            this.hide();

            // Initializing CustomersMenu to display
            CustomerMenu cm = new CustomerMenu();
            cm.show();
        });

        // Populating tableview with all customers details retrieved from getCustomers method above
        for (int i = 0; i < customers.size(); i++) {
            tableView.getItems().add(customers.get(i));
        }

        VBox box = new VBox();
        box.getChildren().addAll(tableView, back);
        Scene scene = new Scene(box, 700, 500);
        this.setScene(scene);
        this.setTitle("View Customers");
        this.setResizable(false);
        this.show();

    }

    /**
     * This method is used to retrieve all the customer details from the
     * database
     *
     * @return an ArrayList of Customer containing all the customers instances
     * retrieved from database
     */
    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = null;
        try {
            customers = new ArrayList<>();
            Connection con = DbProvider.makeConnection();
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            Statement st3 = con.createStatement();
            ResultSet rs = st.executeQuery("select * from customers");
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(Integer.parseInt(rs.getString("Customer_id")));
                c.setName(rs.getString("Customer_Name"));
                c.setAddress(rs.getString("address"));
                c.setPhone(rs.getString("phone"));
                c.setPostalCode(rs.getString("Postal_Code"));
                ResultSet rs2 = st2.executeQuery("select * from first_level_divisions where Division_ID="+rs.getInt("Division_ID"));
                rs2.next();
                c.setDivision(rs2.getString("Division"));
                ResultSet rs3 = st3.executeQuery("select Country from countries where Country_ID="+rs2.getInt("Country_ID"));
                rs3.next();
                

                c.setCountry(rs3.getString("country"));
                customers.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return customers;
        }

        return customers;
    }
}
