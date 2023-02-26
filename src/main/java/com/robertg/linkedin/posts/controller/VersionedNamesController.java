package com.robertg.linkedin.posts.controller;

import com.robertg.linkedin.posts.dto.Name;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VersionedNamesController {

    @GetMapping("/v1/names")
    public List<String> namesV1() {
        return List.of("John", "Olivia", "Liam", "Sophia");
    }

    @GetMapping("/v2/names")
    public List<Name> namesV2() {
        return List.of(new Name("John"), new Name("Olivia"), new Name("Liam"), new Name("Sophia"));
    }
}
