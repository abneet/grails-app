package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class ClassFees {


       static belongsTo = [schoolClass:SchoolClass]
       FeesType type
       Long totalFee
       FeesSchedule feeSchedule
       static hasMany = [paidFees : FeePaid]



    static constraints = {
        feeSchedule(nullable: true)
    }
}
