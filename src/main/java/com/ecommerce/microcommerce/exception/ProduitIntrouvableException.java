package com.ecommerce.microcommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // permet de renvoyer un code erreur de type 404 Not Found
public class ProduitIntrouvableException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 9075051038558567421L;

    public ProduitIntrouvableException(String s) {
        super(s); // on transmet le message d'erreur au parent via super()
    }
}
