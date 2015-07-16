package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class FeesTypeInterval {

    Integer feesTypeIntervalId
    String feesTypeInterval
	Date scheduledDate
	Long school_id;
    static mapping = { id generator: 'increment',name: 'feesTypeIntervalId'}
    static constraints = {
    }
}
