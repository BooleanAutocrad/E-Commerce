package ideas.Ecommerce.ServiceTests;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.dto.user.UserJwtDTO;
import ideas.Ecommerce.dto.user.UserOnly;
import ideas.Ecommerce.dto.user.UserOnlyDTO;
import ideas.Ecommerce.exception.IncorrectUserNameOrPasswordException;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.UserRepository;
import ideas.Ecommerce.service.UserService;
import ideas.Ecommerce.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private ApplicationUser user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new ApplicationUser(1, "testUser", "test@example.com", "password", "address", "CUSTOMER", null, null, null);
    }

    @Test
    void testRegisterUser() throws Exception {
        when(userRepository.findByEmailId(anyString())).thenReturn(null).thenReturn(user);
        when(userRepository.save(any(ApplicationUser.class))).thenReturn(user);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwtToken");

        UserJwtDTO result = userService.register(user);

        assertEquals("testUser", result.getUserName());
        assertEquals("jwtToken", result.getJwtToken());
        verify(userRepository, times(1)).save(any(ApplicationUser.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        when(userRepository.findByEmailId(anyString())).thenReturn(user);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.register(user);
        });

        assertEquals("User Already Exists", exception.getMessage());
        verify(userRepository, never()).save(any(ApplicationUser.class));
    }

    @Test
    void testSendUserAndJWT() {
        when(userRepository.findByEmailId(anyString())).thenReturn(user);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwtToken");

        UserJwtDTO result = userService.sendUserAndJWT("test@example.com");

        assertEquals("testUser", result.getUserName());
        assertEquals("jwtToken", result.getJwtToken());
    }

    @Test
    void testSendUserAndJWTUserNotFound() {
        when(userRepository.findByEmailId(anyString())).thenReturn(null);

        assertThrows(ResourceNotFound.class, () -> {
            userService.sendUserAndJWT("test@example.com");
        });
    }

    @Test
    void testGetAllUsers() {
        List<UserOnlyDTO> users = new ArrayList<>();
        when(userRepository.findBy()).thenReturn(users);

        List<UserOnlyDTO> result = userService.getAllUsers();

        assertEquals(users, result);
        verify(userRepository, times(1)).findBy();
    }

    @Test
    void testDeleteUserById() {
        when(userRepository.existsById(anyInt())).thenReturn(false);

        userService.deleteUserById(user.getUserId());

        verify(userRepository, times(1)).deleteById(user.getUserId());
    }

    @Test
    void testDeleteUserByIdResourceNotDeleted() {
        when(userRepository.existsById(anyInt())).thenReturn(true);

        assertThrows(ResourceNotDeleted.class, () -> {
            userService.deleteUserById(user.getUserId());
        });
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findByEmailId(anyString())).thenReturn(user);
        when(userRepository.save(any(ApplicationUser.class))).thenReturn(user);

        ApplicationUser result = userService.updateUser(user);

        assertEquals("testUser", result.getUserName());
        verify(userRepository, times(1)).save(any(ApplicationUser.class));
    }

    @Test
    void testFindUserByEmailId() {
        when(userRepository.findByEmailId(anyString())).thenReturn(user);

        UserOnly result = userService.findUserByEmailId(user);

        assertEquals("testUser", result.getUserName());
    }

    @Test
    void testFindUserByEmailIdNotFound() {
        when(userRepository.findByEmailId(anyString())).thenReturn(null);

        assertThrows(ResourceNotFound.class, () -> {
            userService.findUserByEmailId(user);
        });
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findByEmailId(anyString())).thenReturn(user);

        UserDetails result = userService.loadUserByUsername("test@example.com");

        assertEquals(user.getEmailId(), result.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByEmailId(anyString())).thenReturn(null);

        assertThrows(IncorrectUserNameOrPasswordException.class, () -> {
            userService.loadUserByUsername("test@example.com");
        });
    }
}
