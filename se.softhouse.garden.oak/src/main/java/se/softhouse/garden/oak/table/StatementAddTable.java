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

import java.util.List;

import se.softhouse.garden.oak.DecisionEngine;
import se.softhouse.garden.oak.model.AList;
import se.softhouse.garden.oak.model.AMap;
import se.softhouse.garden.oak.model.AParameterName;
import se.softhouse.garden.oak.statement.Statement;

/**
 * @author Mikael Svahn
 * 
 */
public class StatementAddTable extends StatementTable {

	protected String[] name;

	public StatementAddTable() {
	}

	public StatementAddTable(String[] name) {
		this.name = name;
	}

	public StatementAddTable(String[] name, List<Statement> conditions) {
		this.name = name;
		this.statements = conditions;
	}

	@Override
	public ActionRows execute(AMap map, ActionRows rows, DecisionEngine actionEngine) {
		AList list = (AList) map.getParameter(new AParameterName(this.name));
		if (list == null) {
			list = map.createList(new AParameterName(this.name));
		}

		ActionRows resultRows = new ActionRows();
		resultRows.empty();
		for (int i = 0; i < this.statements.size(); i++) {
			if (rows.contains(i)) {
				AMap submap = list.create();
				if (this.statements.get(i).execute(submap, actionEngine)) {
					resultRows.add(i);
				}
				if (submap.size() > 0) {
					list.add(submap);
				}
			}
		}
		return resultRows;
	}
}
