package com.fiora.note2.service;

import com.fiora.note2.dao.EmailRecordRepository;
import com.fiora.note2.dao.EmailRepository;
import com.fiora.note2.dao.EmailUserRepository;
import com.fiora.note2.dao.UserRepository;
import com.fiora.note2.model.Email;
import com.fiora.note2.model.EmailRecord;
import com.fiora.note2.model.EmailUser;
import com.fiora.note2.model.User;
import com.fiora.note2.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailRecordRepository emailRecordRepository;
    @Autowired
    private EmailUserRepository emailUserRepository;
    private final static String EMAIL_PATH = "http://localhost:6666/email";

    public void sendEmail(String update) {
        Email email = emailRepository.findByName(update);
        if(email == null) {
            log.error("Error getting email with name {}", update);
            return;
        }
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<EmailRecord> emailRecordList = emailRecordRepository.findByEmailIdAndCreateTimeBetween(email.getId(), start, end);
        if (emailRecordList != null && emailRecordList.size() > 0) {
            return;
        }
        List<EmailUser> emailUserList = emailUserRepository.findByEmailId(email.getId());
        if ( emailUserList == null || emailRecordList.size() == 0) {
            log.error("Error getting userId with emailId {} today ", email.getId());
            return ;
        }

        List<User> userList = new ArrayList<>();
        for (EmailUser emailUser: emailUserList ) {
            Optional<User> user = userRepository.findById(emailUser.getUserId());
            if (user.isPresent()) {
                userList.add(user.get());
            }
        }

        if (userList.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", email.getTitle());
            jsonObject.put("content", email.getContent());
            jsonObject.put("token", "68d5baa0c413a619f00ad7f7a2b0eacd");
            JSONArray arr = new JSONArray();
            for(User user: userList) {
                arr.add(user.getEmail());
            }
            jsonObject.put("emailAddr", arr);
            HttpUtils.httpPostRequest(EMAIL_PATH, null, jsonObject.toString());
            log.info("Email request has been sent. {}", jsonObject.toString());
            for(User user: userList) {
                EmailRecord emailRecord = new EmailRecord();
                emailRecord.setEmailId(email.getId());
                emailRecord.setUserId(user.getId());
                emailRecord.setCreateTime(LocalDateTime.now());
                emailRecordRepository.saveAndFlush(emailRecord);
            }
        }
    }
}
