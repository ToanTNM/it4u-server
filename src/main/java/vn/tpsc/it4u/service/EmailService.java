package vn.tpsc.it4u.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService{
    public void sendEmail(SimpleMailMessage email);
}