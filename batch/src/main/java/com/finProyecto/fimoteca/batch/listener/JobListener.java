package com.finProyecto.fimoteca.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finProyecto.fimoteca.batch.persistencia.film.IFilmRepository;

@Component
public class JobListener extends JobExecutionListenerSupport {

	private final Logger logger = LoggerFactory.getLogger(JobListener.class);

	@Autowired
	IFilmRepository repository;
	
	/**
	 * 
	 * Obtenemos el numero de peliculas que estan pendientes de ser migradas
	 * al inicio del job
	 * 
	 */
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.STARTED) {
			logger.info("Iniciamos el Job");
			long filmMigrate = repository.countPeliculasSinMigrar();
			logger.info("El numero de peliculas a migrar es de: " + filmMigrate);
		}
	}
	
	/**
	 * 
	 * Obtenemos el numero de peliculas que estan pendientes de migrar al finalizar
	 * el job
	 * 
	 */
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("Job Finalizado con exito");
			long filmMigrate = repository.countPeliculasSinMigrar();
			logger.info("El numeo de peliculas pendientes de migrar es: " + filmMigrate);	
		}
	}
}
