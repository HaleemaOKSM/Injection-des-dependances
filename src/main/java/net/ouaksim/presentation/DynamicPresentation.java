package net.ouaksim.presentation;

import net.ouaksim.dao.IDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DynamicPresentation {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Scanner scan  = new Scanner(new File("config.txt"));

        String daoClassName = scan.nextLine();
        Class cDao = Class.forName(daoClassName);
        IDao dao = (IDao) cDao.newInstance();
        System.out.println(dao.getData());
    }
}