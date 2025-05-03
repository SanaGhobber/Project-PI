package com.esprit.services;

import com.esprit.models.Personne;

import java.util.List;

public interface IService<T> {
    void ajouter(T t);
    void modifier(T t);
    void supprimer(T t);
    T findById(int id);
    List<T> recuperer();
}
