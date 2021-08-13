package contracts.chassis

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label "list_all_chassis"
    description "should return a list of all chassis items"

    request {
        url "/api/v1/chassis"
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body([
                name: $(anyNonBlankString()),
                description: $(anyNonBlankString())
        ])
    }
}
