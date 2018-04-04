/*
 * Copyright (c) 2016, Regents of the University of California and
 * contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.berkeley.rest

import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Unroll

class QueryFilterSpec extends Specification {

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
