package net.ouaksim.presentation;

import net.ouaksim.dao.DaoImpl;
import net.ouaksim.dao.IDao;
import net.ouaksim.metier.IMetier;
import net.ouaksim.metier.MetierImpl;

public class StaticPresentation {
    public static void main(String[] args) {
        IDao dao = new DaoImpl();
        IMetier metier = new MetierImpl(dao); // Injection statique via le constructeur
        System.out.println("Résultat calcul (statique) : " + metier.calcul());
    }
}
