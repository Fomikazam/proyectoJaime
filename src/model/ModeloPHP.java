package model;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import clases.Arma;
import clases.Personaje;

public class ModeloPHP extends Modelo {

	ApiRequests encargadoPeticiones;
	String SERVER_PATH;

	public ModeloPHP() {
		encargadoPeticiones = new ApiRequests();
		SERVER_PATH = "http://localhost/alvaro/bdpersonajes/";
	}

	public HashMap<Integer, Personaje> leer() throws IOException, SQLException {
		HashMap<Integer, Personaje> mapa = new HashMap<Integer, Personaje>();
		String response = encargadoPeticiones.getRequest(SERVER_PATH + "principalP.php?metodo=GET");
		JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());
		String estado = (String) respuesta.get("estado");
		if (estado.equals("ok")) {
			JSONArray array = (JSONArray) respuesta.get("personajes");
			Personaje personaje;
			Arma arma;
			String nombre, elemento;
			int id, rareza, idArma;

            for (Object o : array) {
                JSONObject row = (JSONObject) o;
                nombre = row.get("nombre").toString();
                id = Integer.parseInt(row.get("id").toString());
                rareza = Integer.parseInt(row.get("rareza").toString());
                elemento = row.get("elemento").toString();
                idArma = Integer.parseInt(row.get("idArma").toString());
                arma = this.buscarArma(idArma);
                personaje = new Personaje(id, nombre, rareza, arma, elemento);
                mapa.put(id, personaje);
            }
		}
		return mapa;
	}
	
	public HashMap<Integer, Arma> leerArma() throws IOException, SQLException {
		HashMap<Integer, Arma> mapaArma = new HashMap<Integer, Arma>();
		String response = encargadoPeticiones.getRequest(SERVER_PATH + "principalA.php?metodo=GET");
		JSONObject respuesta = (JSONObject) JSONValue.parse(response);
		String estado = (String) respuesta.get("estado");
		if (estado.equals("ok")) {
			JSONArray array = (JSONArray) respuesta.get("armas");
			Arma arma;
			String nombre;
			int rareza, idArma;
            for (Object o : array) {
                JSONObject row = (JSONObject) o;
                nombre = row.get("nombreArma").toString();
                rareza = Integer.parseInt(row.get("rarezaArma").toString());
                idArma = Integer.parseInt(row.get("idArma").toString());
                arma = new Arma(idArma, nombre, rareza);
                mapaArma.put(idArma, arma);
            }
		}
		return mapaArma;
	}

	public Personaje buscar(int idBuscar) throws SQLException, IOException {
		String response = encargadoPeticiones.getRequest(SERVER_PATH + "leePersonaje.php" + "?id=" + idBuscar);
		JSONObject respuesta = (JSONObject) JSONValue.parse(response);
		String estado = (String) respuesta.get("estado");
		Personaje personaje = null;
		if (estado.equals("ok")) {
			JSONArray array = (JSONArray) respuesta.get("personajes");
			Arma arma;
			String nombre, elemento, nombreArma;
			int id, rareza, idArma, rarezaArma;

			for (int i = 0; i < array.size(); i++) {
				JSONObject row = (JSONObject) array.get(i);
				nombre = row.get("nombre").toString();
				id = Integer.parseInt(row.get("id").toString());
				rareza = Integer.parseInt(row.get("rareza").toString());
				elemento = row.get("elemento").toString();
				idArma = Integer.parseInt(row.get("idArma").toString());

				// para leer el arma
				String responseArma = encargadoPeticiones.getRequest(SERVER_PATH + "leeArma.php" + "?id=" + idArma);
				JSONObject respuestaArma = (JSONObject) JSONValue.parse(responseArma);
				JSONArray arrayArma = (JSONArray) respuestaArma.get("armas");
				JSONObject rowArma = (JSONObject) arrayArma.get(0);
				nombreArma = rowArma.get("nombreArma").toString();
				idArma = Integer.parseInt(rowArma.get("idArma").toString());
				rarezaArma = Integer.parseInt(rowArma.get("rarezaArma").toString());
				arma = new Arma(idArma, nombreArma, rarezaArma);

				personaje = new Personaje(id, nombre, rareza, arma, elemento);
			}
		}
		return personaje;
	}

	public Arma buscarArma(int idBuscar) throws SQLException, IOException {
		String responseArma = encargadoPeticiones.getRequest(SERVER_PATH + "principalA.php?metodo=GET&id=" + idBuscar);
		JSONObject respuestaArma = (JSONObject) JSONValue.parse(responseArma.toString());
		String estado = (String) respuestaArma.get("estado");
		Arma arma = null;
		if (estado.equals("ok")) {
			JSONArray arrayArma = (JSONArray) respuestaArma.get("armas");
			JSONObject rowArma = (JSONObject) arrayArma.get(0);
			String nombreArma = rowArma.get("nombreArma").toString();
			int idArma = Integer.parseInt(rowArma.get("idArma").toString());
			int rarezaArma = Integer.parseInt(rowArma.get("rarezaArma").toString());
			arma = new Arma(idArma, nombreArma, rarezaArma);
		}
		return arma;
	}
	
	public void insertar(Personaje p) throws SQLException, IOException {
		JSONObject objPeticion = new JSONObject();
		JSONObject objPersonaje = new JSONObject();

		objPersonaje.put("id", p.getId());
		objPersonaje.put("nombre", p.getNombre());
		objPersonaje.put("rareza", p.getRareza());
		objPersonaje.put("idArma", p.getArma().getId());
		objPersonaje.put("nombreArma", p.getArma().getNombre());
		objPersonaje.put("rarezaArma", p.getArma().getRareza());		
		objPersonaje.put("elemento", p.getElemento());
		
		objPeticion.put("peticion", "add");
		objPeticion.put("personajeAnnadir", objPersonaje);
		String json = objPeticion.toJSONString();
		String url = SERVER_PATH + "principalP.php?metodo=POST";
		
		String enviado = encargadoPeticiones.postRequest(url, json);
		
	}
	
	public void insertar(Arma arma) throws SQLException, IOException {
		JSONObject objPeticion = new JSONObject();
		JSONObject objArma = new JSONObject();

		objArma.put("idArma", arma.getId());
		objArma.put("nombreArma", arma.getNombre());		
		objArma.put("rarezaArma", arma.getRareza());
		
		objPeticion.put("peticion", "add");
		objPeticion.put("armaAnnadir", objArma);
		
		String json = objPeticion.toJSONString();
		String url = SERVER_PATH + "principalA.php?metodo=POST";
		System.out.println(json);
		String enviado = encargadoPeticiones.postRequest(url, json);
		
	}
	
	public void modificar(int id, int campo, String valor) throws IOException {
		JSONObject objPeticion = new JSONObject();
		JSONObject objPersonaje = new JSONObject();

		objPersonaje.put("id", id);
		objPersonaje.put("campo", campo);
		objPersonaje.put("valor", valor);
		
		objPeticion.put("peticion", "put");
		objPeticion.put("personajeMod", objPersonaje);
		
		String json = objPeticion.toJSONString();
		String url = SERVER_PATH + "principalP.php?metodo=PUT";
		
		String enviado = encargadoPeticiones.putRequest(url, json);
		
	}
	
	public void modificarArma(int id, int campo, String valor) throws IOException {
		JSONObject objPeticion = new JSONObject();
		JSONObject objArma = new JSONObject();

		objArma.put("id", id);
		objArma.put("campo", campo);
		objArma.put("valor", valor);
		
		objPeticion.put("peticion", "put");
		objPeticion.put("armaMod", objArma);
		
		String json = objPeticion.toJSONString();
		String url = SERVER_PATH + "principalA.php?metodo=PUT";
		
		encargadoPeticiones.putRequest(url, json);
		
	}
	
	public void eliminar(int id) throws IOException {
		JSONObject objPeticion = new JSONObject();
		JSONObject objPersonaje = new JSONObject();

		objPersonaje.put("id", id);
		
		objPeticion.put("peticion", "remove");
		objPeticion.put("personajeBorrar", objPersonaje);
		
		String json = objPeticion.toJSONString();
		String url = SERVER_PATH + "principalP.php?metodo=DELETE";
		
		System.out.println(json);
		
		encargadoPeticiones.putRequest(url, json);
		
	}
	
	public void eliminarArma(int id) throws IOException {
		JSONObject objPeticion = new JSONObject();
		JSONObject objArma = new JSONObject();

		objArma.put("id", id);
		
		objPeticion.put("peticion", "remove");
		objPeticion.put("armaBorrar", objArma);
		
		String json = objPeticion.toJSONString();
		String url = SERVER_PATH + "principalA.php?metodo=DELETE";
		
		System.out.println(json);
		
		encargadoPeticiones.putRequest(url, json);
		
	}

	
	

}
