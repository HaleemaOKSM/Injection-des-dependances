package net.ouaksim.dao;

public class DaoImpl implements IDao{

    @Override
    public double getData() {
        System.out.println("Version Base de données");
        double temp = 23;
        return temp;
    }
}
