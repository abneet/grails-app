package ghumover2
import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class Address {
    String address
    String place
    String landmark
    String landline
    static constraints = {
        landline(nullable: true)
        address(nullable: true)
        landmark(nullable: true)
        place(nullable: true)
    }
}
