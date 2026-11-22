package presentation;

import dao.DaoImpl;
import metier.MetierImpl;

/**
 * Version 1 : Instanciation statique (Couplage fort).
 * Les classes concrètes sont codées en dur avec l'opérateur 'new'.
 */
public class Presentation1 {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("   VERSION 1 : INSTANCIATION STATIQUE (COUPLAGE FORT - 'NEW')            ");
        System.out.println("==========================================================================");

        DaoImpl dao = new DaoImpl();
        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);

        double resultat = metier.calcul();
        System.out.println("Résultats = " + resultat);
    }
}
