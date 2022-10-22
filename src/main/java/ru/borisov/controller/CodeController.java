package ru.borisov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import ru.borisov.model.Code;
import ru.borisov.service.CodeService;


@Controller
@RequestMapping(value = "/code")
public class CodeController {

    private final CodeService codeService;

    @Autowired
    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/{id}")
    public ModelAndView getCodeView(ModelAndView modelAndView,
                                    @PathVariable String id) {

        Code code;
        try {
            code = codeService.getCodeById(id);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
            modelAndView.setViewName("code_not_found");
            modelAndView.addObject("id", id);
            return modelAndView;
        }

        modelAndView.addObject("code", code);
        modelAndView.setViewName("code");

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView getWriteCodeView(ModelAndView modelAndView) {
        modelAndView.setViewName("code_new");

        return modelAndView;
    }

    @GetMapping("/latest")
    public ModelAndView getLatestCodeSnippets(ModelAndView modelAndView) {
        modelAndView.addObject("codeList", codeService.getLatestCodeSnippets());
        modelAndView.setViewName("code_latest");

        return modelAndView;
    }
}
