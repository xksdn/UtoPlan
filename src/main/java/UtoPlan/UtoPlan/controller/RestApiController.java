package UtoPlan.UtoPlan.controller;

import UtoPlan.UtoPlan.Model.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://project-elfsyyodnvfuotllaw4b.framercanvas.com"}) // CORS 설정
@RestController
@RequestMapping("/api")
public class RestApiController {

    @GetMapping("/greet")
    public String greet() {
        return "Server Connect";
    }
}