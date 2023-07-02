package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ArtistService {
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    MovieService movieService;

    @Transactional
    public void saveArtist(Artist artist){
        artistRepository.save(artist);
    }
    @Transactional
    public void saveNewArtist(Artist artist, String dataN){
        artist.setDataNascita(dataN);
        artistRepository.save(artist);
    }

    @Transactional
    public Artist getArtist(Long id){
        return artistRepository.findById(id).get();
    }

    @Transactional
    public List<Artist> getArtists(){
        return (List<Artist>) artistRepository.findAll();
    }

    @Transactional
    public List<Artist> notMovieActors(Movie movie){
        List<Artist> notMovieActors = (List<Artist>) artistRepository.findAll();
        notMovieActors.removeAll(movie.getAttori());
        return notMovieActors;
    }
}