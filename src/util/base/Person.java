package util.base;

public class Person {
    public static int idCounter = 0;
    protected int id;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Person(String username, String password, String firstName, String lastName, String email, String phone) {

        this.id = idCounter++;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
