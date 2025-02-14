package model;

import clases.Personaje;
import clases.Arma;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;

public class ModeloOBJ extends Modelo {

    EntityManager em;

    public ModeloOBJ(EntityManager em) {
        this.em = em;
    }

    public HashMap<Integer, Personaje> leer() {
        HashMap<Integer, Personaje> mapa = new HashMap<>();
        TypedQuery<Personaje> query = em.createQuery("SELECT p FROM Personaje p", Personaje.class);
        List<Personaje> results = query.getResultList();
        for (Personaje personaje : results) {
            mapa.put(personaje.getId(), personaje);
        }
        return mapa;
    }

    public HashMap<Integer, Arma> leerArma() {
        HashMap<Integer, Arma> mapaArma = new HashMap<>();
        TypedQuery<Arma> query = em.createQuery("SELECT a FROM Arma a", Arma.class);
        List<Arma> results = query.getResultList();
        for (Arma arma : results) {
            mapaArma.put(arma.getId(), arma);
        }
        return mapaArma;
    }

    public Personaje buscar(int idBuscar) {
        TypedQuery<Personaje> query = em.createQuery("Select p from Personajes p where id = " + idBuscar, Personaje.class);
        return query.getSingleResult();
    }

    public Arma buscarArma(int idBuscar) {
        TypedQuery<Arma> query = em.createQuery("Select p from Armas p where id = " + idBuscar, Arma.class);
        return query.getSingleResult();
    }

    public void insertar(Personaje p) {
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }

    public void insertar(Arma arma) {
        em.getTransaction().begin();
        em.persist(arma);
        em.getTransaction().commit();
    }

    public void modificar(int id, int campo, String valor) {
        Personaje p = em.find(Personaje.class, id);
        em.getTransaction().begin();
        switch (campo) {
            case 1:
                p.setNombre(valor);
                break;
            case 2:
                p.setRareza(Integer.parseInt(valor));
                break;
            case 3:
                p.setArma(this.buscarArma(Integer.parseInt(valor)));
                break;
            case 4:
                p.setElemento(valor);
                break;
            default:
                break;
        }
        em.getTransaction().commit();
    }

    public void modificarArma(int id, int campo, String valor) {
        Arma a = em.find(Arma.class, id);
        em.getTransaction().begin();
        switch (campo) {
            case 1:
                a.setNombre(valor);
                break;
            case 2:
                a.setRareza(Integer.parseInt(valor));
                break;
            default:
                break;
        }
        em.getTransaction().commit();
    }

    public void eliminar(int id) {
        Personaje p = em.find(Personaje.class, id);
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public void eliminarArma(int id) {
        Arma a = em.find(Arma.class, id);
        em.getTransaction().begin();
        em.remove(a);
        em.getTransaction().commit();
    }


}
