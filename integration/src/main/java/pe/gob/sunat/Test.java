package pe.gob.sunat;

import pe.gob.sunat.servicio.registro.comppago.factura.gem.service.BillService;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by carlos on 14/03/2017.
 */
public class Test {

    public static void main() throws MalformedURLException {
        // Static use case
        BillService service = new BillService();
        pe.gob.sunat.service.BillService billServicePort = service.getBillServicePort();

        billServicePort.sendBill("", null);

        // change url
        BindingProvider bp = (BindingProvider) billServicePort;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "endpoint url");
        billServicePort.sendBill("", null);





        // Dinamic use case
        Service service1 = Service.create(new URL(""), new QName(""));
        pe.gob.sunat.service.BillService billServicePort1 = service1.getPort(pe.gob.sunat.service.BillService.class);
        billServicePort1.sendBill("", null);
    }
}
