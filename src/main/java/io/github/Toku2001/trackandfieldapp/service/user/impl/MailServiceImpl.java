package io.github.Toku2001.trackandfieldapp.service.user.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;
import io.github.Toku2001.trackandfieldapp.exception.MailSendException;
import io.github.Toku2001.trackandfieldapp.service.user.MailService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    //SEND.GRIDのAPIKey
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;
    //パスワード再設定メールの送り主メールアドレス
    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public void sendPasswordResetMail(PasswordResetRequest userRequest, String resetLink){
        Email from = new Email(fromEmail);
        String subject = "【パスワード再設定】リンクのご案内";
        Email to = new Email(userRequest.getUserMail());
        String contentText = "以下のリンクからパスワードを再設定してください。\n\n" + resetLink;
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            if (response.getStatusCode() >= 400) {
                throw new MailSendException("メール送信失敗: " + response.getBody());
            }

        } catch (IOException | RuntimeException e) {
            throw new MailSendException("メール送信中にエラーが発生しました。", e);
        }
    }
}