package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.PostComment;
import sw2.lab6.teletok.entity.User;
import sw2.lab6.teletok.repository.PostCommentRepository;
import sw2.lab6.teletok.repository.PostRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    PostCommentRepository postCommentRepository;


    @GetMapping(value = {"", "/"})
    public String listPost(){
        return "post/list";
    }

    @GetMapping("/post/new")
    public String newPost(){
        return "post/new";
    }

    @PostMapping("/post/save")
    public String savePost() {
        return "redirect:/";
    }

    @GetMapping("/post/file/{media_url}")
    public String getFile() {
        return "";
    }

    @GetMapping("/post/{id}")
    public String viewPost(Model model, @RequestParam("id") int id) {
        model.addAttribute("post", postRepository.findById(id).get());
        model.addAttribute("comentarios", postCommentRepository.findByPost(postRepository.findById(id).get()));
        return "post/view";
    }

    @PostMapping("/post/comment")
    public String postComment(@RequestParam("coment") String coment, Model model, HttpSession session,
                              @RequestParam("postid") int id) {

        User user = (User) session.getAttribute("user");
        if (coment.length()<3){
            model.addAttribute("msg", "Mínimo 3 caracteres");
        }else if(coment.length()>45){
            model.addAttribute("msg", "Máximo 45 caracteres");
        }else{
            PostComment pC = new PostComment();
            pC.setMessage(coment);
            pC.setPost(postRepository.findById(id).get());
            pC.setUser(user);
            postCommentRepository.save(pC);
        }

        return "";
    }

    @PostMapping("/post/like")
    public String postLike() {
        return "";
    }
}
