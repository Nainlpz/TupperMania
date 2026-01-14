package segundo.dam.tuppermania;

import org.junit.jupiter.api.Test;
import segundo.dam.tuppermania.controller.HomeController;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerTest {

    @Test
    void testHomeDevuelveVistaIndex() {
        HomeController controller = new HomeController();
        String vista = controller.home();
        assertEquals("index", vista, "La ruta /home debería devolver la plantilla 'index'");
    }
}