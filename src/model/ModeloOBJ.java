package model;

import clases.Personaje;
import clases.Arma;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;

public class ModeloOBJ extends Modelo {
    EntityManager entityManager;

    public ModeloOBJ(EntityManager em) {
        this.entityManager = em;
    }

    public HashMap<Integer, Personaje> leer() {
        HashMap<Integer, Personaje> mapa = new HashMap<>();
        TypedQuery<Personaje> query = entityManager.createQuery("SELECT p FROM Personaje p", Personaje.class);
        List<Personaje> results = query.getResultList();
        for (Personaje personaje : results) {
            mapa.put(personaje.getId(), personaje);
        }
        return mapa;
    }

    public HashMap<Integer, Arma> leerArma() {
        HashMap<Integer, Arma> mapaArma = new HashMap<>();
        TypedQuery<Arma> query = entityManager.createQuery("SELECT a FROM Arma a", Arma.class);
        List<Arma> results = query.getResultList();
        for (Arma arma : results) {
            mapaArma.put(arma.getId(), arma);
        }
        return mapaArma;
    }

    public Personaje buscar(int idBuscar) {
        TypedQuery<Personaje> query = entityManager.createQuery("Select p from Personajes p where id = " + idBuscar, Personaje.class);
        return query.getSingleResult();
    }

    public Arma buscarArma(int idBuscar) {
        TypedQuery<Arma> query = entityManager.createQuery("Select p from Armas p where id = " + idBuscar, Arma.class);
        return query.getSingleResult();
    }

    public void insertar(Personaje p) {
        entityManager.getTransaction().begin();
        entityManager.persist(p);
        entityManager.getTransaction().commit();
    }

    public void insertar(Arma arma) {
        entityManager.getTransaction().begin();
        entityManager.persist(arma);
        entityManager.getTransaction().commit();
    }

    public void modificar(int id, int campo, String valor) {
        Personaje personaje = entityManager.find(Personaje.class, id);
        entityManager.getTransaction().begin();
        switch (campo) {
            case 1 -> personaje.setNombre(valor);
            case 2 -> personaje.setRareza(Integer.parseInt(valor));
            case 3 -> personaje.setArma(this.buscarArma(Integer.parseInt(valor)));
            case 4 -> personaje.setElemento(valor);
        }
        entityManager.getTransaction().commit();
    }

    public void modificarArma(int id, int campo, String valor) {
        Arma arma = entityManager.find(Arma.class, id);
        entityManager.getTransaction().begin();
        switch (campo) {
            case 1 -> arma.setNombre(valor);
            case 2 -> arma.setRareza(Integer.parseInt(valor));
        }
        entityManager.getTransaction().commit();
    }

    public void eliminar(int id) {
        Personaje personaje = entityManager.find(Personaje.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(personaje);
        entityManager.getTransaction().commit();
    }

    public void eliminarArma(int id) {
        Arma arma = entityManager.find(Arma.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(arma);
        entityManager.getTransaction().commit();
    }

}
