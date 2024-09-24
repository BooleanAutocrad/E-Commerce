package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.dto.user.UserJwtDTO;
import ideas.Ecommerce.dto.user.UserOnly;
import ideas.Ecommerce.dto.user.UserOnlyDTO;
import ideas.Ecommerce.exception.IncorrectPassword;
import ideas.Ecommerce.exception.IncorrectUserNameOrPasswordException;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.UserRepository;
import ideas.Ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    public UserJwtDTO register(ApplicationUser newUser) throws Exception {
        ApplicationUser existingUser = userRepository.findByEmailId(newUser.getEmailId());
        if(existingUser != null){
            throw new Exception("User Already Exists");
        }
        newUser.setRole("CUSTOMER");
        ApplicationUser user = userRepository.save(newUser);
        String jwtToken = jwtUtil.generateToken(loadUserByUsername(user.getEmailId()));
        return new UserJwtDTO(user.getUserId(), user.getUserName(), user.getEmailId(), user.getAddress(), jwtToken);
    }

    public UserJwtDTO sendUserAndJWT(String emailId){
        ApplicationUser user = userRepository.findByEmailId(emailId);
        if (user == null) {
            throw new ResourceNotFound("User");
        }

        String jwtToken = jwtUtil.generateToken(loadUserByUsername(emailId));
        return new UserJwtDTO(user.getUserId(), user.getUserName(), user.getEmailId(), user.getAddress(), jwtToken);
    }

    public List<UserOnlyDTO> getAllUsers(){
        return userRepository.findBy();
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
        if (userRepository.existsById(id)) {
            throw new ResourceNotDeleted("User");
        }
    }

    public ApplicationUser updateUser(ApplicationUser user){

        ApplicationUser existingUser = userRepository.findByEmailId(user.getEmailId());

        if(existingUser == null){
            throw new ResourceNotFound("User");
        }

        existingUser.setUserName(user.getUserName());
        existingUser.setEmailId(user.getEmailId());
        if(user.getPassword() != null){
            existingUser.setPassword(user.getPassword());
        }
        existingUser.setAddress(user.getAddress());


        return userRepository.save(existingUser);
    }

    public UserOnly findUserByEmailId(ApplicationUser user){
        ApplicationUser existingUser = userRepository.findByEmailId(user.getEmailId());
        if (existingUser == null) {
            throw new ResourceNotFound("User");
        }
        return convertToUserOnlyDTO(existingUser);
    }

    private UserOnly convertToUserOnlyDTO(ApplicationUser user) {
        return new UserOnly(user.getUserId(), user.getUserName(), user.getEmailId(), user.getAddress());
    }

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmailId(emailId);
        if (user == null) {
            throw new IncorrectUserNameOrPasswordException();
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailId())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}