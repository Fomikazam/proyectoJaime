package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import clases.Personaje;
import clases.Arma;

public class ModeloSQLite extends Modelo {

    HashMap<Integer, Personaje> mapa = new HashMap<Integer, Personaje>();
    Connection conn;

    public ModeloSQLite(Connection conn) {
        this.conn = conn;
    }

    public HashMap<Integer, Personaje> leer() throws SQLException {
        HashMap<Integer, Personaje> map = new HashMap<Integer, Personaje>();
        String query = "SELECT * FROM personajes";
        Statement st = conn.createStatement();
        Statement stArma = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            int rareza = rs.getInt("rareza");
            int idArma = rs.getInt("idArma");
            String elemento = rs.getString("elemento");

            String queryArma = "SELECT * FROM armas where id = " + idArma;
            ResultSet rsArma = stArma.executeQuery(queryArma);
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
        HashMap<Integer, Arma> map = new HashMap<Integer, Arma>();
        String query = "SELECT * FROM armas";
        Statement st = conn.createStatement();
        Statement stArma = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            //leemos personaje
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
        String name = p.getNombre();
        int rarity = p.getRareza();
        int weapon = p.getArma().getId();
        String element = p.getElemento();
        String query = "INSERT into personajes (id, name, rarity, weapon, element) VALUES (" + id + ", '" + name + "', "
                + rarity + ", '" + weapon + "', '" + element + "');";
        Statement st = conn.createStatement();
        st.executeUpdate(query);
        st.close();
    }

    public void insertar(Arma w) throws SQLException {
        int id = w.getId();
        String name = w.getNombre();
        int rarity = w.getRareza();
        String query = "INSERT into armas (id, name, rarity) VALUES (" + id + ", '" + name + "', " + rarity + ");";
        System.out.println(query);
        Statement st = conn.createStatement();
        st.executeUpdate(query);
        st.close();
    }

    public void modificar(int keyM, int campo, String valor) throws SQLException {
        String query;
        String campoF = "";
        switch (campo) {
            case 1:
                campoF = "nombre";
                break;
            case 2:
                campoF = "rareza";
                break;
            case 3:
                campoF = "idArma";
                break;
            case 4:
                campoF = "elemento";
                break;
        }
        query = "update personajes set " + campoF + " = '" + valor + "' where id = " + keyM + ";";
        Statement st = conn.createStatement();
        st.executeUpdate(query);
        st.close();
    }

    public void modificarArma(int idModificar, int campo, String valor) throws SQLException {
        String query;
        String campoF = "";
        switch (campo) {
            case 1:
                campoF = "nombre";
                break;
            case 2:
                campoF = "rareza";
                break;
        }
        query = "update armas set " + campoF + " = '" + valor + "' where id = " + idModificar + ";";
        System.out.println(query);
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
        String query = "delete from personajes;";
        PreparedStatement st = conn.prepareStatement(query);
        st.execute();
        st.close();
        for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
            Personaje p = entry.getValue();
            int id = p.getId();
            String name = p.getNombre();
            int rarity = p.getRareza();
            Arma weapon = p.getArma();
            String element = p.getElemento();
            query = "INSERT into personajes (id, name, rarity, weapon, element) VALUES (" + id + ", '" + name + "', "
                    + rarity + ", '" + weapon + "', '" + element + "');";
            Statement st2 = conn.createStatement();
            st2.executeUpdate(query);
            st2.close();
        }
    }

    public ArrayList<Arma> leerW() throws SQLException {
        ArrayList<Arma> mapaW = new ArrayList<Arma>();
        String query = "SELECT * FROM armas";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            mapaW.add(new Arma(rs.getInt("id"), rs.getString("name"), rs.getInt("rarity")));
        }
        st.close();
        return mapaW;
    }

    public boolean comprobarIdArma(String nombre) throws SQLException {
        String query = "SELECT name FROM armas";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            String nombreW = rs.getString("name");
            if (nombreW.equals(nombre)) {
                return true;
            }
        }
        st.close();
        return false;
    }

}
