package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;


public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {
	@Query("from Satellite s where not s.stato='DISATTIVATO' and s.dataLancio <:dataOggi")
	List<Satellite> cercaSatellitiDueAnni(Date dataOggi);
	
	@Query("from Satellite s where s.stato='DISATTIVATO' and s.dataRientro=null")
	List<Satellite> cercaSatellitiOffDataRientroNull();
	
	@Query("from Satellite s where s.stato='FISSO' and s.dataLancio<:dataOggi")
	List<Satellite> cercaSatellitiDieciAnniOrbita(Date dataOggi);
	
	@Query("from Satellite s where (s.dataRientro>curdate() or s.dataRientro=null) and not s.stato='DISATTIVATO'")
	List<Satellite> cercaSatellitiNonRientrati();

}
