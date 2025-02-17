package clases;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Arma implements Serializable {
	@Id
	@GeneratedValue
	private int id = 0;
	private String nombre = "";
	private int rareza = 0;
	private Set<Personaje> equipados = new HashSet<>();
	
	public Arma(int id, String name, int rarity) {
		super();
		this.id = id;
		this.nombre = name;
		this.rareza = rarity;
	}
	
	public Arma() {	}
	
	public Set<Personaje> getEquipados() { return equipados; }
	public void setEquipados(Set<Personaje> equipados) {
		this.equipados = equipados;
	}
	public int getId() { return id; }
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String name) {
		this.nombre = name;
	}
	public int getRareza() {
		return rareza;
	}
	public void setRareza(int rarity) {
		this.rareza = rarity;
	}
	
	public String toString() {
		return "Arma " + id + ": " + nombre + " " + rareza + "⭐";
	}
	
}
