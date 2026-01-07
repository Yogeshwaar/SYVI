package com.coeta.group5.chat_bot_ai.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email_otp_entries")
@Data
public class EmailOtpEntry {

    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String email;

    public EmailOtpEntry() {
    }

    public EmailOtpEntry(ObjectId id, String email, long otp, String status) {
        this.id = id;
        this.email = email;
        this.otp = otp;
        this.status = status;
    }

    private long otp;
    private String status;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getOtp() {
        return otp;
    }

    public void setOtp(long otp) {
        this.otp = otp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
