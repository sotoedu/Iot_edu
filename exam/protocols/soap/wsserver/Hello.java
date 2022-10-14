package wsserver;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class Hello {
	public String sayHello(String name) {
		return "Hello " + name;
	}
}