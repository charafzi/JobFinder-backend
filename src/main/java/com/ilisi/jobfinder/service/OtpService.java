package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.exceptions.EmailNotExist;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OtpService {
    private static final String SECRET = "S3CR3T";
    private static final int OTP_EXPIRATION_MINUTES = 1; // durée d'expiration en minutes
    private JavaMailSender mailSender;
    private StringRedisTemplate redisTemplate;
    private SpringTemplateEngine templateEngine;
    private final UserRepository userRepository;

    public String generateAndStoreOtp(String email) throws EmailNotExist {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new EmailNotExist("No user registered with email = "+email);
        }
        Totp totp = new Totp(SECRET);
        String otp = totp.now();

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        // Stocker l'OTP avec un TTL de 5 minutes
        ops.set(email, otp, Duration.ofMinutes(OTP_EXPIRATION_MINUTES));
        return otp;
    }

    public void SaveOtpforUser(String email,String otp){
        redisTemplate.opsForValue().set(email+":otp",otp,Duration.ofMinutes(OTP_EXPIRATION_MINUTES));
    }
    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("Password Reset OTP");

        // Set up Thymeleaf context
        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("logoImage", "cid:jobFinderLogo");

        // Generate the HTML content with Thymeleaf
        String htmlContent = templateEngine.process("otp-email", context);
        helper.setText(htmlContent, true);

        ClassPathResource resource = new ClassPathResource("static/logo.png");
        helper.addInline("jobFinderLogo", resource);

        mailSender.send(message);
    }
    // Méthode pour valider l'OTP
    public boolean validateOtp(String email, String otp) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedOtp = ops.get(email);
        return otp.equals(storedOtp);
    }
    // Method to retrieve OTP for a specific user
    public String getOtpForUser(String email) {
        return redisTemplate.opsForValue().get(email + ":otp");
    }
}
