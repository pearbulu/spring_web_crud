package com.ciberfarma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ciberfarma.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
	
	List<Producto> findAllByOrderByIdProductoDesc();
	
	List<Producto> findAllByActivoTrue();
	
	@Query("""
			select p
			from Producto as p
			where 
				(p.categoria.idCategoria = :idCategoria or :idCategoria is null)
				and
				(p.proveedor.idProveedor = :idProveedor or :idProveedor is null)
			""")
	List<Producto> findAllByFilters(
		@Param("idCategoria") Integer idCategoria,
		@Param("idProveedor") Integer idProveedor
	);
	
	boolean existsByDescripcion(String descripcion);
	
	boolean existsByDescripcionAndIdProductoNot(String descripcion, Integer idProducto);
}
