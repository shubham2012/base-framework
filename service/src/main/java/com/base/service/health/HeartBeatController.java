package com.base.service.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heartbeat")
@Slf4j
@CrossOrigin
public class HeartBeatController {
    @GetMapping(value = "")
    public ResponseEntity checkHeartBeat() {
        return ResponseEntity.ok("Yey, I am alive");
    }
}
