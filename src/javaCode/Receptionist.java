package javaCode;

import util.base.Person;

public class Receptionist extends Person{
    public Receptionist(int id,String username, String password, String firstName, String lastName,
                    String email, String phone) {
        super(id, username, password, firstName, lastName, email, phone, "Receptionist");
    }
    @Override
    public String toString() {
        return "Receptionst Profile: \n" +
        "ID: " + getId() + "\n" + 
        "Username: " + getUsername() + "\n" + 
        "First Name: " + getFirstName() + "\n" + 
        "Last Name: " + getLastName() + "\n" + 
        "Email: " + getEmail() + "\n" + 
        "Phone: " + getPhone() + "\n" + 
        "Role: "+ getRole();
}
}

