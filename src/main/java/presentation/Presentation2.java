package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Moteur de chargement dynamique par réflexion.
 * Simule un conteneur IoC (Inversion of Control) / Injection de Dépendances.
 */
public class Presentation2 {

    public static void main(String[] args) {
        try {
            System.out.println("==========================================================================");
            System.out.println("   TP 1 : CHARGEMENT DYNAMIQUE PAR REFLEXION (ARCHITECTURE MICROSERVICES) ");
            System.out.println("==========================================================================");

            // 1. Charger les noms de classes depuis le fichier de configuration
            String[] classNames = loadClassNames("config.txt");
            System.out.println("[CONFIG] Configuration chargée depuis config.txt :");
            System.out.println("   -> DAO    : " + classNames[0]);
            System.out.println("   -> METIER : " + classNames[1]);

            // 2. Instancier le DAO dynamiquement
            IDao dao = (IDao) instantiateClass(classNames[0]);
            System.out.println("[CONTAINER] DAO instancié : " + dao.getClass().getName());

            // 3. Instancier le Métier dynamiquement
            IMetier metier = (IMetier) instantiateClass(classNames[1]);
            System.out.println("[CONTAINER] Métier instancié : " + metier.getClass().getName());

            // 4. Injecter le DAO dans le Métier via la réflexion
            System.out.println("[CONTAINER] Injection de la dépendance IDao via setDao()...");
            injectDependency(metier, "setDao", dao);

            // 5. Exécuter le calcul métier
            System.out.println("[APPLICATION] Exécution de la méthode metier.calcul()...");
            double resultat = metier.calcul();
            System.out.println("Résultats = " + resultat);

            System.out.println();
            System.out.println("+======================================================================+");
            System.out.println("|   [SUCCES] CHARGEMENT DYNAMIQUE & INJECTION REUSSIES SANS ERREUR !   |");
            System.out.println("+======================================================================+");

        } catch (Exception e) {
            System.err.println("[ERREUR] Échec du chargement dynamique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lit les noms de classes depuis un fichier de configuration.
     * Supporte la lecture depuis le chemin relatif direct ou le Classpath.
     */
    public static String[] loadClassNames(String fileName) throws IOException {
        String[] classNames = new String[2];
        File file = new File(fileName);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                for (int i = 0; i < 2; i++) {
                    String line = reader.readLine();
                    classNames[i] = (line != null) ? line.trim() : null;
                }
            }
        } else {
            InputStream is = Presentation2.class.getClassLoader().getResourceAsStream(fileName);
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    for (int i = 0; i < 2; i++) {
                        String line = reader.readLine();
                        classNames[i] = (line != null) ? line.trim() : null;
                    }
                }
            } else {
                throw new IOException("Fichier de configuration introuvable : " + fileName);
            }
        }
        return classNames;
    }

    /**
     * Instancie dynamiquement une classe à partir de son nom complet.
     */
    public static Object instantiateClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className.trim());
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return constructor.newInstance();
    }

    /**
     * Injecte une dépendance dans un objet cible via une méthode setter (méthode de réflexion).
     */
    public static void injectDependency(Object target, String setterName, Object dependency) throws Exception {
        Class<?> targetClass = target.getClass();
        // Récupérer le type d'interface implémenté par la dépendance
        Class<?> interfaceType = dependency.getClass().getInterfaces()[0];
        Method setterMethod = targetClass.getMethod(setterName, interfaceType);
        setterMethod.invoke(target, dependency);
    }
}
