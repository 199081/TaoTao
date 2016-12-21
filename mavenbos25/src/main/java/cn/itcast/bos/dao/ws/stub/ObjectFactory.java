
package cn.itcast.bos.dao.ws.stub;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.itcast.bos.dao.ws.stub package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OperateCustomerResponse_QNAME = new QName("http://ws.itcast.cn/", "operateCustomerResponse");
    private final static QName _OperateCustomer_QNAME = new QName("http://ws.itcast.cn/", "operateCustomer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.itcast.bos.dao.ws.stub
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OperateCustomer }
     * 
     */
    public OperateCustomer createOperateCustomer() {
        return new OperateCustomer();
    }

    /**
     * Create an instance of {@link OperateCustomerResponse }
     * 
     */
    public OperateCustomerResponse createOperateCustomerResponse() {
        return new OperateCustomerResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperateCustomerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.itcast.cn/", name = "operateCustomerResponse")
    public JAXBElement<OperateCustomerResponse> createOperateCustomerResponse(OperateCustomerResponse value) {
        return new JAXBElement<OperateCustomerResponse>(_OperateCustomerResponse_QNAME, OperateCustomerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperateCustomer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.itcast.cn/", name = "operateCustomer")
    public JAXBElement<OperateCustomer> createOperateCustomer(OperateCustomer value) {
        return new JAXBElement<OperateCustomer>(_OperateCustomer_QNAME, OperateCustomer.class, null, value);
    }

}
