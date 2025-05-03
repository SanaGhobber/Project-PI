package com.esprit.services;
import jakarta.persistence.EntityManager;
import java.util.List;

public  abstract  class AbstractService<T> implements IService<T>{

    protected EntityManager em;

    public AbstractService(EntityManager em) {
        this.em = em;
    }

    protected abstract Class<T> getEntityClass();

    @Override
    public void ajouter(T t) {
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();
    }

    @Override
    public void modifier(T t) {
        em.getTransaction().begin();
        em.merge(t);
        em.getTransaction().commit();
    }

    @Override
    public void supprimer(T t) {
        em.getTransaction().begin();
        em.remove(em.contains(t) ? t : em.merge(t));
        em.getTransaction().commit();
    }

    @Override
    public List<T> recuperer() {
        return em.createQuery("FROM " + getEntityClass().getSimpleName(), getEntityClass()).getResultList();
    }
    @Override
    public T findById(int id) {
        return em.find(getEntityClass(), id);
    }


}
