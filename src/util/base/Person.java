package util.base;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Person implements Authentication {
    protected int id;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String role;
    protected static List<Person> users = new ArrayList<>(); 

    public Person(String username, String password, String firstName, String lastName, String email, String phone, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public Person(int userId, String username, String password, String firstName, String lastName, String email, String phone, String role) {
        this.id = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
       @Override
    public boolean login(String username, String password, String role) {
        if(this.username.equals(username) && this.password.equals(password) && this.role.equals(role)) {
            System.out.println("Login successful");
            return true;
        }
        return false;
    }
    @Override
    public boolean signUp() {

        if (isUsernameTaken(username)) {
            System.out.println("Error: Username '" + username + "' is already taken.");
            return false;
        } else {
            users.add(this);
            System.out.println("User " + username + " signed up successfully.");
            return true;
        }
    }
    public boolean isUsernameTaken(String username){
        for(Person user : users){
            if(user.username.equals(username)){
                return true;
            }
        }
        return false;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public int getId() {
        return this.id;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPhone() {
        return this.phone;
    }
    public String getRole() {
        return this.role;
    }
    
}
