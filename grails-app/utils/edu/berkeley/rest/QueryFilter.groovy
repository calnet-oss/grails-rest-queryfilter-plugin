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

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

abstract class QueryFilter extends LinkedHashMap {

    String toJson() {
        return new JsonBuilder(this).toString()
    }

    static class LeftRightQueryFilter extends QueryFilter {
        LeftRightQueryFilter(String op, def left, def right, Boolean not = null) {
            this.operation = op
            this.left = left
            this.right = right
            if (not != null)
                this.not = not
        }
    }

    public static final String EQUALS = "EQUALS"
    public static final String LESSTHAN = "LESSTHAN"
    public static final String GREATERTHAN = "GREATERTHAN"
    public static final String LESSTHANOREQUALS = "LESSTHANOREQUALS"
    public static final String GREATERTHANOREQUALS = "GREATERTHANOREQUALS"
    public static final String AND = "AND"
    public static final String OR = "OR"

    static class Equals extends LeftRightQueryFilter {
        Equals(def left, def right, Boolean not = null) { super(EQUALS, left, right, not) }
    }

    static class LessThan extends LeftRightQueryFilter {
        LessThan(def left, def right, Boolean not = null) { super(LESSTHAN, left, right, not) }
    }

    static class GreaterThan extends LeftRightQueryFilter {
        GreaterThan(def left, def right, Boolean not = null) { super(GREATERTHAN, left, right, not) }
    }

    static class LessThanOrEquals extends LeftRightQueryFilter {
        LessThanOrEquals(def left, def right, Boolean not = null) { super(LESSTHANOREQUALS, left, right, not) }
    }

    static class GreaterThanOrEquals extends LeftRightQueryFilter {
        GreaterThanOrEquals(def left, def right, Boolean not = null) { super(GREATERTHANOREQUALS, left, right, not) }
    }

    static class And extends LeftRightQueryFilter {
        And(Map left, Map right, Boolean not = null) { super(AND, left, right, not) }
    }

    static class Or extends LeftRightQueryFilter {
        Or(Map left, Map right, Boolean not = null) { super(OR, left, right, not) }
    }

    public static QueryFilter equals(def left, def right) { return new Equals(left, right) }

    public static QueryFilter notEquals(def left, def right) { return new Equals(left, right, true) }

    public static QueryFilter lessThan(def left, def right) { return new LessThan(left, right) }

    public static QueryFilter notLessThan(def left, def right) { return new LessThan(left, right, true) }

    public static QueryFilter greaterThan(def left, def right) { return new GreaterThan(left, right) }

    public static QueryFilter notGreaterThan(def left, def right) { return new GreaterThan(left, right, true) }

    public static QueryFilter lessThanOrEquals(def left, def right) { return new LessThanOrEquals(left, right) }

    public static QueryFilter notLessThanOrEquals(def left, def right) { return new LessThanOrEquals(left, right, true) }

    public static QueryFilter greaterThanOrEquals(def left, def right) { return new GreaterThanOrEquals(left, right) }

    public static QueryFilter notGreaterThanOrEquals(def left, def right) { return new GreaterThanOrEquals(left, right, true) }

    public static QueryFilter and(Map left, Map right) { return new And(left, right) }

    public static QueryFilter notAnd(Map left, Map right) { return new And(left, right, true) }

    public static QueryFilter or(Map left, Map right) { return new Or(left, right) }

    public static QueryFilter notOr(Map left, Map right) { return new Or(left, right, true) }

    public static QueryFilter parseQueryFilter(String qf) {
        return parseQueryFilter(new JsonSlurper().parseText(qf))
    }

    public static QueryFilter parseQueryFilter(Map qf) {
        if (!qf?.operation)
            throw new RuntimeException("'operation' is a required key in the queryFilter map")
        if (!qf?.left)
            throw new RuntimeException("'left' is a required key in the queryFilter map")
        if (!qf?.right)
            throw new RuntimeException("'left' is a required key in the queryFilter map")

        String op = qf.operation

        QueryFilter filter = null
        if (op == EQUALS) filter = ifnot(qf.not, equals(qf.left, qf.right))
        else if (op == LESSTHAN) filter = ifnot(qf.not, lessThan(qf.left, qf.right))
        else if (op == GREATERTHAN) filter = ifnot(qf.not, greaterThan(qf.left, qf.right))
        else if (op == LESSTHANOREQUALS) filter = ifnot(qf.not, lessThanOrEquals(qf.left, qf.right))
        else if (op == GREATERTHANOREQUALS) filter = ifnot(qf.not, greaterThanOrEquals(qf.left, qf.right))
        else if (op == AND) filter = ifnot(qf.not, and(qf.left, qf.right))
        else if (op == OR) filter = ifnot(qf.not, or(qf.left, qf.right))
        else
            throw new RuntimeException("Unsupported operation: " + op)

        return filter
    }

    private static QueryFilter ifnot(Boolean isNot, QueryFilter filter) {
        if (isNot) {
            filter.not = true
        }
        return filter
    }
}
