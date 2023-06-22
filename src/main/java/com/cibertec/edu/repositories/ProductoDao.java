package com.cibertec.edu.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cibertec.edu.models.Producto;

public interface ProductoDao extends CrudRepository<Producto, Long> {

	public List<Producto> findAll();
}
