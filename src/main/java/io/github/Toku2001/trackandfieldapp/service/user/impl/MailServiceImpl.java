package io.github.Toku2001.trackandfieldapp.service.user.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import io.github.Toku2001.trackandfieldapp.dto.password.PasswordResetRequest;
import io.github.Toku2001.trackandfieldapp.service.user.MailService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    //resendのAPIKey
    @Value("${resend.api.key}")
    private String resendApiKey;
    //パスワード再設定メールの送り主メールアドレス
    @Value("${resend.from.email}")
    private String fromEmail;

    public void sendPasswordResetMail(PasswordResetRequest userRequest, String resetLink){
    	Resend resend = new Resend(resendApiKey);
    	CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(userRequest.getUserMail())
                .subject("【パスワード再設定】リンクのご案内")
                .html(
                  "<p>以下のリンクからパスワードを再設定してください。</p>" +
                  "<p><a href=\"" + resetLink + "\">パスワードを再設定する</a></p>" +
                  "<p>このリンクの有効期限は24時間です。</p>" +
                  "<p>もしこのリクエストに心当たりがない場合は、このメールを無視してください。</p>"
                  )
                .build();

         try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}