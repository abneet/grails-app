package ghumover2

import grails.rest.Resource
import org.grails.databinding.BindingFormat
@Resource(formats=['json', 'xml'])
class FeePaid {

    Long feePaidId
    static belongsTo = [ student:Student , classFees:ClassFees ]
    Integer amountPaid
    @BindingFormat("dd-MM-yyyy")
    Date date

    static mapping = {
        id generator: 'increment',name: 'feePaidId'
    }


    static constraints = {


    }
}
