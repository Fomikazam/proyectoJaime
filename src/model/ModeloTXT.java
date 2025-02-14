package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import clases.Personaje;
import clases.Arma;

public class ModeloTXT extends Modelo {
	private final File archivo;
	private final HashMap<Integer, Personaje> mapa = new HashMap<>();

    public ModeloTXT(File archivo) {
		this.archivo = archivo;
	}

	public HashMap<Integer, Personaje> leer() throws IOException {
		FileReader lector = new FileReader(archivo);
		BufferedReader buffer = new BufferedReader(lector);
		String linea = buffer.readLine();
		while (linea != null) {
			String[] split = linea.split(";");
			Arma weapon = new Arma(Integer.parseInt(split[3]), split[4], Integer.parseInt(split[5]));
            Personaje personaje = new Personaje(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]), weapon, split[6]);
			mapa.put(Integer.parseInt(split[0]), personaje);
			linea = buffer.readLine();
		}
		lector.close();
		buffer.close();
		return mapa;
	}

	public void insertar(Personaje personaje) throws IOException {
		FileWriter escritor = new FileWriter(archivo, true);
		BufferedWriter buffer = new BufferedWriter(escritor);
		buffer.write(personaje.getId() + ";" + personaje.getNombre() + ";" + personaje.getRareza() + ";" + personaje.getArma().getId() + ";"
				+ personaje.getArma().getNombre() + ";" + personaje.getArma().getRareza() + ";" + personaje.getElemento() + "\n");
		buffer.close();
		escritor.close();
	}

	public void escribir(HashMap<Integer, Personaje> mapa) throws IOException {
		FileWriter escritor = new FileWriter(archivo, false);
		BufferedWriter buffer = new BufferedWriter(escritor);
		int veces = mapa.size();
		for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
			buffer.write(
					entry.getValue().getId() + ";" + entry.getValue().getNombre() + ";" + entry.getValue().getRareza()
							+ ";" + entry.getValue().getArma().getId() + ";" + entry.getValue().getArma().getNombre() + ";"
							+ entry.getValue().getArma().getRareza() + ";" + entry.getValue().getElemento());
			if (veces > 0) {
				buffer.write("\n");
				veces--;
			}
		}
		buffer.close();
		escritor.close();
	}

}
