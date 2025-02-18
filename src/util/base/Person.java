package util.base;

public class Person {
    public static int idCounter = 0;
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

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
