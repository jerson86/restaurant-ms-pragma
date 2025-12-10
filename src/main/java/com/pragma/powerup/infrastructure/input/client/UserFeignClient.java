package com.pragma.powerup.infrastructure.input.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-ms", url = "${service.user.url}")
public interface UserFeignClient {

    @GetMapping("/users/{userId}")
    void checkUserExists(@PathVariable("userId") Long userId);
}
