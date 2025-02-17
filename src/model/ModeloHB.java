package model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.hibernate.Session;

import clases.Personaje;
import clases.Arma;

public class ModeloHB extends Modelo {

	Session s;

	public ModeloHB(Session s) {
		this.s = s;
	}

	public HashMap<Integer, Personaje> leer() throws SQLException {
		HashMap<Integer, Personaje> map = new HashMap<>();
		String query = "FROM Personaje";
		TypedQuery<Personaje> tq = s.createQuery(query);
		List<Personaje> results = tq.getResultList();
		for (int i = 0; i < results.size(); i++) {
			map.put(results.get(i).getId(), results.get(i));
		}
		return map;
	}
	
	public HashMap<Integer, Arma> leerArma() {
		HashMap<Integer, Arma> mapaArma = new HashMap<>();
		String query = "FROM Arma";
		TypedQuery<Arma> tq = s.createQuery(query);
		List<Arma> results = tq.getResultList();
		for (int i = 0; i < results.size(); i++) {
			mapaArma.put(results.get(i).getId(), results.get(i));
		}
		return mapaArma;
	}

	public void insertar(Personaje p) throws SQLException {
		s.beginTransaction();
		s.save(p);
		s.getTransaction().commit();
	}
	
	public void insertar(Arma w) throws SQLException {
		s.beginTransaction();
		s.save(w);
		s.getTransaction().commit();
	}

	public void modificar(Personaje personaje) {
		s.beginTransaction();
		s.update(personaje);
		s.getTransaction().commit();
	}
	
	public void modificarArma(Arma arma) {
		s.beginTransaction();
		s.update(arma);
		s.getTransaction().commit();
	}

	public void eliminar(Personaje personaje) {
		s.beginTransaction();
		s.delete(personaje);
		s.getTransaction().commit();
	}
	
	public void eliminarArma(Arma arma) {
		s.beginTransaction();
		s.delete(arma);
		s.getTransaction().commit();
	}
	
	public void escribir(HashMap<Integer, Personaje> mapa) {
		s.beginTransaction();
		for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
			s.save(entry.getValue());
		}
		s.getTransaction().commit();
		s.close();
	}
	
	public void escribirArma(HashMap<Integer, Arma> mapaArma) {
		s.beginTransaction();
		for (Map.Entry<Integer, Arma> entry : mapaArma.entrySet()) {
			s.save(entry.getValue());
		}
		s.getTransaction().commit();
		s.close();
	}


}
