package ru.borisov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.borisov.model.Code;
import ru.borisov.service.CodeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/code")
public class CodeRestController {

    private final CodeService codeService;

    @Autowired
    public CodeRestController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/{id}")
    public Code getCode(@PathVariable String id) {
        return codeService.getCodeById(id);
    }

    @PostMapping("/new")
    public Map<String, String> addCode(@Valid @RequestBody Code newCode) {
        Code addedCode = codeService.addCode(newCode);
        return Map.of("id", addedCode.getId());
    }

    @GetMapping("/latest")
    public List<Code> getLatestCodeSnippets() {
        return codeService.getLatestCodeSnippets();
    }
}
