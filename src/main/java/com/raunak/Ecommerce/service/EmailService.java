package com.raunak.Ecommerce.service;

import com.raunak.Ecommerce.model.EmailDetails;

public interface EmailService {
    String sendMail(EmailDetails details);
}
