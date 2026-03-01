package net.ouaksim.presentation;

import net.ouaksim.dao.DaoImpl;
import net.ouaksim.dao.IDao;
import net.ouaksim.ext.DaoImplV2;
import net.ouaksim.metier.IMetier;
import net.ouaksim.metier.MetierImpl;

public class StaticPresentation {
    public static void main(String[] args) {
//        DaoImpl dao = new DaoImpl();
        DaoImplV2 dao = new DaoImplV2();
        IMetier metier = new MetierImpl(dao); // Injection statique via le constructeur
        System.out.println("Résultat calcul (statique) : " + metier.calcul());
    }
}
