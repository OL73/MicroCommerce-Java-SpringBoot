package com.ecommerce.microcommerce.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exception.ProduitIntrouvableException;
import com.ecommerce.microcommerce.model.Product;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Gestion des produits") // annotation swagger pour enrichir la documentation (besoin de créer une classe SwaggerConfig avec @Configuration + méthode api())
@RestController
public class ProductController {
    
    @Autowired
    private ProductDao productDao;

    // produits/{id}
    @ApiOperation(value = "récupère un produit selon son id")
    @GetMapping(value = "produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {

        /* Product product = new Product(id, new String("aspirateur"), 100);

        return product; */

        Product product = productDao.findById(id);

        if (product == null) {

            throw new ProduitIntrouvableException("Ce produit n'existe pas");

        }
        return product;
    }

    // liste des produits
    // version sans les Filtres
    /* @GetMapping(value = "produits")
    public List<Product> listProduits() {
        return productDao.findAll();
    } */

    // version avec les filtres
    @RequestMapping(value = "/produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {

        List<Product> produits = productDao.findAll();

        // on établit les régles de filtrage sur un Bean
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

        // on indique à Jackson à quel bean on applique le filtrage parmi toutes les beans qui sont annotées avec monFiltreDynamique
        FilterProvider listeDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

        // on applique les règles aux méthodes qui nous intéressent comme setFilter()
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);

        produitsFiltres.setFilters(listeDeNosFiltres);

        // on retourne la liste filtrées
        return produitsFiltres;
    }

    
    @PostMapping(value = "/produits/new")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product unProduit) { // @RequestBody permet d'aller chercher dans le body de la requette un Json et de le parser pour constituer un objet de type Product

        Product produitAjoute = productDao.save(unProduit);

        if (unProduit == null) {
            return ResponseEntity.noContent().build();
        }

        // ResponseEntity<Void> permet de renvoyer le statut de la requete dans le navigateur

        // par convention, il est nécessaire d'indiquer un code 201 created dans l'en-tête de la requete
        // si l'ajout n'est pas null, on récupère l'URI de ce produit
        // permet de créer un lien à partir d'une requête
        URI location =  ServletUriComponentsBuilder
            .fromCurrentRequest() // à partir de la requête courante
            .path("/{id}") // ajout d'un id, qui est celui du produit ajouté
            .buildAndExpand(produitAjoute.getId())
            .toUri();

        return ResponseEntity.created(location).build(); // construction de cette réponse
    }


    // utilisation des requêtes personnalisée HSQL (ex ici pour récupérer les produits dont les prix sont supérieurs à un prix défini)
    @GetMapping(value = "/produits/highPrice/{prixLimit}")
    public List<Product> findByPrixGreaterThan(@PathVariable int prixLimit) {

        return productDao.findByPrixGreaterThan(prixLimit);
    }

    @GetMapping(value = "/produits/search/{recherche}")
    public List<Product> testeDeRequetes(@PathVariable String recherche) {
        return productDao.findByNomLike("%"+recherche+"%");
    }

    // utilisation d'une requête JPSQL Query personnalisée : SELECT id, nom, prix FROM Product p WHERE p.prix > ?1
    @GetMapping(value = "test/produits/{prixLimit}")
    public List<Product> testPrixInferieur(@PathVariable int prixLimit) {

        return productDao.rechercherPrixInferieur((prixLimit));
    }

    // delete
    @DeleteMapping(value = "/produits/{id}")
    public void supprimerProduit(@PathVariable int id) {

        productDao.deleteById(id);
    }

    // put / mettre à jour un produit
    @PutMapping(value = "/produits/update")
    public void mettreAJourProduit(@RequestBody Product produit) {

        productDao.save(produit);
    }
}