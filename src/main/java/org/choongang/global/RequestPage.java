package org.choongang.global;

import lombok.Data;

@Data
public class RequestPage {
    private Integer page = 1;
    private Integer size = 10;
}
