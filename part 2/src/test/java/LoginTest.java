import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.Login;

public class LoginTest {

    private final Login login = new Login();

    @Test
    // Test username validation
    public void testCheckUserName() {
        assertTrue(login.checkUserName("kyl_1"));
        assertFalse(login.checkUserName("kyle!!!!!!"));
        assertEquals(true, login.checkUserName("kyl_1"));
        assertEquals(false, login.checkUserName("kyle!!!!!!"));
    }

    @Test
    // Test password complexity validation
    public void testCheckPasswordComplexity() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
        assertFalse(login.checkPasswordComplexity("password"));
        assertEquals(true, login.checkPasswordComplexity("Ch&&sec@ke99!"));
        assertEquals(false, login.checkPasswordComplexity("password"));
    }

    @Test
    // Test cellphone number validation
    public void testCheckCellPhoneNumber() {
        assertTrue(login.checkCellPhoneNumber("+27888968976"));
        assertFalse(login.checkCellPhoneNumber("27888968976"));
        assertEquals(true, login.checkCellPhoneNumber("+27888968976"));
        assertEquals(false, login.checkCellPhoneNumber("27888968976"));
    }

    @Test
    // Test login status validation
    public void testReturnLoginStatus() {
        Login.User user = login.new User("user_1", "Password1!", "+27821234567", "John", "Doe");
        login.registerUser(user);
        assertTrue(login.returnLoginStatus("user_1", "Password1!"));
        assertFalse(login.returnLoginStatus("user_1", "WrongPassword"));
    }
}
    