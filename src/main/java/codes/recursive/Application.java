package codes.recursive;

import io.micronaut.runtime.Micronaut;
import oracle.security.pki.OraclePKIProvider;

import java.security.Security;

public class Application {

    public static void main(String[] args) {
        // prevent issue with ATP wallet that causes HttpsUrlConnection to fail
        if (null == Security.getProvider("OraclePKI")) {
            Security.insertProviderAt(new OraclePKIProvider(), Integer.MAX_VALUE);
        }
        System.setProperty("oracle.jdbc.fanEnabled", "false");
        Micronaut.run(Application.class, args);
    }
}
