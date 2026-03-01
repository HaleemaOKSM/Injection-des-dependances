package net.ouaksim.metier;

import net.ouaksim.dao.DaoImpl;
import net.ouaksim.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("metier")
public class MetierImpl implements IMetier{
    @Autowired
    @Qualifier("d")
// or
// @Qualifier("d2")
    private IDao dao;

    public MetierImpl( IDao dao) {
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
