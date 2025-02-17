package model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import clases.Personaje;

public class ModeloBIN extends Modelo {
    private final File archivo;

    public ModeloBIN(File archivo) {
        this.archivo = archivo;
    }

    public HashMap<Integer, Personaje> leer() throws IOException, ClassNotFoundException {
        HashMap<Integer, Personaje> mapa = new HashMap<>();
        Personaje personaje;
        FileInputStream fis = new FileInputStream(archivo);
        if (fis.available() > 0) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                try {
                    personaje = (Personaje) ois.readObject();
                    mapa.put(personaje.getId(), personaje);
                } catch (EOFException eof) {
                    break;
                }
            }
            fis.close();
            ois.close();
        }
        return mapa;
    }

    public void insertar(Personaje personaje) throws IOException {
        HashMap<Integer, Personaje> mapa = new HashMap<>();
        mapa.put(personaje.getId(), personaje);
        this.escribir(mapa);
    }

    public void escribir(HashMap<Integer, Personaje> mapa) throws IOException {
        FileOutputStream fos = new FileOutputStream(archivo, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
            oos.writeObject(entry.getValue());
        }
        oos.close();
        fos.close();
    }


}
