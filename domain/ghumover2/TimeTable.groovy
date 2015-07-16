package ghumover2

import grails.rest.Resource


@Resource(formats=['json', 'xml'])
class TimeTable {

    Subject subject
    String day

    String startTime
    String endTime
	int seq
    static belongsTo = [grade:Grade , teacher:Teacher ]
    static constraints = {
      startTime(nullable: true)
      endTime(nullable: true)
	  seq(nullable: true)
    }
}
