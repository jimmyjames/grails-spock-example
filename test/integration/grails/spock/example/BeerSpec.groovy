package grails.spock.example

import grails.plugin.spock.IntegrationSpec

class BeerSpec extends IntegrationSpec {

	def setup() {
	}

	def cleanup() {
	}

	def "Beers can be added to a brewery"() {
		setup:
		Brewery brewery = new Brewery(name: "my brewery", description: "some desc", location: "my basement")
		Beer beer1 = new Beer(name: "first homebrew", description: "not bad!")
		Beer beer2 = new Beer(name: "second homebrew", description: "a little better")
		Beer beer3 = new Beer(name: "third homebrew", description: "nailed it!")
		
		when:
		brewery.addToBeers(beer1)
		brewery.addToBeers(beer2)
		brewery.addToBeers(beer3)
		brewery.save()

		then:
		def foundBrewery = Brewery.get(brewery.id)
		foundBrewery.beers.size() == 3
		def beers = foundBrewery.beers.collect {
			it.name
		}
		
		["first homebrew", "second homebrew", "third homebrew"] == beers.sort()
	}

	def "Beer requires a Brewery"() {
		when:
		Beer beer = new Beer(name: "brew", description: "description")
		beer.save()

		then: 
		beer.hasErrors() == true
		"nullable" == beer.errors['brewery'].code
	}

	def "Beers can look up their brewery"() {
		setup:
		Brewery brewery1 = new Brewery(name: "my brewery", description: "some desc", location: "my basement")
		Brewery brewery2 = new Brewery(name: "your brewery", description: "some desc", location: "your basement")
		Beer beer1 = new Beer(name: "my homebrew 1", description: "it's ok")
		Beer beer2 = new Beer(name: "my homebrew 2", description: "not bad")
		Beer beer3 = new Beer(name: "your homebrew", description: "terrible!")
		
		when:
		brewery1.addToBeers(beer1)
		brewery1.addToBeers(beer2)
		brewery2.addToBeers(beer3)
		brewery1.save()
		brewery2.save()

		then:
		Beer.get(beer1.id).brewery.name == "my brewery"
		Beer.get(beer2.id).brewery.name == "my brewery"
		Beer.get(beer3.id).brewery.name == "your brewery"
	}

}