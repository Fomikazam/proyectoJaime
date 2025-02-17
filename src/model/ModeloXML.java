package model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.io.FileOutputStream;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import clases.Personaje;
import clases.Arma;

public class ModeloXML extends Modelo {
    private final File archivo;

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
            for (Element personaje : lista) {
                Arma weapon = new Arma(Integer.parseInt(personaje.getChild("arma").getChildText("id")), personaje.getChild("arma").getChildText("nombre"), Integer.parseInt(personaje.getChild("arma").getChildText("rareza")));
                mapa.put(Integer.parseInt(personaje.getAttributeValue("id")),
                        new Personaje(Integer.parseInt(personaje.getAttributeValue("id")), personaje.getChildText("nombre"),
                                Integer.parseInt(personaje.getChildText("rareza")), weapon, personaje.getChildText("elemento")));
            }
        }
        return mapa;
    }

    public void insertar(Personaje p) throws IOException {
        HashMap<Integer, Personaje> mapa = new HashMap<>();
        mapa.put(p.getId(), p);
        this.escribir(mapa);
    }

    public void escribir(HashMap<Integer, Personaje> mapa) throws IOException {
        Personaje p;
        Element personajes = new Element("personajes");
        Document document = new Document(personajes);
        for (Map.Entry<Integer, Personaje> entrada : mapa.entrySet()) {
            Integer clave = entrada.getKey();
            p = entrada.getValue();
            Element personaje = new Element("personaje");
            personajes.addContent(personaje);
            Attribute id = new Attribute("id", String.valueOf(clave));
            personaje.setAttribute(id);
            Element nombre = new Element("nombre");
            nombre.setText(p.getNombre());
            personaje.addContent(nombre);
            Element rareza = new Element("rareza");
            personaje.addContent(rareza);
            rareza.setText(String.valueOf(p.getRareza()));
            //para escribir arma
            Element arma = new Element("arma");
            Element idArma = new Element("id");
            idArma.setText(String.valueOf(p.getArma().getId()));
            arma.addContent(idArma);
            Element nombreArma = new Element("nombre");
            nombreArma.setText(p.getArma().getNombre());
            arma.addContent(nombreArma);
            Element rarezaArma = new Element("rareza");
            rarezaArma.setText(String.valueOf(p.getArma().getRareza()));
            arma.addContent(rarezaArma);
            personaje.addContent(arma);

            Element elemento = new Element("elemento");
            elemento.setText(String.valueOf(p.getElemento()));
            personaje.addContent(elemento);
        }
        Format f = Format.getPrettyFormat();
        f.setEncoding("gbk");
        f.setOmitDeclaration(false);
        XMLOutputter xmlOut = new XMLOutputter(f);
        xmlOut.output(document, new FileOutputStream(archivo));
    }
}
