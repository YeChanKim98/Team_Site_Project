package mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
class Controller{

    private final EmailService emailService;

    @PostMapping("/mail")
    public String home3(@RequestParam String address, @RequestParam String title, @RequestParam String content, HttpServletRequest req){
        emailService.sendSimpleMessage(address, title, content);
        return "home";
    }
}