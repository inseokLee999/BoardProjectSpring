package org.choongang.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.global.exceptions.BadRequestException;
import org.choongang.tour.constants.ContentType;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Utils { // 빈의 이름 - utils

    private final MessageSource messageSource;
    private final HttpServletRequest request;

    public String toUpper(String str) {
        return str.toUpperCase();
    }

    public Map<String, List<String>> getErrorMessages(Errors errors) {//JSON 받을 때는 에러를 직접 가공
        // FieldErrors


        Map<String, List<String>> messages = errors.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, e -> getCodeMessages(e.getCodes())));

        // GlobalErrors
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(e -> getCodeMessages(e.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages);
        }
        return messages;
    }


    public List<String> getCodeMessages(String[] codes) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);

        List<String> messages = Arrays.stream(codes)
                .map(c -> {
                    try {
                        return ms.getMessage(c, null, request.getLocale());
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(s -> !s.isBlank())
                .toList();

        ms.setUseCodeAsDefaultMessage(true);
        return messages;
    }

    public String getMessage(String code) {
        List<String> messages = getCodeMessages(new String[]{code});

        return messages.isEmpty() ? code : messages.get(0);
    }

    /**
     * 접속 장비가 모바일인지 체크
     *
     * @return
     */
    public boolean isMobile() {
        //모바일 수동 전환 체크, 처리
        HttpSession session = request.getSession();
        String device = (String) session.getAttribute("device");
        if (StringUtils.hasText(device)) {
            return device.equals("MOBILE");
        }
        //User-Agent 요청 헤더 정보
        String ua = request.getHeader("User-Agent");

        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";

        return ua.matches(pattern);
    }

    /**
     * 모바일, PC 뷰 템플릿 경로 생성
     *
     * @param path
     * @return
     */
    public String tpl(String path) {
        String prefix = isMobile() ? "mobile/" : "front/";
        return prefix + path;
    }

    public String nl2br(String str) {
        return str.replaceAll("\\n", "<br>").replaceAll("\\r", "");
    }

    public ContentType typeCode(String type) {
        switch (type) {
            case("spot"):
                return ContentType.TourSpot;
            case("culture"):
                return ContentType.CultureFacility;
            case ("festival"):
                return ContentType.Festival;
            case("course"):
                return ContentType.TourCourse;
            case ("leports"):
                return ContentType.Leports;
            case ("stay"):
                return ContentType.Accommodation;
            case ("shopping"):
                return ContentType.Shopping;
            case("restaurant"):
                return ContentType.Restaurant;
        }
        throw new BadRequestException("Wrong contentType");
    }
}