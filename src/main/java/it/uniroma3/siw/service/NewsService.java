package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    MovieService movieService;
    @Autowired
    CredentialsService credentialsService;

    @Transactional
    public Movie newNews(String username, Long idM, News news, Integer voto, String descrizione){
        Movie movie = movieService.getMovie(idM);
        Credentials user = credentialsService.getCredentials(username);
        news.setVoto(voto);
        news.setDescrizione(descrizione);
        news.setUser(user);
        saveNews(news);
        credentialsService.addNews(user, news);
        movieService.addNotizia(movie, news);
        movieService.saveMovie(movie);
        return movie;
    }

    @Transactional
    public void saveNews(News news) {
        newsRepository.save(news);
    }
}