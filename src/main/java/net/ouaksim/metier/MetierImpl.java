package net.ouaksim.metier;

import net.ouaksim.dao.DaoImpl;
import net.ouaksim.dao.IDao;

public class MetierImpl implements IMetier{
    private IDao dao;

    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    public MetierImpl() {
    }

    @Override
    public double calcul() {
        double data = dao.getData();
        return data * 43/3;
    }

    public void setDao(DaoImpl dao) {
        this.dao = dao;
    }
}
