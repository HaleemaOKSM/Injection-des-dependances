package net.ouaksim.presentation;

import net.ouaksim.dao.IDao;
import net.ouaksim.metier.IMetier;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class DynamicPresentation {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Scanner scan  = new Scanner(new File("config.txt"));

        String daoClassName = scan.nextLine();
        Class cDao = Class.forName(daoClassName);
        IDao dao = (IDao) cDao.newInstance();
        System.out.println(dao.getData());

        String metierClassName = scan.nextLine();
        Class cMetier = Class.forName(metierClassName);
        IMetier metier  = (IMetier) cMetier.getConstructor(IDao.class).newInstance(dao);
        System.out.println(metier.calcul());

    }
}