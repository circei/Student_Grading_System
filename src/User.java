import java.util.Set;
public interface User {
    String getUsername();
    String getRole();
    Set<String> getPermissions();
}
