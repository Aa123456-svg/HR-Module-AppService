package ir.appservice.controller;

import ir.appservice.model.entity.application.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController extends BaseController {

    @GetMapping("/index")
    public Account index() {
        Account account = new Account();
        account.setDisplayName("Bahman Heydari Nejad");
        account.setEmail("bahmanheydarinejad@gmail.com");
        return account;
    }
}
