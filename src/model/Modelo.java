package model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.JDOMException;

import clases.Personaje;
import clases.Arma;

public class Modelo {

    public HashMap<Integer, Personaje> leer()
            throws IOException, ClassNotFoundException, JDOMException, SQLException {
        return null;
    }

    public HashMap<Integer, Arma> leerArma() throws SQLException, IOException {
        return null;
    }

    public Personaje buscar(int keyB) throws SQLException, IOException {
        return null;
    }

    public Arma buscarArma(int keyB) throws SQLException, IOException {
        return null;
    }

    public void insertar(Personaje p) throws SQLException, IOException {
    }

    public void insertar(Arma w) throws SQLException, IOException {
    }

    public void modificar(int keyM, int campo, String valor) throws SQLException, IOException {
    }

    public void modificarArma(int idModificar, int campo, String valor) throws SQLException, IOException {
    }

    public void eliminar(int id) throws SQLException, IOException {
    }

    public void eliminarArma(int id) throws SQLException, IOException {
    }

    public void escribir(HashMap<Integer, Personaje> mapa, HashMap<Integer, Arma> mapaArma) throws IOException, SQLException {
    }

    public void escribir(HashMap<Integer, Personaje> mapa) throws IOException, SQLException {
    }

    public void escribirArma(HashMap<Integer, Arma> mapaArma) throws SQLException {
    }

    //los que usamos para hibernate
    public void modificar(Personaje personajeAux) {
    }

    public void modificarArma(Arma arma) {
    }

    public void eliminar(Personaje p) throws SQLException, IOException {
    }

    public void eliminarArma(Arma a) throws SQLException, IOException {
    }


    public Personaje buscar(HashMap<Integer, Personaje> mapa, int keyB) {
        for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
            if (entry.getKey() == keyB) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Arma buscarArma(HashMap<Integer, Arma> mapaW, int keyB) {
        for (Map.Entry<Integer, Arma> entry : mapaW.entrySet()) {
            if (entry.getValue().getId() == keyB) {
                return entry.getValue();
            }
        }
        return null;
    }

    public HashMap<Integer, Arma> leerArma(HashMap<Integer, Personaje> mapa) throws SQLException {
        HashMap<Integer, Arma> mapaArma = new HashMap<>();
        for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
            mapaArma.put(entry.getValue().getId(), entry.getValue().getArma());

        }
        return mapaArma;
    }

    public boolean comprobarNombreArma(String nombre, HashMap<Integer, Arma> mapaW) {
        for (Map.Entry<Integer, Arma> entry : mapaW.entrySet()) {
            if (entry.getValue().getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public void modificar(HashMap<Integer, Personaje> mapa, Personaje personaje)
            throws SQLException, IOException {
        mapa.put(personaje.getId(), personaje);
        this.escribir(mapa);
    }

    public boolean comprobarNombre(String nombre, HashMap<Integer, Personaje> mapa) {
        for (Map.Entry<Integer, Personaje> p : mapa.entrySet()) {
            if (p.getValue().getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public void eliminar(HashMap<Integer, Personaje> mapa, Personaje pE) throws IOException, SQLException {
        mapa.remove(pE.getId());
        this.escribir(mapa);
    }

}