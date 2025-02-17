package vista;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import clases.Personaje;
import clases.Arma;

public class Vista {
	public Vista() {

	}

	public int preguntarInt(Scanner scan, String mensaje) throws InputMismatchException {
		System.out.println(mensaje);
		return Integer.parseInt(scan.nextLine());
	}

	public String preguntarStr(Scanner scan, String mensaje) throws InputMismatchException {
		System.out.println(mensaje);
		return scan.nextLine();
	}

	public void mostrar(String mensaje) {
		System.out.println(mensaje);
	}

	public void mostrarError(String mensaje) {
		System.err.println(mensaje);
	}

	public void mostrar(HashMap<Integer, Personaje> mapa) {
		if (mapa.isEmpty()) {
			System.out.println("No tiene personajes.");
		} else {
			for (Map.Entry<Integer, Personaje> entry : mapa.entrySet()) {
				System.out.println("Personaje " + entry.getKey() + "\n" + entry.getValue().toString() + "\n");
			}
		}
	}

	public void mostrarArmas(HashMap<Integer, Arma> mapaW) {
		if (mapaW.isEmpty()) {
			System.out.println("No tiene armas.");
		} else {
			for (Map.Entry<Integer, Arma> entry : mapaW.entrySet()) {
				System.out.println(entry.getValue().toString());
			}
		}
	}

	public void mostrar(Personaje personaje) {
		if (personaje == null) {
			System.out.println("Personaje no encontrado.");
		} else {
			System.out.println("¡Personaje encontrado!\n " + personaje);
		}
	}

	public void mostrar(Arma arma) {
		if (arma == null) {
			System.out.println("Arma no encontrada.");
		} else {
			System.out.println("¡Arma encontrada!\n " + arma);
		}
	}

}
