package ru.borisov.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.borisov.model.Code;
import ru.borisov.repository.CodeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CodeService {

    private final CodeRepository codeRepository;

    @Autowired
    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public Code getCodeById(String id) {
        Code code = codeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Code snippet with id= " + id + " not found"));

        if (code.isTimeRestricted()) {
            code.updateTime();
        }

        if (code.isViewsRestricted()) {
            code.setViews(code.getViews() -1);
        }

        if (code.shouldBeDeleted()) {
            codeRepository.deleteById(id);
            return getCodeById(id);
        }

        return codeRepository.save(code);
    }

    public Code addCode(Code newCode) {
        newCode.generateId();
        newCode.setDate();

        if (newCode.getTime() > 0) {
            newCode.setTimeRestricted(true);
            newCode.setExpirationTime();
        }
        if (newCode.getViews() > 0) {
            newCode.setViewsRestricted(true);
        }

        return codeRepository.save(newCode);
    }

    public List<Code> getLatestCodeSnippets() {
        List<Code> sortedCodeList = new ArrayList<>(codeRepository.findAll());
        sortedCodeList.sort((code1, code2) ->
                code2.getCreationDateTime().compareTo(code1.getCreationDateTime()));

        return sortedCodeList.stream()
                .filter(code -> !code.isViewsRestricted())
                .filter(code -> !code.isTimeRestricted())
                .limit(10)
                .collect(Collectors.toList());
    }
}