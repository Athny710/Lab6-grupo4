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

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.dto.ListaPosts;
import sw2.lab6.teletok.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
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


@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCommentRepository postCommentRepository;


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

    @PostMapping("/post/buscar")
    public String postBuscar() {
        return "";
    }

}
