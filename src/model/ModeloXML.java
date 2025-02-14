package model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.io.FileOutputStream;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Scanner;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

import clases.Personaje;
import clases.Arma;

import org.jdom2.input.SAXBuilder;

public class ModeloXML extends Modelo {
	private File archivo;
	private Personaje p;

	public ModeloXML(File archivo) {
		this.archivo = archivo;
	}

	public HashMap<Integer, Personaje> leer() throws IOException, JDOMException {
		HashMap<Integer, Personaje> mapa = new HashMap<>();
		SAXBuilder saxBuilder = new SAXBuilder();
		if (archivo.length() > 0) {
			Document document = saxBuilder.build(archivo);
			Element classElement = document.getRootElement();
			List<Element> lista = classElement.getChildren();
			for (int i = 0; i < lista.size(); i++) {
				Element personaje = lista.get(i);
				Arma weapon = new Arma(Integer.parseInt(personaje.getChild("arma").getChildText("id")), personaje.getChild("arma").getChildText("nombre"), Integer.parseInt(personaje.getChild("arma").getChildText("rareza")));
				mapa.put(Integer.parseInt(personaje.getAttributeValue("id")),
						new Personaje(Integer.parseInt(personaje.getAttributeValue("id")), personaje.getChildText("nombre"),
								Integer.parseInt(personaje.getChildText("rareza")), weapon,	personaje.getChildText("elemento")));
			}
			return mapa;
		} else {
			return mapa;
		}
	}

	public void insertar(Personaje p) throws IOException {
		HashMap<Integer, Personaje> mapa = new HashMap<>();
		mapa.put(p.getId(), p);
		this.escribir(mapa);
	}

	public void escribir(HashMap<Integer, Personaje> mapa) throws IOException {
		try {
			Element personajes = new Element("personajes");
			Document document = new Document(personajes);
			for (Map.Entry<Integer, Personaje> entrada : mapa.entrySet()) {
				Integer clave = entrada.getKey();
				p = entrada.getValue();
				Element personaje = new Element("personaje");
				personajes.addContent(personaje);
				Attribute attr = new Attribute("id", String.valueOf(clave));
				personaje.setAttribute(attr);
				Element nombre = new Element("nombre");
				nombre.setText(p.getNombre());
				personaje.addContent(nombre);
				Element rareza = new Element("rareza");
				personaje.addContent(rareza);
				rareza.setText(String.valueOf(p.getRareza()));
				Element weapon = new Element("arma");
				Element idweapon = new Element("id");
				idweapon.setText(String.valueOf(p.getArma().getId()));
				weapon.addContent(idweapon);
				Element weaponname = new Element("nombre");
				weaponname.setText(p.getArma().getNombre());
				weapon.addContent(weaponname);
				Element weaponrarity = new Element("rareza");
				weaponrarity.setText(String.valueOf(p.getArma().getRareza()));
				weapon.addContent(weaponrarity);
				personaje.addContent(weapon);
				Element element = new Element("elemento");
				element.setText(String.valueOf(p.getElemento()));
				personaje.addContent(element);
			}
			Format f = Format.getPrettyFormat();
			f.setEncoding("gbk");
			f.setOmitDeclaration(false);
			XMLOutputter xmlOut = new XMLOutputter(f);
			xmlOut.output(document, new FileOutputStream(archivo));
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
