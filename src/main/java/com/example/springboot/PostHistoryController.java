package com.example.springboot;

import com.example.utils.PostLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.*;

@Controller
public class PostHistoryController {

    @Autowired
    private Environment env;

    @RequestMapping(value = "/")
    public String index() {
        return "test";
    }

    @RequestMapping(value = "/api")
    public String postString(@RequestParam("post_input_text") String inputTest,
                             Model model) throws IOException {
        model.addAttribute("title","Test Gen Page");
        System.out.println(inputTest);
        try (PrintWriter out = new PrintWriter("test.sol")) {
            out.println(inputTest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ProcessBuilder builder = new ProcessBuilder();
        try {
            Process p = Runtime.getRuntime().exec("python3 /Users/ilyazlatkin/CLionProjects/blockchain_exp/hello_foundry/scripts/RunAll.py " +
                    "-i test.sol -o /Users/ilyazlatkin/IdeaProjects/springboot_tg/build/resources/main/templates/regression_1");

            System.out.println("Waiting the script");
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("script done");
        return "regression_1/1_html_report";
    }


}

