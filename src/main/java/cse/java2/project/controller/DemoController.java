package cse.java2.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DemoController {

    /**
     * This method is called when the user requests the root URL ("/") or "/demo".
     * In this demo, you can visit localhost:9090 or localhost:9090/demo to see the result.
     * @return the name of the view to be rendered
     * You can find the static HTML file in src/main/resources/templates/home.html
     */
    @GetMapping({"/","/home"})
    public String demo() {
        return "home";
    }
    @GetMapping({ "/answers"})
    public String answers() {
        return "answers";
    }
    @GetMapping({ "/tags"})
    public String tags() {
        return "tags";
    }
    @GetMapping({ "/users"})
    public String users() {
        return "users";
    }
}
