package com.coeta.group5.chat_bot_ai.service;

import com.coeta.group5.chat_bot_ai.entity.EmailOtpEntry;
import com.coeta.group5.chat_bot_ai.repository.EmailOtpEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.coeta.group5.chat_bot_ai.helper.ConstantFileHandler.EMAIL_BODY;
import static com.coeta.group5.chat_bot_ai.helper.ConstantFileHandler.SUBJECT;

@Service
public class OtpVerificationService {

    Logger log = LoggerFactory.getLogger(OtpVerificationService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailOtpEntryRepository emailOtpEntryRepository;

    public String generateOtpAndSendMail(String email){
        long otp = generateOtp();
        EmailOtpEntry entry = emailOtpEntryRepository.findByEmail(email);
        log.info(""+entry);
        if(entry==null) {
            entry = new EmailOtpEntry();
            entry.setEmail(email);
        }
        entry.setOtp(otp);
        entry.setStatus("generated");
        EmailOtpEntry savedEntry = emailOtpEntryRepository.save(entry);
        boolean isSent = generateAndSendMail(savedEntry);
        if(isSent) {
            return "OTP generated and email has sent to user";
        }
        return null;
    }

    public String verifyOtp(String email, long otp){
        EmailOtpEntry entry = emailOtpEntryRepository.findByEmailAndOtp(email,otp);
        if(entry.getEmail().equalsIgnoreCase(email)&&entry.getOtp()==otp){
            entry.setStatus("Email verified");
            emailOtpEntryRepository.save(entry);
            return "Email is verified";
        }
        return null;
    }

    private long generateOtp(){
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    private boolean generateAndSendMail(EmailOtpEntry entry){
        try{
            emailService.sendMail(entry.getEmail(),EMAIL_BODY.replace("{{OTP}}",entry.getOtp()+""),SUBJECT);
            entry.setStatus("Email sent");
            emailOtpEntryRepository.save(entry);
            return true;
        }catch(Exception e){
            log.error("Exception occurred while sending the email to the user",e);
        }
        return false;
    }
}
