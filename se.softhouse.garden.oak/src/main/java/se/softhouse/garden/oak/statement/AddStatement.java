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

import java.math.BigDecimal;

import net.entropysoft.transmorph.ConverterException;
import net.entropysoft.transmorph.DefaultConverters;
import net.entropysoft.transmorph.Transmorph;
import se.softhouse.garden.oak.DecisionEngine;
import se.softhouse.garden.oak.model.ARegister;
import se.softhouse.garden.oak.model.ARegisterPtr;

/**
 * @author Mikael Svahn
 * 
 */
public class AddStatement extends AbstractStatement {

	protected ARegisterPtr name;
	protected Object value;
	Transmorph converter = new Transmorph(new DefaultConverters());

	public AddStatement() {
	}

	public AddStatement(ARegisterPtr name, Object value) {
		this.name = name;
		this.value = value;
	}

	public void setKey(ARegisterPtr name) {
		this.name = name;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public boolean execute(ARegister register, DecisionEngine actionEngine) {
		Object parameter = register.get(this.name);
		if (parameter == null) {
			return false;
		}
		Object val = register.evaluate(this.value);
		if (val != null) {
			try {
				parameter = this.converter.convert(parameter, val.getClass());

				if (parameter instanceof Byte) {
					parameter = ((Byte) parameter) + ((Byte) val);
				} else if (parameter instanceof Short) {
					parameter = ((Short) parameter) + ((Short) val);
				} else if (parameter instanceof Integer) {
					parameter = ((Integer) parameter) + ((Integer) val);
				} else if (parameter instanceof Long) {
					parameter = ((Long) parameter) + ((Long) val);
				} else if (parameter instanceof Float) {
					parameter = ((Float) parameter) + ((Float) val);
				} else if (parameter instanceof Double) {
					parameter = ((Double) parameter) + ((Double) val);
				} else if (parameter instanceof BigDecimal) {
					parameter = ((BigDecimal) parameter).add((BigDecimal) val);
				} else if (parameter instanceof String) {
					parameter = ((String) parameter) + ((String) val);
				}
			} catch (ConverterException e) {
			}
		}

		register.set(this.name, parameter);
		return true;
	}
}
