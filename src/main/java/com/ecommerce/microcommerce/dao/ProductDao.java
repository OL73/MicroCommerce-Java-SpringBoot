package com.ecommerce.microcommerce.dao;

import java.util.List;

import com.ecommerce.microcommerce.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository                                         // obligation de déclarer le Bean Product comme une entité
public interface ProductDao extends JpaRepository<Product, Integer> { 
    
    /* Plus besoin de ces méthodes, elles sont générées par JPA
    
    public List<Product> findAll();

    public Product findById(int id);

    public Product save(Product product) */
    
    Product findById(int id);

    List<Product> findByPrixGreaterThan(int prixLimit);

    List<Product> findByNomLike(String recherche);

    @Query("SELECT p.id, p.nom, p.prix FROM Product p WHERE p.prix < :prixLimit")
    List<Product> rechercherPrixInferieur(@Param("prixLimit") int prix);

}