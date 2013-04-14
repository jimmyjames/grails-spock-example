package grails.spock.example

class Brewery {
    String name
    String location
    String description

    static constraints = {
        name(blank: false, unique: true, maxSize: 50)
        location(blank: false, maxSize: 50)
        description(blank: true, nullable: true, maxSize: 100)
    }

    static hasMany = [beers: Beer]
}
