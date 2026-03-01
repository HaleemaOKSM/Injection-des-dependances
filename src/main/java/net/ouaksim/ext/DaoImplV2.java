package net.ouaksim.ext;

import net.ouaksim.dao.IDao;
import org.springframework.stereotype.Component;

@Component("d2")
public class DaoImplV2 implements IDao {

    @Override
    public double getData() {
        System.out.println("version capteurs ....");
        double data = 12;
        return data;
    }
}
