package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class FeesMasterSchedule {

    FeesTypeInterval interval
    Integer sequenceNo
    Integer month


    static constraints = {
    }
}
