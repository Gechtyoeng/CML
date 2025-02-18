package util.base;

public interface Authentication {
    public boolean login(String username, String password);
    public boolean signUp(String username, String password, String firstName, String lastName, String email, String phone);
}
