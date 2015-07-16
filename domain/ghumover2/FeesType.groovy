package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class FeesType {

    Long feesTypeId
    String feesType


    static mapping = { id generator: 'increment',name: 'feesTypeId'}
    static constraints = {
    }
}
