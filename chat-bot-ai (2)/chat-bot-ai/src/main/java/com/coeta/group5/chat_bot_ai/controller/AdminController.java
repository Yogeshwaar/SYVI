package com.coeta.group5.chat_bot_ai.controller;

import com.coeta.group5.chat_bot_ai.entity.NameEmailDTO;
import com.coeta.group5.chat_bot_ai.entity.PromptContentRequestObj;
import com.coeta.group5.chat_bot_ai.entity.PromptContentResponseObj;
import com.coeta.group5.chat_bot_ai.entity.User;
import com.coeta.group5.chat_bot_ai.helper.PromptHandler;
import com.coeta.group5.chat_bot_ai.service.UserService;
import com.coeta.group5.chat_bot_ai.utils.AESUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PromptHandler promptHandler;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<NameEmailDTO> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createUser(@RequestBody  Map<String,String> body) {
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
            userService.saveAdmin(user);
            return new ResponseEntity<>("User is Signed Up successfully ",HttpStatus.OK);
        }catch(Exception e){
            log.error("Exception occurred while sign up process ", e);
            return new ResponseEntity<>("Interruption occurred while sign up ",HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/update-prompt")
    public ResponseEntity<String> updatePrompts(
            @RequestParam(value = "type",
                    defaultValue = "public") String promptType,
            @RequestBody PromptContentRequestObj requestObj
            ){
        if(!requestObj.getPromptContent().isEmpty()) {
            boolean status = promptHandler.updatePrompt(promptType, requestObj);
            if (status) {
                return new ResponseEntity<>("Successfully updated the prompt", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unable to update the prompt", HttpStatus.EXPECTATION_FAILED);
            }
        }else{
            return new ResponseEntity<>("Prompt Body can not be empty", HttpStatus.LENGTH_REQUIRED);
        }
    }

    @GetMapping("/get-prompts")
    public ResponseEntity<PromptContentResponseObj> getPrompts(){
        PromptContentResponseObj responseObj = promptHandler.getALlPrompts();
        if(!responseObj.getInternalPrompt().isEmpty()||!responseObj.getPublicPrompt().isEmpty()){
            return new ResponseEntity<>(responseObj,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
}