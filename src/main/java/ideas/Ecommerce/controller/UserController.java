package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.dto.user.UserJwtDTO;
import ideas.Ecommerce.dto.user.UserOnly;
import ideas.Ecommerce.dto.user.UserOnlyDTO;
import ideas.Ecommerce.exception.IncorrectUserNameOrPasswordException;
import ideas.Ecommerce.service.UserService;
import ideas.Ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

//    TODO: Get user reviews

//    TODO: Create New User
    @PostMapping("/authenticate/register")
    public UserJwtDTO registerUser(@RequestBody ApplicationUser user) throws Exception {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userService.register(user);
    }

//    TODO: Login User
    @PostMapping("/authenticate/login")
    public UserJwtDTO userLogin(@RequestBody ApplicationUser user) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailId() , user.getPassword()));
        } catch (Exception e){
            throw new IncorrectUserNameOrPasswordException();
        }

        return userService.sendUserAndJWT(user.getEmailId());
    }

//    TODO: Get All Users
    @GetMapping("/users")
    public List<UserOnlyDTO> getAllUsers() {
        return userService.getAllUsers();
    }

//    TODO: Update Existing User
    @PutMapping("/admin/users")
    public UserOnly updateUser(@RequestBody ApplicationUser user) {
        if(user.getPassword() != null){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        ApplicationUser updatedUser = userService.updateUser(user);
        return new UserOnly(updatedUser.getUserId(), updatedUser.getUserName(), updatedUser.getEmailId(), updatedUser.getAddress());
    }

//    TODO: Delete Existing User
    @DeleteMapping("/admin/users")
    public ResponseEntity<Void> deleteUser(@RequestBody ApplicationUser user) {
        userService.deleteUserById(user.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
