package grails.spock.example

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Brewery)
class BrewerySpec extends Specification {

    def "Brewery name constraints"() {
        setup:
        mockForConstraintsTests(Brewery)

        when:
        def brewery = new Brewery(name: name, description: "best brewery ever!", location: "Saint Paul, MN")
        brewery.validate()

        then:
        brewery.hasErrors() == !valid

        where:
        name                                                             | valid
        ""                                                               | false
        "  "                                                             | false
        null                                                             | false
        "some name"                                                      | true
        "a name that exceeds fifty characters is explicitly not allowed" | false
        "1234567890123456789012345678901234567890123456789"              | true  // 49 chars ok
        "12345678901234567890123456789012345678901234567890"             | true  // 50 chars ok
        "123456789012345678901234567890123456789012345678901"            | false // 51 chars not ok
    }

    def "Brewery name uniqueness constraints"() {
        setup:
        Brewery brewery = new Brewery(name: name, description: "some desc", location: "some location")
        mockForConstraintsTests(Brewery, [brewery])

        when:
        def brewery2 = new Brewery(name: name, description: "blah", location: "wherever") 
        brewery2.validate()

        then:
        brewery2.hasErrors() == !valid
        "unique" == brewery2.errors["name"]

        where:
        name          | valid
        "my brewery"  | false
        "My brewery"  | false
        "MY BREWERY"  | false
        "my brewery " | false
    }

    def "Brewery description constraints"() {
        setup:
        mockForConstraintsTests(Brewery)

        when:
        def brewery = new Brewery(name: "someName", description: desc, location: "location")
        brewery.validate()

        then:
        brewery.hasErrors() == !valid

        where:
        desc               | valid
        "some description" | true
        ""                 | true
        null               | true
    }

    def "Brewery location constraints"() {
        setup:
        mockForConstraintsTests(Brewery)

        when:
        def brewery = new Brewery(name: "someName", description: "someDesc", location: location)
        brewery.validate()

        then:
        brewery.hasErrors() == !valid

        where:
        location                                               | valid
        ""                                                     | false
        null                                                   | false
        "1234567890123456789012345678901234567890123456789"    | true  // 49 chars ok
        "12345678901234567890123456789012345678901234567890"   | true  // 50 chars ok
        "123456789012345678901234567890123456789012345678901"  | false // 51 chars not ok
    }


}