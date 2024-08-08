package org.choongang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choongang.board.entities.Board;
import org.choongang.board.repositories.BoardRepository;
import org.choongang.global.exceptions.ExceptionProcessor;
import org.choongang.member.MemberInfo;
import org.choongang.member.MemberUtil;
import org.choongang.member.services.MemberSaveService;
import org.choongang.member.validators.JoinValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@SessionAttributes("requestLogin")
public class MemberController implements ExceptionProcessor {

    private final JoinValidator joinValidator;
    private final MemberSaveService memberSaveService;
    private final MemberUtil memberUtil;
    private final BoardRepository boardRepository;

    @ModelAttribute
    public RequestLogin requestLogin() {
        return new RequestLogin();
    }

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);


        return "front/member/join";
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model) {
        commonProcess("join", model);
        joinValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return "front/member/join";
        }
        memberSaveService.save(form);
        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(@Valid @ModelAttribute RequestLogin form, Errors errors, Model model) {
        commonProcess("login", model);
        String code = form.getCode();
        if (StringUtils.hasText(code)) {
            errors.reject(code, form.getDefaultMessage());
            //비번이 만료인 경우 비번 재설정 페이지 이동
            if (code.equals("CredentialsExpired.Login")) {
                return "redirect:/member/password/reset ";
            }
        }
        return "front/member/login";
    }

    /**
     * 회원 관련 컨트롤러 공통 처리
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        mode = Objects.requireNonNullElse(mode, "join");

        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        addCss.add("member/style");//회원 공통 스타일
        if (mode.equals("join")) {
            addCommonScript.add("fileManager");
            addCss.add("member/join");
            addScript.add("member/join");
        } else if (mode.equals("login")) {
            addCss.add("member/login");
        }
        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }

    @ResponseBody
    @GetMapping("/test")
    public void test(Principal principal) {
        log.info("로그인 아이디 : {}", principal.getName());
        log.info("{}", principal);
    }

    @ResponseBody
    @GetMapping("/test2")
    public void test2(@AuthenticationPrincipal MemberInfo memberInfo) {
        log.info("로그인 회원 : {}", memberInfo);
    }

    @ResponseBody
    @GetMapping("/test3")
    public void test3() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("로그인 상태 : {}", authentication.isAuthenticated());
        log.info("{}", authentication.getPrincipal());
/*        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo) {//로그인 상태 - USERDETAILS 구현체
            MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();
            log.info("로그인 회원 : {}", memberInfo.toString());
        } else { // 미로그인 상태 - String / anonymousUser (getPrincipal())
            log.info("getPrincipal() : {}", authentication.getName());
        }*/
    }

    @ResponseBody
    @GetMapping("/test4")
    public void test4() {
        log.info("로그인 여부 : {}", memberUtil.isLogin());
        log.info("로그인 회원z : {}", memberUtil.getMember());
    }

    @ResponseBody
    @GetMapping("/test5")
    public void test5() {
        /*Board board = Board.builder()
                .bId("freeTalk2")
                .bName("자유게시판")
                .build();
        boardRepository.saveAndFlush(board);*/
        Board board = boardRepository.findById("freeTalk2").orElse(null);

        board.setBName("(수정)자유게시판");
        boardRepository.saveAndFlush(board);
    }

    @GetMapping("/test6")
    @ResponseBody
//    @PreAuthorize("isAuthenticated()")//회원만 접근가능한 주소
    public void test6() {
        log.info("test6 - 회원만 접근 가능");
    }

    @ResponseBody
    @GetMapping("/test7")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void test7() {

        log.info("test2 - 관리자만 접근 가능");
    }

}
