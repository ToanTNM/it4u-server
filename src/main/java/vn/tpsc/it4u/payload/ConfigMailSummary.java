package vn.tpsc.it4u.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ConfigMailSummary {
    private Long id;
    private String serviceMail;
    private String hostMail;
    private String portMail;
    private String maxMessages;
    private String rateDelta;
    private String rateLimit;
    private String usernameMail;
    private String passwordMail;
    private String ccMail;
    private String subjectMail;
    private String textMail;
    private String cronjobMail;
}
