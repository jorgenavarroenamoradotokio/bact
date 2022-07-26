package com.finProyecto.fimoteca.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.finProyecto.fimoteca.batch.items.FilmItemProcessor;
import com.finProyecto.fimoteca.batch.items.FilmItemReader;
import com.finProyecto.fimoteca.batch.listener.JobListener;
import com.finProyecto.fimoteca.batch.persistencia.film.Film;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public FilmItemReader reader(){
		return new FilmItemReader();
	}
	
	@Bean
	public FilmItemProcessor processor() {
		return new FilmItemProcessor();
	}
	
	/**
	 * 
	 * Metodo encargado de escribir la informacion de las peliculas en un fichero csv
	 * agregamos el caracter que se encargara de separar los diferentes campos (en nuestro caso es un ;)
	 * agregamos los campos que van a representarse en el documento
	 * 
	 * @return
	 */
	
	@Bean
	public FlatFileItemWriter<Film> writer() {
		FlatFileItemWriter<Film> fileItemWriter = new FlatFileItemWriterBuilder<Film>()
				.name("FilmItemWriter")
				.resource(new FileSystemResource("Film.csv"))
				.delimited().delimiter(";")
				.names(new String[] { "id", "title", "year", "duration", "sypnosis", "poster" })
				.build();

		return fileItemWriter;
	}
	
	/**
	 * 
	 * Definimos el job con los pasos a realizar
	 * 
	 * @param step
	 * @param filmJobListener
	 * @return
	 */
	
	@Bean
	public Job job(Step step, JobListener filmJobListener) {
		Job job = jobBuilderFactory.get("Migracion de peliculas")
				.listener(filmJobListener)
				.flow(step)
				.end()
				.build();
		return job;
	}
	
	/**
	 * 
	 * Definimos el step, tratamos la informacion de 100 en 100
	 * agregamos los listener de reader, processor y writer
	 * 
	 * @return
	 */
	
	@Bean
	public Step step() {
		TaskletStep step = stepBuilderFactory.get("step1")
				.<Film, Film>chunk(100)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
		return step;
	}
}