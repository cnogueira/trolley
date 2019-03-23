package org.cnogueira.trolley.api.v1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CartNotFoundException extends RuntimeException {
}
