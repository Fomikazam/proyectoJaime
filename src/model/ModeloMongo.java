package model;

import clases.Arma;
import clases.Personaje;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ModeloMongo extends Modelo  {

    MongoClient cliente;
    MongoCollection personajes;
    MongoCollection armas;

    public ModeloMongo(MongoClient mongoClient) {
        cliente = mongoClient;
        MongoDatabase database = mongoClient.getDatabase("bdpersonajes");
        personajes = database.getCollection("personajes");
        armas = database.getCollection("armas");
    }

    public HashMap<Integer, Personaje> leer() {
        MongoCursor personajesCursor = personajes.find().iterator();
        HashMap<Integer, Personaje> mapa = new HashMap<>();
        while (personajesCursor.hasNext()) {
            Document personajeLeido = (Document) personajesCursor.next();
            int idArma = personajeLeido.getInteger("idArma");
            Arma arma = this.buscarArma(idArma);
            Personaje personaje = new Personaje(personajeLeido.getInteger("id"), personajeLeido.getString("nombre"), personajeLeido.getInteger("rareza"), arma, personajeLeido.getString("elemento"));
            mapa.put(personajeLeido.getInteger("id"), personaje);
        }
        return mapa;
    }

    public HashMap<Integer, Arma> leerArma() {
        MongoCursor armasCursor = armas.find().iterator();
        HashMap<Integer, Arma> mapa = new HashMap<>();
        while (armasCursor.hasNext()) {
            Document armaLeida = (Document) armasCursor.next();
            Arma arma = new Arma(armaLeida.getInteger("id"), armaLeida.getString("nombre"), armaLeida.getInteger("rareza"));
            mapa.put(armaLeida.getInteger("id"), arma);
        }
        return mapa;
    }

    public Personaje buscar(int id) {
        Document criterioBusqueda = new Document();
        criterioBusqueda.put("id", id);
        MongoCursor busqueda = personajes.find(criterioBusqueda).iterator();
        Document personajeLeido = (Document) busqueda.next();
        int idArma = Integer.parseInt(personajeLeido.getDouble("arma").toString());
        Arma arma = this.buscarArma(idArma);
        Personaje personaje = new Personaje(personajeLeido.getInteger("id"), personajeLeido.getString("nombre"), personajeLeido.getInteger("rareza"), arma, personajeLeido.getString("elemento"));
        return personaje;
    }

    public Arma buscarArma(int id) {
        Document criterioBusqueda = new Document();
        criterioBusqueda.put("id", id);
        MongoCursor busqueda = armas.find(criterioBusqueda).iterator();
        if (busqueda.hasNext()) {
            Document armaLeida = (Document) busqueda.next();
            Arma arma = new Arma(armaLeida.getInteger("id"), armaLeida.getString("nombre"), armaLeida.getInteger("rareza"));
            return arma;
        }
        return null;
    }

    public void insertar(Personaje personaje) {
        Document personajeInsertar = new Document();
        personajeInsertar.put("id", personaje.getId());
        personajeInsertar.put("nombre", personaje.getNombre());
        personajeInsertar.put("rareza", personaje.getRareza());
        personajeInsertar.put("idArma", personaje.getArma().getId());
        personajeInsertar.put("elemento", personaje.getElemento());
        personajes.insertOne(personajeInsertar);
    }

    public void insertar(Arma arma) {
        Document armaInsertar = new Document();
        armaInsertar.put("id", arma.getId());
        armaInsertar.put("nombre", arma.getNombre());
        armaInsertar.put("rareza", arma.getRareza());
        armas.insertOne(armaInsertar);
    }

    public void modificar(int idModificar, int campo, String valor) throws SQLException {
        String campoF = "";
        int tipo = -1;
        switch (campo) {
            case 1:
                campoF = "nombre";
                tipo = 0;
                break;
            case 2:
                campoF = "rareza";
                tipo = 1;
                break;
            case 3:
                campoF = "idArma";
                tipo = 1;
                break;
            case 4:
                campoF = "elemento";
                tipo = 0;
                break;
        }
        Document buscar = new Document();
        buscar.append("id", idModificar);
        Document modificar = new Document();
        switch (tipo) {
            case 0:
                modificar.append(campoF, valor);
                break;
            case 1:
                modificar.append(campoF, Integer.parseInt(valor));
                break;
        }
        personajes.findOneAndUpdate(buscar, new Document("$set", modificar));
    }

    public void modificarArma(int idModificar, int campo, String valor) throws SQLException {
        String campoF = "";
        Document buscar = new Document();
        buscar.append("id", idModificar);
        Document modificar = new Document();
        switch (campo) {
            case 1:
                campoF = "nombre";
                modificar.append(campoF, valor);
                break;
            case 2:
                campoF = "rareza";
                modificar.append(campoF, Integer.parseInt(valor));
                break;
        }
        armas.findOneAndUpdate(buscar, new Document("$set", modificar));
    }

    public void eliminar(int id) {
        personajes.deleteOne(new Document("id", id));
    }

    public void eliminarArma(int id) {
        armas.deleteOne(new Document("id", id));
    }

    public void escribir(HashMap<Integer, Personaje> mapa, HashMap<Integer, Arma> mapaArmas) {
        personajes.deleteMany(new Document());
        for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
            this.insertar(entry.getValue());
        }
        this.escribirArma(mapaArmas);
    }

    public void escribirArma(HashMap<Integer, Arma> mapaArma) {
        armas.deleteMany(new Document());
        for (Map.Entry<Integer, Arma> entry : mapaArma.entrySet()) {
            this.insertar(entry.getValue());
        }
    }

}
