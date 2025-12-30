package com.odc.aws_learning.auth.base.response;

import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Data
public class CResponse<T> implements Serializable {
    private T data;
    private boolean ok;
    private String message;

    private CResponse(T data, String message, boolean ok) {
        this.data = data;
        this.message = message;
        this.ok = ok;
    }

    private CResponse(String message, boolean ok) {
        this.message = message;
        this.ok = ok;
    }

    public static <E> CResponse<E> success(E e, String message) {
        return new CResponse(e, message, true);
    }

    public static <E> CResponse<E> success(E e) {
        return CResponse.success(e, null);
    }

    public static <E> CResponse<E> validate(String message) {
        return new CResponse(message, true);
    }

    public static <E> CResponse<E> error(String message) {
        return new CResponse(null, message, false);
    }

    public ResponseEntity<CResponse<T>> wrap(Function<CResponse<T>, ResponseEntity<CResponse<T>>> successFunc, Function<CResponse<T>, ResponseEntity<CResponse<T>>> errorFunc) {
        return isOk() ? nonNull(successFunc) ? successFunc.apply(this) : ok(this) : nonNull(errorFunc) ? errorFunc.apply(this) : badRequest().body(this);
    }

    public ResponseEntity<CResponse<T>> wrap(Function<CResponse<T>, ResponseEntity<CResponse<T>>> successFunc) {
        return wrap(successFunc, null);
    }

    public ResponseEntity<CResponse<T>> wrap() {
        return wrap(null);
    }

    /**
     * Vérifie si la réponse est un succès.
     * Cette méthode est générée automatiquement par Lombok (@Data) pour le champ boolean 'ok',
     * mais nous l'ajoutons explicitement pour garantir sa disponibilité au moment de la compilation.
     * @return true si la réponse est un succès, false sinon
     */
    public boolean isOk() {
        return ok;
    }

    public boolean isKo() {
        return !isOk();
    }
}
