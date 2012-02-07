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
import se.softhouse.garden.oak.model.AMap;
import net.entropysoft.transmorph.ConverterException;
import net.entropysoft.transmorph.DefaultConverters;
import net.entropysoft.transmorph.Transmorph;
import net.entropysoft.transmorph.utils.NumberComparator;

/**
 * @author Mikael Svahn
 * 
 */
public class StatementCompare extends AbstractStatement {

	public enum OP {
		LT, LE, EQ, GE, GT
	};

	private String name;
	private Number value;
	private NumberComparator nc = new NumberComparator();
	private OP op;
	Transmorph converter = new Transmorph(new DefaultConverters());

	public StatementCompare() {
	}

	public StatementCompare(String name, Number value, OP op) {
		this.name = name;
		this.value = value;
		this.op = op;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	@Override
	public boolean execute(AMap doc, DecisionEngine actionEngine) {
		Object parameter = doc.getParameter(this.name);
		Number n = null;
		if (parameter instanceof Number) {
			n = (Number) parameter;
		} else {
			try {
				n = this.converter.convert(parameter, Number.class);
			} catch (ConverterException e) {
				return false;
			}
		}

		switch (this.op) {
			case LT:
				return this.nc.compare(n, this.value) < 0;
			case LE:
				return this.nc.compare(n, this.value) <= 0;
			case EQ:
				return this.nc.compare(n, this.value) == 0;
			case GE:
				return this.nc.compare(n, this.value) >= 0;
			case GT:
				return this.nc.compare(n, this.value) > 0;
			default:
				return false;
		}
	}
}
