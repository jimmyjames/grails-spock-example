package grails.spock.example

class Beer {

    String name
    String description

    static belongsTo = [brewery: Brewery]
}
