package com.pixyapp.codescribe.Controller;

import com.pixyapp.codescribe.request.CreateJavaGeneratorRequest;
import com.pixyapp.codescribe.service.JavaGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api-Generate")
public class JavaGeneratorController {

    @Autowired
    private JavaGeneratorService javaGeneratorService;

    @PostMapping("/javaGenerate")
    public ResponseEntity<?> codeScribe(@RequestBody CreateJavaGeneratorRequest request) {

        try {
            return javaGeneratorService.javaGenerator(request.getPackageName(), request.getClassName(), request.getProperties());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Please check the input", HttpStatus.BAD_REQUEST);
        }

    }

}
