package dao;

/**
 * Deuxième implémentation concrète de l'interface IDao.
 * Simule la récupération de données depuis un Web Service distant / Capteurs IoT.
 */
public class DaoImpl2 implements IDao {

    @Override
    public double getValue() {
        System.out.println("[DAO v2 - WebService] Récupération dynamique depuis l'API distante...");
        return 150.0;
    }
}
