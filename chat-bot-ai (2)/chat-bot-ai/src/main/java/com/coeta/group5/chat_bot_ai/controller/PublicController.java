package com.coeta.group5.chat_bot_ai.controller;

import com.coeta.group5.chat_bot_ai.entity.Response;
import com.coeta.group5.chat_bot_ai.entity.TokenResponse;
import com.coeta.group5.chat_bot_ai.entity.User;
import com.coeta.group5.chat_bot_ai.service.AiService;
import com.coeta.group5.chat_bot_ai.service.OtpVerificationService;
import com.coeta.group5.chat_bot_ai.service.UserDetailsServiceImpl;
import com.coeta.group5.chat_bot_ai.service.UserService;
import com.coeta.group5.chat_bot_ai.utils.AESUtil;
import com.coeta.group5.chat_bot_ai.utils.JwtUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PublicController {

    Logger log = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;

    @Autowired
    private AiService aiService;

    @Autowired
    private OtpVerificationService otpVerificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @GetMapping("/ask-ai")
    public ResponseEntity<Response> askAi(
            @RequestParam(value = "query", required = false,
                    defaultValue = "How are you ? How can you help me ?") String query
    ){
        String responseText = aiService.askPublicAi(query);
        return ResponseEntity.ok(new Response(responseText));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String,String> body) {
        try {
            String ciphertext = body.get("ciphertext");
            String iv = body.get("iv");
            String decryptedJson = AESUtil.decrypt(ciphertext,iv);
            // Now parse decrypted JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> credentials = mapper.readValue(decryptedJson, new TypeReference<>() {});
            String name = credentials.get("name");
            String userName = credentials.get("email");
            String password = credentials.get("password");
            User user = new User();
            user.setFullName(name);
            user.setUserName(userName);
            user.setPassword(password);
            userService.saveNewUser(user);
            return new ResponseEntity<>("User is Signed Up successfully ",HttpStatus.OK);
        }catch(Exception e){
            log.error("Exception occurred while sign up process ", e);
            return new ResponseEntity<>("Interruption occurred while sign up ",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        try{
            String ciphertext = body.get("ciphertext");
            String iv = body.get("iv");
            String decryptedJson = AESUtil.decrypt(ciphertext,iv);
            // Now parse decrypted JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> credentials = mapper.readValue(decryptedJson, new TypeReference<>() {});

            String userName = credentials.get("email");
            String password = credentials.get("password");

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, password));
            User userData = userService.findByUserName(userName);
            String jwt = jwtUtil.generateToken(userData);
            TokenResponse token = new TokenResponse(jwt);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/generate-otp")
    public ResponseEntity<?> generateOtpAndSendMail(
            @RequestParam(value = "email", required = true) String email
    ){
        try{
            String status = otpVerificationService.generateOtpAndSendMail(email);
            if(status!=null){
                return new ResponseEntity<>(status,HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Incorrect email", HttpStatus.EXPECTATION_FAILED);
            }
        }catch(Exception e){
            log.error("Exception occurred while generating otp and sending mail ", e);
            return new ResponseEntity<>("Incorrect email", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String,String> body){
        try {

            String ciphertext = body.get("ciphertext");
            String iv = body.get("iv");
            String decryptedJson = AESUtil.decrypt(ciphertext, iv);
            // Now parse decrypted JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> credentials = mapper.readValue(decryptedJson, new TypeReference<>() {});

            String email = credentials.get("email");
            long otp = Long.parseLong(credentials.get("otp"));
            String status = otpVerificationService.verifyOtp(email, otp);
            if(status!=null){
                return new ResponseEntity<>(status,HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Incorrect email or otp", HttpStatus.EXPECTATION_FAILED);
            }
        }catch(Exception e){
            log.error("Exception occurred while otp verification ", e);
            return new ResponseEntity<>("Incorrect email or otp", HttpStatus.EXPECTATION_FAILED);
        }
    }

}