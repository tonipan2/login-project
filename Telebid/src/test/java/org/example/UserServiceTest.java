package org.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    void registerUserTest(){
        User user = new User();
        user.setEmail("test@test.bg");
        user.setPassword("password");
        user.setFirstName("Johnny");
        user.setLastName("Test");

        when(userDao.existsByEmail(user.getEmail())).thenReturn(false);
        when(userDao.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser(user);
        verify(userDao).save(savedUser);
    }

    @Test
    void updateProfileTest(){
        User user = new User();
        user.setEmail("test@test.bg");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setFirstName("Johnny");
        user.setLastName("Test");

        when(userDao.update(user)).thenReturn(user);
        when(userDao.findByEmail("test@test.bg")).thenReturn(Optional.of(user));

        User userToUpdate = userService.updateProfile(user);

        verify(userDao).update(userToUpdate);

        assertEquals("Johnny", userToUpdate.getFirstName());
        assertEquals("Test", userToUpdate.getLastName());
        assertEquals("test@test.bg", userToUpdate.getEmail());
        assertTrue(userService.authenticate("test@test.bg", "password"));

    }

    @Test
    void hashPasswordTest(){
        User user = new User();
        user.setEmail("test@test.bg");
        user.setPassword("password");
        user.setFirstName("Johnny");
        user.setLastName("Test");

        when(userDao.existsByEmail(user.getEmail())).thenReturn(false);
        when(userDao.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser(user);

        assertNotEquals("password", savedUser.getPassword());
        assertTrue(BCrypt.checkpw("password", savedUser.getPassword()));
    }

    @Test
    void emailExceptionTest(){
        User user = new User();
        user.setEmail("test@test.bg");
        when(userDao.existsByEmail(user.getEmail())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(user));

        assertEquals("Email already in use", ex.getMessage());
        verify(userDao, never()).save(any());
    }

    @Test
    void authenticateTest(){
        User user = new User();
        user.setEmail("test@test.bg");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        when(userDao.findByEmail("test@test.bg")).thenReturn(Optional.of(user));
        assertTrue(userService.authenticate("test@test.bg", "password"));
        assertFalse(userService.authenticate("test@test.bg", "wrongPassword"));
    }

    @Test
    void findByEmailTest()
    {
        User user = new User();
        user.setEmail("test@test.bg");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setFirstName("Johnny");
        user.setLastName("Test");

        when(userDao.update(user)).thenReturn(user);
        when(userDao.findByEmail("test@test.bg")).thenReturn(Optional.of(user));

        User emailUser = userService.findByEmail(user.getEmail());
        assertEquals(user.getEmail(), emailUser.getEmail());
    }
}
