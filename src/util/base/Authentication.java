package util.base;

public interface Authentication {
    public boolean login(String username, String password,String role);
    public boolean signUp();
}
