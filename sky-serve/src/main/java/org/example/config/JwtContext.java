package org.example.config;

import org.springframework.stereotype.Component;

@Component
public class JwtContext<T> {
    ThreadLocal<T> threadLocal = new ThreadLocal<>();

    public void setJwtContext(T value) {
        threadLocal.set(value);
    }

   public T getJwtContext() {
        return threadLocal.get();
    }
   public void clearJwtContext() {
        threadLocal.remove();
    }

}
