package ToDo.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "비밀번호 재설정 요청";
        String content = "<p>비밀번호를 재설정하려면 아래 링크를 클릭해주세요:</p>"
                + "<a href=\"" + resetLink + "\">비밀번호 재설정</a>";

        sendEmail(to, subject, content);
    }

    private void sendEmail(String to, String subject, String content) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("이메일 전송에 실패하였습니다.", e);
        }
    }


}
