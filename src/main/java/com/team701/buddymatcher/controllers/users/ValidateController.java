package com.team701.buddymatcher.controllers.users;

import com.team701.buddymatcher.config.JwtTokenUtil;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "Validation")
@RequestMapping("/api/validate")
public class ValidateController {

    private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @Operation(summary = "Get method to check that a JWT token is valid")
    @GetMapping(path = "")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void validateToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            token = token.replace("Bearer ","");
            jwtTokenUtil.validateToken(token);
        } catch (SignatureException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException | MalformedJwtException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
