package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    ArtistRepository artistRepository;

    @Transactional
    public Movie setDirectorToMovie(Long idArtist, Long idMovie){
        Movie movie = movieRepository.findById(idMovie).get();
        Artist regista = artistRepository.findById(idArtist).get();
        movie.setRegista(regista);
        regista.getFilm_diretti().add(movie);
        movieRepository.save(movie);
        artistRepository.save(regista);
        return movie;
    }

    @Transactional
    public void saveNewMovie(Movie movie, MultipartFile file) throws IOException {
        /*Se ho un'immagine del movie*/
        if(!file.isEmpty()) {
            /*Ricavo dal file di upload il suo nome e lo setto in "urlImage" del nuovo movie e lo salvo*/
            String nomeFile = StringUtils.cleanPath(file.getOriginalFilename());
            movie.setImage(nomeFile);
            Movie movieSalvato = movieRepository.save(movie);
            /*Per avere disponibile una cartella con tutte le foto dei singoli movie*/
            String uploadDir = "./foto-movie/" + movieSalvato.getId();
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try(InputStream inputStream = file.getInputStream()){
                Path filePath = uploadPath.resolve(nomeFile);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e) {
                throw new IOException("Errore di upload: " + nomeFile, e);
            }
        }
        /*Altrimenti*/
        else
            movieRepository.save(movie);
    }
}
