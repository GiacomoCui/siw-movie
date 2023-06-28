package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.NewsRepository;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NewsController {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/formNewNews/{idMovie}")
    public String formNewNews(@PathVariable("idMovie") Long idM, Model model){
        model.addAttribute("movie", movieRepository.findById(idM).get());
        model.addAttribute("news", new News());
        return "formNewNews.html";
    }

    @PostMapping("/news")
    public String newNews(@RequestParam("username")String username, @RequestParam("idMovie")Long idM, @ModelAttribute("news") News news, @RequestParam("rate") Integer voto, Model model){
        Movie movie = movieRepository.findById(idM).get();
        Credentials user = credentialsService.getCredentials(username);
        news.setVoto(voto);
        news.setUser(user);

        newsRepository.save(news);
        user.getRecensioni().add(news);
        credentialsService.saveCredentials(user);

        movie.getNotizie().add(news);
        movieRepository.save(movie);
        model.addAttribute("movie", movie);
        return "movie.html";
    }
}
