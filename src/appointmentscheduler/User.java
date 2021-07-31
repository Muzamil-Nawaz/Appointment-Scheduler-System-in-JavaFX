package appointmentscheduler;

/**
 * This method is used to store the structure of the User data from the database
 *
 * @author ADMIN
 */
public class User {

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    String name;
    String pass;

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", pass=" + pass + '}';
    }

}
