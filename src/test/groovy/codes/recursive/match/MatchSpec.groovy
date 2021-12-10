package codes.recursive.match

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

@MicronautTest
class MatchSpec extends Specification {
    void "test map lookup"() {
        given:
        String foo = 'bar'

        expect:
        assert foo.reverse() == 'rab'
    }
}
