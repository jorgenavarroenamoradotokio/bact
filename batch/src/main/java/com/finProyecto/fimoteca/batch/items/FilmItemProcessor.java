package com.finProyecto.fimoteca.batch.items;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.finProyecto.fimoteca.batch.persistencia.film.Film;
import com.finProyecto.fimoteca.batch.persistencia.film.IFilmRepository;

public class FilmItemProcessor implements ItemProcessor<Film, Film> {

	private final Logger logger = LoggerFactory.getLogger(FilmItemProcessor.class);

	@Autowired
	IFilmRepository repository;
	
	/**
	 * 
	 * Leemos la informacion de una pelicula, actualizamos el check de pelicula
	 * migrada y la fecha de migracion
	 * 
	 */
	
	@Override
	public Film process(Film item) throws Exception {
		LocalDate dateMigrate = LocalDate.now();
		logger.info("Indicamos que la pelicula con id " + item.getId() + " se ha migado con fecha " + dateMigrate);
		item.setMigrate(true);
		item.setDateMigrate(dateMigrate);
		
		// Actualizamos el objeto en base de datos
		repository.checkPeliculaMigrada(item.getId(), item.isMigrate(), item.getDateMigrate());
		
		return item;
	}
}