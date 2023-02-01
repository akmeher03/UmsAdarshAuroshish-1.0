package UserQuizManagement.demoUserQuiz.Controller;
import java.security.Principal;

import UserQuizManagement.demoUserQuiz.CustomException;
import UserQuizManagement.demoUserQuiz.DTO.AuthRequest;
import UserQuizManagement.demoUserQuiz.DTO.ForgotPasswordDto;
import UserQuizManagement.demoUserQuiz.Entity.Users;
import UserQuizManagement.demoUserQuiz.Service.JWTService;
import UserQuizManagement.demoUserQuiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

//    @PreAuthorize()
    @GetMapping("/admin/allusers")
    public List<Users> getAllUser(){
        return userService.getUsers();
    }

    @GetMapping("/users/{userId}")
    public Users getUserById(@PathVariable(value = "userId") Long userId,Principal principal) throws CustomException {
        Users users = userService.getUserById(userId);
        if(principal.getName().equals(users.getUserEmail())) {
            return userService.getUserById(userId);
        }
        else{
            throw new CustomException("you are not authorised to view this detail");
        }
    }

//    @PostMapping("/users/login")
//    public ResponseEntity<Users> loginUser(@RequestBody Users user) throws Exception {
//        Users loginUser = userService.loginUser(user);
//        return  ResponseEntity.ok(loginUser);
//    }
//   @PostMapping("/users/login")
//    public String userAuthenticate(@RequestBody AuthRequest authRequest){
//    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserEmail(),authRequest.getUserPassword()));;
//    if (authentication.isAuthenticated()) {
//        return jwtService.generateToken(authRequest.getUserEmail());
//    } else {
//        throw new UsernameNotFoundException("Invalid User Request");
//    }
//}

//    @PostMapping("/admin/login")
//    public ResponseEntity<Users> adminUser(@RequestBody Users user) throws Exception {
//        Users adminUser = userService.adminUser(user);
//        return  ResponseEntity.ok(adminUser);
//    }

    @PutMapping("/forgotpassword")
    public String forgotPwd(@RequestBody ForgotPasswordDto forgotPasswordDto) throws CustomException {
         Users updatedUser = userService.forgotPassword(forgotPasswordDto);
         return "Password changed successfully";
    }


    @PostMapping("/createusers")
    public Users createUser(@RequestBody Users user) throws Exception {
        return userService.createNewUser(user);
    }
    @PostMapping("/admin/create")
    public Users createAdmin(@RequestBody Users user) throws Exception {
        return userService.createNewAdmin(user);
    }

    @PutMapping("/updateusers")
    public ResponseEntity<Users> updateUser(@RequestBody Users user,Principal principal ) throws CustomException {
        Users users = userService.getUserById(user.getUserId());
        if(principal.getName().equals(users.getUserEmail())) {
            Users updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        }
        else{
            throw new CustomException("you are not authorised to view this detail");
        }

    }

    @DeleteMapping("admin/deleteusers/{userId}")
    public Map<String,Boolean> deleteUsers(@PathVariable(value = "userId") Long userId) throws CustomException {
        return userService.deleteUser(userId);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserEmail(),authRequest.getUserPassword()));;
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUserEmail());
        } else {
            throw new UsernameNotFoundException("Invalid User Request");
        }
    }

}
