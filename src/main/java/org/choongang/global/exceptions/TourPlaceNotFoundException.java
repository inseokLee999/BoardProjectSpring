package org.choongang.global.exceptions;

import org.choongang.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class TourPlaceNotFoundException extends AlertBackException {
    public TourPlaceNotFoundException() {
        super("NotFound.TourPlace", HttpStatus.NOT_FOUND);
    }
}
