package org.requirementsascode.predicate;

import java.io.Serializable;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModelRunner;

public class Anytime implements Predicate<UseCaseModelRunner>, Serializable {
    private static final long serialVersionUID = 7724607380865304333L;

    @Override
    public boolean test(UseCaseModelRunner t) {
	return true;
    }
}
