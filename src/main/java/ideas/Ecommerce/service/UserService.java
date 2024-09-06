package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.dto.user.UserOnly;
import ideas.Ecommerce.dto.user.UserOnlyDTO;
import ideas.Ecommerce.exception.IncorrectPassword;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.UserRepository;
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


    public ApplicationUser register(ApplicationUser user) throws Exception {
        ApplicationUser existingUser = userRepository.findByEmailId(user.getEmailId());
        if(existingUser != null){
            throw new Exception("User Already Exists");
        }
        return userRepository.save(user);
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
        existingUser.setUserName(user.getUserName());
        existingUser.setEmailId(user.getEmailId());
        existingUser.setPassword(user.getPassword());
        existingUser.setAddress(user.getAddress());
        existingUser.setRole(existingUser.getRole());
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
        return new UserOnly(user.getUserId(), user.getUserName(), user.getEmailId(), user.getAddress(), user.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmailId(emailId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + emailId);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailId())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}