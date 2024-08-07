package org.choongang.global.rests.gov.areacodeapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.choongang.global.rests.gov.api.ApiHeader;

@Data
public class CodeResponse {
    private ApiHeader header;
    @JsonAlias("body")
    private CodeBody codeBody;
}
