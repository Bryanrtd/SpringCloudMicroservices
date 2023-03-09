package com.microservice.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.productservice.entity.Category;
import com.microservice.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    public List<Product> findByCategory(Category category);

}
