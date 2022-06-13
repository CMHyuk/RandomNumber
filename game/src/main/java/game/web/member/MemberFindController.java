package game.web.member;

import game.domain.login.member.Member;
import game.domain.login.member.MemberRepository;
import game.web.member.find.FindLoginPassword;
import game.web.member.find.FindPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberFindController {

    private final MemberRepository memberRepository;
    private final FindLoginPassword findLoginPassword;

    @GetMapping("/findPassword")
    public String findForm(@ModelAttribute FindPassword findPassword) {
        return "members/findForm";
    }

    @PostMapping("/findPassword")
    public String find(@Validated @ModelAttribute FindPassword findPassword, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "members/findForm";
        }

        Optional<Member> loginId = memberRepository.findByLoginId(findPassword.getInputId());

        log.info("loginId={}", loginId);

        if (loginId.isEmpty()) {
            bindingResult.reject("findFail", "아이디가 존재하지 않습니다.");
            return "members/findForm";
        }

        log.info("findLoginPassword={}", findLoginPassword.findByPassword(findPassword.getInputId()));
        model.addAttribute("password", findLoginPassword.findByPassword(findPassword.getInputId()));

        return "members/findPassword";
    }

}
