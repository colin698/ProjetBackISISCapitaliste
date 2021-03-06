/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author colin
 */
public class Services {

    World readWorldFromXml(String username) throws JAXBException {
        World world = null;
        try {
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Unmarshaller u = cont.createUnmarshaller();
            File file = new File(username + "-world.xml");
            world = (World) u.unmarshal(file);
        } catch (Exception e) {
            InputStream input = getClass().getClassLoader().getResourceAsStream("world.xml");
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Unmarshaller u = cont.createUnmarshaller();
            world = (World) u.unmarshal(input);
        }
        return world;
    }

    void saveWorldToXML(String username, World world) {
        try {
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller u = cont.createMarshaller();
            OutputStream output = new FileOutputStream(username + "-world.xml");
            u.marshal(world, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    World getWorld(String username) throws JAXBException {
        World leMonde = readWorldFromXml(username);
        saveWorldToXML(username, leMonde);//eadWorldFromXml(String username);
        return leMonde;
    }

    // prend en paramètre le pseudo du joueur et le produit
// sur lequel une action a eu lieu (lancement manuel de production ou
// achat d’une certaine quantité de produit)
// renvoie false si l’action n’a pas pu être traitée
    public Boolean updateProduct(String username, ProductType newproduct) throws JAXBException {
        // aller chercher le monde qui correspond au joueur
        World world = getWorld(username);
        // trouver dans ce monde, le produit équivalent à celui passé
        // en paramètre
        ProductType product = findProductById(world, newproduct.getId());
        if (product == null) {
            return false;
        }

        // calculer la variation de quantité. Si elle est positive c'est
        // que le joueur a acheté une certaine quantité de ce produit
        // sinon c’est qu’il s’agit d’un lancement de production.
        int qtchange = newproduct.getQuantite() - product.getQuantite();
        if (qtchange > 0) {
            // soustraire de l'argent du joueur le cout de la quantité
            // achetée et mettre à jour la quantité de product

        } else {
            // initialiser product.timeleft à product.vitesse
            // pour lancer la production
        }
        // sauvegarder les changements du monde
        saveWorldToXML(username, world);
        return true;
    }

    public ProductType findProductById(World world, int id) {
        ProductType idProduit = null;
        for (ProductType produit : world.getProducts().product) {
            if (id == produit.id) {
                idProduit = produit;
            }
        }
        return idProduit;
    }
    // prend en paramètre le pseudo du joueur et le manager acheté.
// renvoie false si l’action n’a pas pu être traitée

    public Boolean updateManager(String username, PallierType newmanager) throws JAXBException {
        // aller chercher le monde qui correspond au joueur
        World world = getWorld(username);
        // trouver dans ce monde, le manager équivalent à celui passé
        // en paramètre
        PallierType manager = findManagerByName(world, newmanager.getName());
        if (manager == null) {
            return false;
        }

        // débloquer ce manager
        // trouver le produit correspondant au manager
        ProductType product = findProductById(world, manager.getIdcible());
        if (product == null) {
            return false;
        }
        // débloquer le manager de ce produit

        // soustraire de l'argent du joueur le cout du manager
        // sauvegarder les changements au monde
        saveWorldToXML(username, world);
        return true;
    }

    public PallierType findManagerByName(World world, String nom) {
        PallierType nomManager = null;
        for (PallierType nomMana : world.getManagers().pallier) {
            if (nom.equals(nomMana.getName())) {
                nomManager = nomMana;
            }
        }
        return nomManager;
    }
}
