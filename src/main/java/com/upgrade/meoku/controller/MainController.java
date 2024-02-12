package com.upgrade.meoku.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Main화면 컨트롤러", description = "메인화면 기능")
@Controller
public class MainController {

    @Operation(summary = "서버동작 확인", description = "Just Print Hello World")
    @GetMapping(value = "helloworld")
    public String HelloWorld() {
        return "HelloWorld";
    }

}
