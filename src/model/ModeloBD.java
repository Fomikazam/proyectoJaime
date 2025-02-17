package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import clases.Personaje;
import clases.Arma;

public class ModeloBD extends Modelo {

	Connection conn;

	public ModeloBD(Connection conn) {
		this.conn = conn;
	}

	public HashMap<Integer, Personaje> leer() throws SQLException {
		HashMap<Integer, Personaje> map = new HashMap<>();
		String query = "SELECT * FROM personajes";
		Statement st = conn.createStatement();
		Statement stArma = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			//leemos personaje
			int id = rs.getInt("id");
			String nombre = rs.getString("nombre");
			int rareza = rs.getInt("rareza");
			int idArma = rs.getInt("idArma");
			String elemento = rs.getString("elemento");
			
			//para leer el arma asociada al idArma
			String queryarma = "SELECT * FROM armas where id = " + idArma;
			ResultSet rsArma = stArma.executeQuery(queryarma);
			rsArma.next();
			String nombreArma = rsArma.getString("nombre");
			int rarezaArma = rsArma.getInt("rareza");
			Arma arma = new Arma(idArma, nombreArma, rarezaArma);
			
			Personaje personaje = new Personaje(id, nombre, rareza, arma, elemento);
			map.put(id, personaje);
		}
		stArma.close();
		st.close();
		return map;
	}
	
	public HashMap<Integer, Arma> leerArma() throws SQLException {
		HashMap<Integer, Arma> map = new HashMap<>();
		String query = "SELECT * FROM armas";
		Statement st = conn.createStatement();
		Statement stArma = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			//leemos arma
			int id = rs.getInt("id");
			String nombre = rs.getString("nombre");
			int rareza = rs.getInt("rareza");
			Arma arma = new Arma(id, nombre, rareza);
			map.put(id, arma);
		}
		stArma.close();
		st.close();
		return map;
	}

	public Personaje buscar(int keyB) throws SQLException {
		String query = "SELECT * FROM personajes where id = " + keyB + ";";
		Personaje personaje = null;
		Statement st = conn.createStatement();
		Statement stweapon = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			//leemos personaje
			int id = rs.getInt("id");
			String nombre = rs.getString("nombre");
			int rareza = rs.getInt("rareza");
			int idArma = rs.getInt("idArma");
			String elemento = rs.getString("elemento");
			
			//para leer el arma asociada al idArma
			String queryarma = "SELECT * FROM armas where id = " + idArma;
			ResultSet rsArma = stweapon.executeQuery(queryarma);
			rsArma.next();
			String nombreArma = rsArma.getString("nombre");
			int rarezaarma = rsArma.getInt("rareza");
			Arma arma = new Arma(idArma, nombreArma, rarezaarma);
			
			personaje = new Personaje(id, nombre, rareza, arma, elemento);
		}
		
		return personaje;
		
	}

	public Arma buscarArma(int keyB) throws SQLException {
		String query = "SELECT * FROM armas where id = " + keyB + ";";
		Arma arma = null;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			rs.next();
			String nombreArma = rs.getString("nombre");
			int rarezaarma = rs.getInt("rareza");
			arma = new Arma(keyB, nombreArma, rarezaarma);
		}
		return arma;
	}

	public void insertar(Personaje p) throws SQLException {
		int id = p.getId();
		String nombre = p.getNombre();
		int rareza = p.getRareza();
		int idArma = p.getArma().getId();
		String elemento = p.getElemento();
		String query = "INSERT into personajes (id, nombre, rareza, idArma, elemento) VALUES (" + id + ", '" + nombre + "', "
				+ rareza + ", '" + idArma + "', '" + elemento + "');";
		Statement st = conn.prepareStatement(query);
		st.executeUpdate(query);
		st.close();
	}

	public void insertar(Arma w) throws SQLException {
		int id = w.getId();
		String nombre = w.getNombre();
		int rareza = w.getRareza();
		String query = "INSERT into armas (id, nombre, rareza) VALUES (" + id + ", '" + nombre + "', " + rareza + ");";
		Statement st = conn.prepareStatement(query);
		st.executeUpdate(query);
		st.close();
	}

	public void modificar(int keyM, int campo, String valor) throws SQLException {
		String query;
		String campoF = switch (campo) {
            case 1 -> "nombre";
            case 2 -> "rareza";
            case 3 -> "idArma";
            case 4 -> "elemento";
            default -> "";
        };
        query = "update personajes set " + campoF + " = '" + valor + "' where id = " + keyM + ";";
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
	}
	
	public void modificarArma(int idModificar, int campo, String valor) throws SQLException {
		String query;
		String campoF = switch (campo) {
            case 1 -> "nombre";
            case 2 -> "rareza";
            default -> "";
        };
        query = "update armas set " + campoF + " = '" + valor + "' where id = " + idModificar + ";";
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
	}

	public void eliminar(int id) throws SQLException {
		String query = "delete from personajes where id = " + id + ";";
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
	}
	
	public void eliminarArma(int id) throws SQLException {
		String query = "delete from armas where id = " + id + ";";
		Statement st = conn.createStatement();
		st.executeUpdate(query);
		st.close();
	}
	

	public void escribir(HashMap<Integer, Personaje> mapa) throws SQLException {
		String query = "TRUNCATE table personajes;";
		PreparedStatement st = conn.prepareStatement(query);
		st.execute();
		st.close();
		for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
			Personaje p = entry.getValue();
			int id = p.getId();
			String nombre = p.getNombre();
			int rareza = p.getRareza();
			Arma arma = p.getArma();
			String elemento = p.getElemento();
			query = "INSERT into personajes (id, nombre, rareza, idArma, elemento) VALUES (" + id + ", '" + nombre + "', "
					+ rareza + ", '" + arma.getId() + "', '" + elemento + "');";
			Statement st2 = conn.createStatement();
			st2.executeUpdate(query);
			st2.close();
		}
	}
	
	public void escribirArma(HashMap<Integer, Arma> mapaArma) throws SQLException {
		String query = "TRUNCATE table armas;";
		PreparedStatement st = conn.prepareStatement(query);
		st.execute();
		st.close();
		for (Map.Entry<Integer, Arma> entry : mapaArma.entrySet()) {
			Arma arma = entry.getValue();
			int id = arma.getId();
			String nombre = arma.getNombre();
			int rareza = arma.getRareza();
			query = "INSERT into personajes (id, nombre, rareza) VALUES (" + id + ", '" + nombre + "', "
					+ rareza + ");";
			Statement st2 = conn.createStatement();
			st2.executeUpdate(query);
			st2.close();
		}
	}




}
