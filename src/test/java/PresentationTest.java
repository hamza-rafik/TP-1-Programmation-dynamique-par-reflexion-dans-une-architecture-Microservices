import dao.DaoImpl;
import dao.DaoImpl2;
import dao.IDao;
import metier.IMetier;
import metier.MetierImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import presentation.Presentation2;

public class PresentationTest {

    @Test
    public void testStaticInstantiation() {
        IDao dao = new DaoImpl();
        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);
        double resultat = metier.calcul();
        Assertions.assertEquals(200.0, resultat, 0.001);
    }

    @Test
    public void testDynamicInstantiationViaReflection() throws Exception {
        IDao dao = (IDao) Presentation2.instantiateClass("dao.DaoImpl");
        Assertions.assertNotNull(dao);
        Assertions.assertEquals(100.0, dao.getValue(), 0.001);

        IMetier metier = (IMetier) Presentation2.instantiateClass("metier.MetierImpl");
        Assertions.assertNotNull(metier);

        Presentation2.injectDependency(metier, "setDao", dao);
        double resultat = metier.calcul();
        Assertions.assertEquals(200.0, resultat, 0.001);
    }

    @Test
    public void testDynamicSwitchingImplementation() throws Exception {
        // Démonstration du découplage : changement d'implémentation sans toucher au métier
        IDao dao2 = (IDao) Presentation2.instantiateClass("dao.DaoImpl2");
        Assertions.assertNotNull(dao2);
        Assertions.assertEquals(150.0, dao2.getValue(), 0.001);

        IMetier metier = (IMetier) Presentation2.instantiateClass("metier.MetierImpl");
        Presentation2.injectDependency(metier, "setDao", dao2);

        double resultat = metier.calcul();
        Assertions.assertEquals(300.0, resultat, 0.001);
    }

    @Test
    public void testExceptionWhenDaoNotSet() {
        MetierImpl metier = new MetierImpl();
        Assertions.assertThrows(IllegalStateException.class, () -> {
            metier.calcul();
        });
    }
}
