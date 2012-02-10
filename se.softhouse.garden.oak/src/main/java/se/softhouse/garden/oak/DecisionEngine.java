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

package se.softhouse.garden.oak;

import java.util.HashMap;
import java.util.Map;

import se.softhouse.garden.oak.model.ARegister;
import se.softhouse.garden.oak.table.DecisionTable;

import com.google.gson.Gson;

/**
 * @author Mikael Svahn
 * 
 */
public class DecisionEngine {

	protected Map<String, DecisionTable> sets = new HashMap<String, DecisionTable>();

	public void addDecisionTable(String name, DecisionTable set) {
		this.sets.put(name, set);
	}

	public boolean execute(String tableSetName, ARegister register) {
		DecisionTable tableSet = this.sets.get(tableSetName);
		if (tableSet != null) {
			tableSet.execute(register, this);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
