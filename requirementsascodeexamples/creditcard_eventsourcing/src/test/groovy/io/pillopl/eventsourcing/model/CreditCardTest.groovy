package io.pillopl.eventsourcing.model

import spock.lang.Specification

class CreditCardTest extends Specification {

    CreditCard card = new CreditCard(UUID.randomUUID())

    def 'cannot reassign limit'() {
        given:
            card.assignLimit(100)
        when:
            card.assignLimit(200)
        then:
            thrown(IllegalStateException)

    }

    def 'can assign limit'() {
        when:
            card.assignLimit(200)
        then:
            card.availableLimit() == 200
    }

    def 'can withdraw'() {
        given:
            card.assignLimit(100)
        when:
            card.withdraw(50)
        then:
            card.availableLimit() == 50
    }

    def 'cannot withdraw when not enough money'() {
        given:
            card.assignLimit(100)
        when:
            card.withdraw(500)
        then:
            thrown(IllegalStateException)
    }

    def 'cannot withdraw when too many withdrawals in cycle'() {
        given:
            card.assignLimit(100)
        and:
            45.times {card.withdraw(1)}
        when:
            card.withdraw(1)
        then:
            thrown(IllegalStateException)
    }

    def 'can repay'() {
        given:
            card.assignLimit(100)
        and:
            card.withdraw(50)
        when:
            card.repay(40)
        then:
            card.availableLimit() == 90
    }

    def 'can withdraw in next cycle'() {
        given:
            card.assignLimit(100)
        and:
            45.times {card.withdraw(1)}
        and:
            card.cycleClosed()
        when:
            card.withdraw(1)
        then:
            card.availableLimit() == 54
    }
}

