package com.charityapp.services;

import com.charityapp.entities.Categorie;
import com.charityapp.repositories.CategorieRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorieService implements ICategorieService {
    
    private final CategorieRepository categorieRepository;
    
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }
    
    @Override
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }
    
    @Override
    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id).orElse(null);
    }
    
    @Override
    public Categorie saveCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }
    
    @Override
    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }
    
    @Override
    public Categorie getCategorieByNom(String nom) {
        return categorieRepository.findAll().stream()
            .filter(categorie -> categorie.getNom().equalsIgnoreCase(nom))
            .findFirst()
            .orElse(null);
    }
} 