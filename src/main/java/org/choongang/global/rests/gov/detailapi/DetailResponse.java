package org.choongang.global.rests.gov.detailapi;

import lombok.Data;
import org.choongang.global.rests.gov.api.ApiHeader;

@Data
public class DetailResponse {
    private ApiHeader header;
    private DetailBody body;
}
