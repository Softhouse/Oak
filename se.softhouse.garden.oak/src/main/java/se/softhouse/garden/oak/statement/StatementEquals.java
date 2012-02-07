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

/**
 * @author Mikael Svahn
 * 
 */
public class StatementEquals extends AbstractStatement {

	private String name;
	private Object value;
	Transmorph converter = new Transmorph(new DefaultConverters());

	public StatementEquals() {
	}

	public StatementEquals(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public boolean execute(AMap doc, DecisionEngine actionEngine) {
		Object parameter = doc.getParameter(this.name);
		try {
			parameter = this.converter.convert(parameter, this.value.getClass());
		} catch (ConverterException e) {
			return false;
		}

		return this.value.equals(parameter);
	}

}
