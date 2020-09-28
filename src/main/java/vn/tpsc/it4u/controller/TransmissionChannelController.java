package vn.tpsc.it4u.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import vn.tpsc.it4u.util.ApiRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("${app.api.version}")
public class TransmissionChannelController {
    String urlLoginUcrm = "https://member.tpsc.vn/login";
    String urlCheckLogin = "https://member.tpsc.vn/login-check";
    String urlGetToken = "https://member.tpsc.vn/get-jwt";

    @ApiOperation(value = "Get clients")
    @GetMapping("/it4u/clients")
    public String getAllClient() {
        ApiRequest apiRequest = new ApiRequest();
        String data = apiRequest.getTokenUcrm(urlLoginUcrm);
        final String regex = "name=\"_csrf_token\" value=\"(.*)\">.*verticalRhythm";
        String getGroup = regex(regex, data);
        final String regexToken = "(.*)\">";
        String token = regex(regexToken, getGroup);
        String dataPost = "_csrf_token=" + token + "&_username=lap.dang%40tpsc.vn&_password=Nccvbcl0167%40&_submit=";
        String getCookie = apiRequest.getCookieUcrm(urlCheckLogin, dataPost);
        String getToken = apiRequest.getRequestUCRM(urlGetToken, getCookie);
        return getCookie;
    }

    public String regex(String regex, String data) {
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(data);
        String group1 = "";
        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
                group1 = matcher.group(i);
            }
        }
        return group1;
    }
}
