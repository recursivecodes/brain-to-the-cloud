package codes.recursive;

import io.micronaut.runtime.Micronaut;
import oracle.security.pki.OraclePKIProvider;

import java.security.Security;

public class Application {

    public static void main(String[] args) {
        if (null == Security.getProvider("OraclePKI")) {
            Security.insertProviderAt(new OraclePKIProvider(), Integer.MAX_VALUE);
        }
        Micronaut.run(Application.class, args);
    }
}
