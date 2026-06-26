package com.ciberfarma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ciberfarma.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByCuentaAndClave(String cuenta, String clave);

	@Query("""
			select u
			from Usuario as u
			where
				(u.tipo.idTipo = :idTipo or :idTipo is null)
			""")
	List<Usuario> findAllByFilters(@Param("idTipo") Integer idTipo);
	
	boolean existsByCuenta(String cuenta);
	
	boolean existsByCuentaAndIdUsuarioNot(String cuenta, Integer idUsuario);
}
