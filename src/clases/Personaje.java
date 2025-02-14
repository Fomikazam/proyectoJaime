package clases;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Personaje implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String nombre;
    private int rareza;
    private Arma arma = new Arma();
    private String elemento;

    public Personaje(int id, String nombre, int rareza, Arma arma, String elemento) {
        this.id = id;
        this.nombre = nombre;
        this.rareza = rareza;
        this.arma = arma;
        this.elemento = elemento;
    }

    public Personaje() {

    }

    public int getId() {
        return id;
    }

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

    public Arma getArma() {
        return arma;
    }

    public void setArma(Arma arma) {
        this.arma = arma;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    @Override
    public String toString() {
        String tostring = "";
        tostring = tostring.concat("Nombre: " + nombre + " " + rareza + "⭐ ");
        String elemento = "";
        switch (this.elemento.toLowerCase().trim()) {
            case "pyro":
                elemento = elemento.concat("Pyro🔥");
                break;
            case "hydro":
                elemento = elemento.concat("Hydro💧");
                break;
            case "dendro":
                elemento = elemento.concat("Dendro🌱");
                break;
            case "geo":
                elemento = elemento.concat("Geo🪨");
                break;
            case "anemo":
                elemento = elemento.concat("Anemo🍃");
                break;
            case "electro":
                elemento = elemento.concat("Electro🌩️");
                break;
            case "cryo":
                elemento = elemento.concat("Cryo❄️");
                break;
            default:
                elemento = elemento.concat("Elemento no reconocido");
                break;
        }
        tostring = tostring.concat(elemento);
        tostring = tostring.concat("\n" + arma);
        return tostring;
    }

}
