package com.charityapp.services;

import com.charityapp.entities.Categorie;
import com.charityapp.repositories.CategorieRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorieService {
    
    private final CategorieRepository categorieRepository;
    
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }
    
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }
    
    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id).orElse(null);
    }
    
    public Categorie saveCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }
    
    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }
} 