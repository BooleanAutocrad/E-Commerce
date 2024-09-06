package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.dto.user.UserOnly;
import ideas.Ecommerce.dto.user.UserOnlyDTO;
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

//    TODO: Create New User
    @PostMapping("/authenticate/register")
    public UserOnly registerUser(@RequestBody ApplicationUser user) throws Exception {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        ApplicationUser savedUser = userService.register(user);
        return new UserOnly(savedUser.getUserId(), savedUser.getUserName(), savedUser.getEmailId(), savedUser.getPassword(), savedUser.getAddress());
    }

//    TODO: Login User
    @GetMapping("/authenticate/login")
    public String userLogin(@RequestBody ApplicationUser user) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailId() , user.getPassword()));
        } catch (Exception e){
            throw new Exception("Incorrect email or password", e);
        }

        UserDetails userDetails = userService.loadUserByUsername(user.getEmailId());
        return jwtUtil.generateToken(userDetails);
    }

//    TODO: Get All Users
    @GetMapping("/users")
    public List<UserOnlyDTO> getAllUsers() {
        return userService.getAllUsers();
    }


//    TODO: Update Existing User
    @PutMapping("/admin/users")
    public UserOnly updateUser(@RequestBody ApplicationUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        ApplicationUser updatedUser = userService.updateUser(user);
        return new UserOnly(updatedUser.getUserId(), updatedUser.getUserName(), updatedUser.getEmailId(), updatedUser.getPassword(), updatedUser.getAddress());
    }

//    TODO: Delete Existing User
    @DeleteMapping("/admin/users")
    public ResponseEntity<Void> deleteUser(@RequestBody ApplicationUser user) {
        userService.deleteUserById(user.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
