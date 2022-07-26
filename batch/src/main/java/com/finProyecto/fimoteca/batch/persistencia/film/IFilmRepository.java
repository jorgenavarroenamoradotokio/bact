package com.finProyecto.fimoteca.batch.persistencia.film;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IFilmRepository extends CrudRepository<Film, Long> {

	
	public Set<Film> findByDateMigrateIsNull();
	
	@Query("select count(*) from Film f where f.dateMigrate is null")
	public long countPeliculasSinMigrar();

	@Modifying
	@Query(value="update films f set f.migrate = :migrate, f.date_migrate = :dateMigrate where f.id = :filmId", nativeQuery = true)
	public void checkPeliculaMigrada(@Param("filmId")long id, @Param("migrate") boolean migrate, @Param("dateMigrate") LocalDate dateMigrate);
}
