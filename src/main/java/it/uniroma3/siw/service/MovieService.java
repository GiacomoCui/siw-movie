package it.uniroma3.siw.service;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.*;
import it.uniroma3.siw.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    MovieValidator movieValidator;
    @Autowired
    ArtistService artistService;

    @Transactional
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Transactional
    public Movie setDirectorToMovie(Long idArtist, Long idMovie) {
        Movie movie = getMovie(idMovie);
        Artist regista = artistService.getArtist(idArtist);
        movie.setRegista(regista);
        regista.getFilm_diretti().add(movie);
        movieRepository.save(movie);
        artistService.saveArtist(regista);
        return movie;
    }

    @Transactional
    public List<Movie> searchMovies(Integer anno) {
        return movieRepository.findByAnno(anno);
    }

    @Transactional
    public void saveNewMovie(MultipartFile file, Movie movie) throws IOException {
        /*Se ho un'immagine del movie*/
        if (!file.isEmpty()) {
            /*Ricavo dal file di upload il suo nome e lo setto in "urlImage" del nuovo movie e lo salvo*/
            String nomeFile = StringUtils.cleanPath(file.getOriginalFilename());
            movie.setImage(nomeFile);
            Movie movieSalvato = movieRepository.save(movie);
            /*Per avere disponibile una cartella con tutte le foto dei singoli movie*/
            String uploadDir = "./foto-movie/" + movieSalvato.getId();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(nomeFile);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Errore di upload: " + nomeFile, e);
            }
        }
        /*Altrimenti*/
        else
            movieRepository.save(movie);
    }

    @Transactional
    public Movie getMovie(Long id) {
        return movieRepository.findById(id).get();
    }

    @Transactional
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Transactional
    public Movie addActorToMovie(Long idA, Long idM) {
        Movie movie = getMovie(idM);
        Artist actor = artistService.getArtist(idA);
        movie.getAttori().add(actor);
        actor.getPartecipazione_film().add(movie);
        saveMovie(movie);
        return movie;
    }

    @Transactional
    public Movie removeActorFromMovie(Long idA, Long idM) {
        Movie movie = getMovie(idM);
        Artist actor = artistService.getArtist(idA);
        movie.getAttori().remove(actor);
        actor.getPartecipazione_film().remove(movie);
        saveMovie(movie);
        return movie;
    }

    @Transactional
    public void addNotizia(Movie movie, News news) {
        if (movie.getRecensioni().isEmpty())
            movie.getRecensioni().add(news);
        else {
            Iterator<News> iterator = movie.getRecensioni().iterator();
            while (iterator.hasNext()) {
                News notizia = iterator.next();
                if (notizia.getUser().equals(news.getUser())) {
                    iterator.remove();
                }
            }
            movie.getRecensioni().add(news);
        }
    }
}