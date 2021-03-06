import API.APIManager;
import API.Models.Node;
import GUI.Frontend;
import GUI.Generator.DepthFirst;
import backend.Dijkstra;
import backend.SearchAlgorithm;

public class MainOld {

    public static void main(String[] args) {
        APIManager manager = new APIManager();
        Frontend frontend = new Frontend(manager);
        SearchAlgorithm backend = new Dijkstra(manager);
        manager.attachFrontend(frontend);
        manager.attachBackend(backend);
        int x = 10;
        int y = 10;
        DepthFirst a = new DepthFirst();
        Node[][] field = a.generateLabyrinth(x, y);
        frontend.setMatrix(field);
        manager.initMatrix(field);
        backend.run();

        // daten im apimanager nur an ein backend bzw. frontend schicken
        // backend starten
    }
}
