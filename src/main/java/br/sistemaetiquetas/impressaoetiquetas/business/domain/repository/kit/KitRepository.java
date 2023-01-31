package br.sistemaetiquetas.impressaoetiquetas.business.domain.repository.kit;

import br.sistemaetiquetas.impressaoetiquetas.business.domain.entity.kit.Kit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KitRepository extends JpaRepository<Kit,Integer> {

    @Query(value = "SELECT  CASE WHEN max(id) IS NULL THEN 1 ELSE  max(id)+1 END AS last_id FROM kit", nativeQuery = true)
    Integer getNextValMySequence();

    List<Kit> findAllByOrderByDescricao();
}