package edu.berkeley.rest

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Unroll

@TestMixin(GrailsUnitTestMixin)
class QueryFilterSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    private QueryFilter backAgain(QueryFilter qf) {
        // extra check using JsonSlurper
        Map map = new JsonSlurper().parseText(qf.toJson())
        assert map == qf
        // convert back to a QueryFilter
        return QueryFilter.parseQueryFilter(qf.toJson())
    }

    @Unroll
    void "test filters"() {
        expect:
            expected == input

        where:
            input                                                       | expected
            backAgain(QueryFilter.equals("uid", "123"))                 | [left: "uid", operation: QueryFilter.EQUALS, right: "123"]
            backAgain(QueryFilter.notEquals("uid", "123"))              | [left: "uid", operation: QueryFilter.EQUALS, right: "123", not: true]
            backAgain(QueryFilter.lessThan("uid", "123"))               | [left: "uid", operation: QueryFilter.LESSTHAN, right: "123"]
            backAgain(QueryFilter.notLessThan("uid", "123"))            | [left: "uid", operation: QueryFilter.LESSTHAN, right: "123", not: true]
            backAgain(QueryFilter.greaterThan("uid", "123"))            | [left: "uid", operation: QueryFilter.GREATERTHAN, right: "123"]
            backAgain(QueryFilter.notGreaterThan("uid", "123"))         | [left: "uid", operation: QueryFilter.GREATERTHAN, right: "123", not: true]
            backAgain(QueryFilter.lessThanOrEquals("uid", "123"))       | [left: "uid", operation: QueryFilter.LESSTHANOREQUALS, right: "123"]
            backAgain(QueryFilter.notLessThanOrEquals("uid", "123"))    | [left: "uid", operation: QueryFilter.LESSTHANOREQUALS, right: "123", not: true]
            backAgain(QueryFilter.greaterThanOrEquals("uid", "123"))    | [left: "uid", operation: QueryFilter.GREATERTHANOREQUALS, right: "123"]
            backAgain(QueryFilter.notGreaterThanOrEquals("uid", "123")) | [left: "uid", operation: QueryFilter.GREATERTHANOREQUALS, right: "123", not: true]
    }

    @Unroll
    void "test and or filters"() {
        expect:
            expected == input

        where:
            input                                                                                                    | expected
            backAgain(QueryFilter.and(QueryFilter.equals("uid", "123"), QueryFilter.equals("givenName", "John")))    | [left: [left: "uid", operation: QueryFilter.EQUALS, right: "123"], operation: QueryFilter.AND, right: [left: "givenName", operation: QueryFilter.EQUALS, right: "John"]]
            backAgain(QueryFilter.notAnd(QueryFilter.equals("uid", "123"), QueryFilter.equals("givenName", "John"))) | [left: [left: "uid", operation: QueryFilter.EQUALS, right: "123"], operation: QueryFilter.AND, right: [left: "givenName", operation: QueryFilter.EQUALS, right: "John"], not: true]
            backAgain(QueryFilter.or(QueryFilter.equals("uid", "123"), QueryFilter.equals("givenName", "John")))     | [left: [left: "uid", operation: QueryFilter.EQUALS, right: "123"], operation: QueryFilter.OR, right: [left: "givenName", operation: QueryFilter.EQUALS, right: "John"]]
            backAgain(QueryFilter.notOr(QueryFilter.equals("uid", "123"), QueryFilter.equals("givenName", "John")))  | [left: [left: "uid", operation: QueryFilter.EQUALS, right: "123"], operation: QueryFilter.OR, right: [left: "givenName", operation: QueryFilter.EQUALS, right: "John"], not: true]

    }
}
