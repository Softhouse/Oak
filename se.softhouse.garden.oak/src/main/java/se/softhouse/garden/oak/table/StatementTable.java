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

package se.softhouse.garden.oak.table;

import java.util.ArrayList;
import java.util.List;

import se.softhouse.garden.oak.DecisionEngine;
import se.softhouse.garden.oak.model.ARegister;
import se.softhouse.garden.oak.statement.Statement;
import se.softhouse.garden.oak.statement.AndStatement;

/**
 * @author Mikael Svahn
 * 
 */
public class StatementTable implements ActionTable {

	protected List<Statement> statements;
	protected boolean multi;

	public StatementTable() {
		this.statements = new ArrayList<Statement>();
	}

	public StatementTable(boolean multi, List<Statement> conditions) {
		this.statements = conditions;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public void setStatements(List<Statement> conditions) {
		this.statements = conditions;
	}

	@Override
	public ActionRows execute(ARegister register, ActionRows rows, DecisionEngine actionEngine) {
		ActionRows resultRows = new ActionRows();
		resultRows.empty();
		for (int i = 0; i < this.statements.size(); i++) {
			if (rows.contains(i) && this.statements.get(i).execute(register, actionEngine)) {
				resultRows.add(i);
				if (!this.multi) {
					return resultRows;
				}
			}
		}
		return resultRows;
	}

	public void addStatement(AndStatement stmt) {
		this.statements.add(stmt);
	}

}
