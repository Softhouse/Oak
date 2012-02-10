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

import se.softhouse.garden.oak.model.ABasicRegisterPtr;
import se.softhouse.garden.oak.model.ARegisterPtr;
import se.softhouse.garden.oak.statement.Statement;
import se.softhouse.garden.oak.statement.AndStatement;
import se.softhouse.garden.oak.statement.CompareStatement;
import se.softhouse.garden.oak.statement.CompareStatement.OP;
import se.softhouse.garden.oak.statement.EqualsStatement;

/**
 * @author Mikael Svahn
 * 
 */
public class DC {

	public static AndStatement AND(Statement... conditions) {
		return new AndStatement(conditions);
	}

	public static EqualsStatement EQUALS(String name, Object value) {
		return new EqualsStatement(createName(name), value);
	}

	public static CompareStatement LT(String name, Number value) {
		return new CompareStatement(createName(name), value, OP.LT);
	}

	public static CompareStatement LE(String name, Number value) {
		return new CompareStatement(createName(name), value, OP.LE);
	}

	public static CompareStatement EQ(String name, Number value) {
		return new CompareStatement(createName(name), value, OP.EQ);
	}

	public static CompareStatement GE(String name, Number value) {
		return new CompareStatement(createName(name), value, OP.GE);
	}

	public static CompareStatement GT(String name, Number value) {
		return new CompareStatement(createName(name), value, OP.GT);
	}

	private static ARegisterPtr createName(String name) {
		return new ABasicRegisterPtr(name);
	}
}
