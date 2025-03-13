package javaCode;

public class Receptionist extends Admin{
    public Receptionist(String username, String password) {
        super(username, password);
        this.role = "Receptionist"; // Override Admin role to "Receptionist"
    }
}
