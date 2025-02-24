package bookhive.bookhiveserver.domain.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    @GetMapping("")
    public String showTags() {
        return "showTags";
    }

    @PostMapping("")
    public String createTag() {
        return "createTag";
    }

    @PutMapping("/{tagId}")
    public String updateTag() {
        return "updateTag";
    }

    @DeleteMapping("/{tagId}")
    public String deleteTag() {
        return "deleteTag";
    }

}
