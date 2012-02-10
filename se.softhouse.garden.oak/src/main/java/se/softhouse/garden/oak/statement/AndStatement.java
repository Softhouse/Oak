/**
 * Copyright (c) 2011, Mikael Svahn, Softhouse Consulting AB
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package se.softhouse.garden.oak.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.softhouse.garden.oak.DecisionEngine;
import se.softhouse.garden.oak.model.ARegister;

/**
 * @author Mikael Svahn
 * 
 */
public class AndStatement extends AbstractStatement {

	private List<Statement> statements;

	public AndStatement() {
		this.statements = new ArrayList<Statement>();
	}

	public AndStatement(Statement... conditions) {
		this.statements = new ArrayList<Statement>();
		this.statements.addAll(Arrays.asList(conditions));
	}

	public AndStatement(List<Statement> statements) {
		this.statements = statements;
	}

	public void addStatement(Statement condition) {
		this.statements.add(condition);
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}

	@Override
	public boolean execute(ARegister register, DecisionEngine actionEngine) {
		for (Statement condition : this.statements) {
			if (!condition.execute(register, actionEngine)) {
				return false;
			}
		}
		return true;
	}

}
