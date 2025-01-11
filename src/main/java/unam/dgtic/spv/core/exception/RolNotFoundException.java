package unam.dgtic.spv.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RolNotFoundException extends Exception {
    public RolNotFoundException(Integer id) {
        super("User Info Role with id " + id + " is NOT found");
    }
}
