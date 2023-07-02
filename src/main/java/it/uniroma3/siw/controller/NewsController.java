package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.NewsRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NewsController {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private NewsService newsService;
    @Autowired
    private MovieService movieService;

    @GetMapping("/formNewNews/{idMovie}")
    public String formNewNews(@PathVariable("idMovie") Long idM, Model model){
        model.addAttribute("movie", movieService.getMovie(idM));
        model.addAttribute("news", new News());
        return "formNewNews.html";
    }

    @PostMapping("/news")
    public String newNews(@RequestParam("username")String username, @RequestParam("idMovie")Long idM, @ModelAttribute("news") News news, @RequestParam("rating") Integer voto, Model model){
        Movie movie = newsService.newNews(username, idM, news, voto);
        model.addAttribute("movie", movie);
        return "index.html";
    }
}