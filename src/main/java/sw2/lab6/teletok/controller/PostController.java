package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.StorageServices;
import sw2.lab6.teletok.entity.User;
import sw2.lab6.teletok.repository.PostRepository;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.repository.PostRepository;
@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;

    @GetMapping(value = {"", "/"})
    public String listPost(Model model){
        model.addAttribute("listaposts",postRepository.obtenerListaPosts());

        return "post/list";
    }

    @GetMapping("/post/new")
    public String newPost(@ModelAttribute("post")Post post, HttpSession session){

            return "post/new";
    }

    @PostMapping("/post/save")
    public String savePost(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
                           @RequestParam("archivo") MultipartFile file, HttpSession session, Model model) throws ParseException {
        if(bindingResult.hasErrors()){
            return "post/new";
        }else {
            StorageServices storageServices = new StorageServices();
            HashMap<String, String> map = storageServices.store(file);
            if(map.get("estado").equalsIgnoreCase("exito")){
                User usuarioLog = (User) session.getAttribute("user");
                Calendar fecha = new GregorianCalendar();
                int año = fecha.get(Calendar.YEAR);
                int mes = fecha.get(Calendar.MONTH);
                int dia = fecha.get(Calendar.DAY_OF_MONTH);
                String fechaActual = año + "-" + (mes+1) + "-" + dia ;
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(fechaActual);
                post.setCreationDate(date);
                post.setMediaUrl(map.get("fileName"));
                post.setUser(usuarioLog);
                postRepository.save(post);
                return "redirect:/post/";
            }else {
                model.addAttribute("msg", map.get("msg"));
                return "post/new";
            }
        }
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
