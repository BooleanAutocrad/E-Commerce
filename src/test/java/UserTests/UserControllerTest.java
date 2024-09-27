package UserTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import ideas.Ecommerce.CapstoneDataApplication;
import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.dto.user.UserJwtDTO;
import ideas.Ecommerce.exception.IncorrectUserNameOrPasswordException;
import ideas.Ecommerce.service.UserService;
import ideas.Ecommerce.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CapstoneDataApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    AuthenticationManager authenticationManager;

    String header;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldRegisterUser() throws Exception {
        ApplicationUser user = new ApplicationUser();
        user.setUserName("testuser");
        user.setEmailId("test@example.com");
        user.setPassword("password");

        UserJwtDTO userJwtDTO = new UserJwtDTO(1, "testuser", "test@example.com", "123 Main St", "mockJwtToken");

        when(userService.register(any(ApplicationUser.class))).thenReturn(userJwtDTO);

        mockMvc.perform(post("/authenticate/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailId").value("test@example.com"))
                .andExpect(jsonPath("$.jwtToken").value("mockJwtToken"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
    @Test
    void shouldLoginUser() throws Exception {
        ApplicationUser user = new ApplicationUser();
        user.setEmailId("test@example.com");
        user.setPassword("password");

        UserJwtDTO userJwtDTO = new UserJwtDTO(1, "testuser", "test@example.com", "123 Main St", "token123");

        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("test@example.com", "password"));

        when(userService.sendUserAndJWT("test@example.com")).thenReturn(userJwtDTO);

        mockMvc.perform(post("/authenticate/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailId").value("test@example.com"))
                .andExpect(jsonPath("$.jwtToken").value("token123"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
    @Test
    void shouldThrowIncorrectUserNameOrPasswordException() throws Exception {
        ApplicationUser user = new ApplicationUser();
        user.setEmailId("test@example.com");
        user.setPassword("wrongpassword");

        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new IncorrectUserNameOrPasswordException());

        mockMvc.perform(post("/authenticate/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

}
