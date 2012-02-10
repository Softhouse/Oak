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

import se.softhouse.garden.oak.DecisionEngine;
import se.softhouse.garden.oak.model.ARegister;
import se.softhouse.garden.oak.model.ARegisterPtr;

/**
 * @author Mikael Svahn
 * 
 */
public class InvokeTableStatement implements Statement {

	protected ARegisterPtr name;
	protected String tableName;

	public InvokeTableStatement() {
	}

	public InvokeTableStatement(ARegisterPtr name, String tableName) {
		this.name = name;
		this.tableName = tableName;
	}

	@Override
	public boolean execute(ARegister register) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(ARegister register, DecisionEngine actionEngine) {
		ARegister subreg = this.name == null ? register : register.getSubRegister(this.name);
		if (!this.tableName.isEmpty()) {
			actionEngine.execute(this.tableName, subreg);
		}
		return true;
	}

}
