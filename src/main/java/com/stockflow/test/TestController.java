package com.stockflow.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "테스트용 API")
public class TestController {
    @GetMapping("/api/test")
    @Operation(summary = "test api", description = "테스트 용도이며 Hello world 반환")
    public String test() { return "Hello world!"; }
}
