package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.dto.ListaPosts;
import sw2.lab6.teletok.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping(value = {"", "/"})
    public String listPost(Model model){
        List<ListaPosts> listaPosts = postRepository.obtenerListaPosts();
        List<String> mensajes = new ArrayList<String>();

        for (ListaPosts lp:
             listaPosts) {

            if (lp.getHora() >= 1){
                mensajes.add("Publicado hace " + lp.getHora() + " horas");
            }else if (lp.getHora() < 1 && lp.getMinuto() >= 1){
                mensajes.add("Publicado hace " + lp.getMinuto() + " minutos");
            }else {
                mensajes.add("Publicado hace " + lp.getSegundo() + " segundos");
            }
        }
        model.addAttribute("listaposts",postRepository.obtenerListaPosts());
        model.addAttribute("mesj",mensajes);
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
    public String viewPost() {
        return "post/view";
    }

    @PostMapping("/post/comment")
    public String postComment() {
        return "";
    }

    @PostMapping("/post/like")
    public String postLike() {
        return "";
    }
}
