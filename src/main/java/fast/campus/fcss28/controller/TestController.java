package fast.campus.fcss28.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/v1/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/kakao-login")
    public String kakaologin(@RequestParam String code) {
        return "hello kakao, code=" + code;
    }
}
