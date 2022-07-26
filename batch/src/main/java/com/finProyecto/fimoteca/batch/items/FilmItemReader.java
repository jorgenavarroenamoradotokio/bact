package com.finProyecto.fimoteca.batch.items;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import com.finProyecto.fimoteca.batch.persistencia.film.Film;
import com.finProyecto.fimoteca.batch.persistencia.film.IFilmRepository;

public class FilmItemReader implements ItemReader<Film>{
	
	private final Logger logger = LoggerFactory.getLogger(FilmItemReader.class);
	
	@Autowired
	IFilmRepository repository;
	private Iterator<Film> itFilm;
	
	/**
	 * 
	 * Antes de iniciar el step obtenemos de base de datos todas las peliculas que
	 * tienen fecha de migracion a null
	 * 
	 * @param stepExceExecution
	 */
	
	@BeforeStep
	public void before(StepExecution stepExceExecution) {
		logger.info("Leemos de base de datos todas las peliculas pendientes de migrar");
		itFilm = repository.findByDateMigrateIsNull().iterator();
	}
	
	/**
	 * 
	 * Leemos individualmente las peliculas y retornamos el objeto
	 * 
	 */
	
	@Override
	public Film read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Film film = null;
		if (itFilm != null && itFilm.hasNext()) {
			film = itFilm.next();
		}
		return film;
	}
}
