package cn.itcast.bos.dao.ws.stub;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.15
 * 2016-02-22T11:44:22.826+08:00
 * Generated source version: 2.6.15
 * 
 */
@WebServiceClient(name = "CRMService", 
                  wsdlLocation = "http://localhost:8888/mavencrm25/ws/CustomerWS?wsdl",
                  targetNamespace = "http://ws.itcast.cn/") 
public class CRMService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ws.itcast.cn/", "CRMService");
    public final static QName CRMServicePort = new QName("http://ws.itcast.cn/", "CRMServicePort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8888/mavencrm25/ws/CustomerWS?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(CRMService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8888/mavencrm25/ws/CustomerWS?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public CRMService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CRMService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CRMService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CRMService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CRMService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CRMService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns CustomerService
     */
    @WebEndpoint(name = "CRMServicePort")
    public CustomerService getCRMServicePort() {
        return super.getPort(CRMServicePort, CustomerService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CustomerService
     */
    @WebEndpoint(name = "CRMServicePort")
    public CustomerService getCRMServicePort(WebServiceFeature... features) {
        return super.getPort(CRMServicePort, CustomerService.class, features);
    }

}
