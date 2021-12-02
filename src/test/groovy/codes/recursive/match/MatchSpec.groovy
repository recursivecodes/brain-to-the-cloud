package codes.recursive.match

import codes.recursive.model.CallOfDuty
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

@MicronautTest
class MatchSpec extends Specification {
    void "test map lookup"() {
        given:
        String resolvedName = CallOfDuty.lookupMap(CallOfDuty.VANGUARD, "mp_berlin_01")

        expect:
        assert resolvedName == "Berlin"
    }
}
