package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.member.constants.Authority;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.AuthoritiesRepository;
import org.choongang.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadDoneService uploadDoneService;
    /**
     * 회원 가입 처리
     *
     * @param form
     */
    public void save(RequestJoin form) {
        Member member = new ModelMapper().map(form, Member.class);
        String hash = passwordEncoder.encode(member.getPassword());
        member.setPassword(hash);
        save(member, List.of(Authority.USER));
    }

    public void save(Member member, List<Authority> authorities) {
        //휴대폰 번호 숫자만 기록
        String mobile = member.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
            member.setMobile(mobile);
        }

        memberRepository.saveAndFlush(member);
        if (authorities != null) {
            List<Authorities> items = authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();

            items = authorities.stream().map(authority -> Authorities.builder()
                    .member(member)
                    .authority(authority)
                    .build()).toList();
            authoritiesRepository.saveAllAndFlush(items);
        }
        //파일 업로드 완료 처리
        uploadDoneService.process(member.getGid());
    }
}
