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

import se.softhouse.garden.oak.statement.Statement;
import se.softhouse.garden.oak.statement.StatementAnd;
import se.softhouse.garden.oak.statement.StatementCompare;
import se.softhouse.garden.oak.statement.StatementCompare.OP;
import se.softhouse.garden.oak.statement.StatementEquals;

/**
 * @author Mikael Svahn
 * 
 */
public class DC {

	public static StatementAnd AND(Statement... conditions) {
		return new StatementAnd(conditions);
	}

	public static StatementEquals EQUALS(String name, Object value) {
		return new StatementEquals(createName(name), value);
	}

	public static StatementCompare LT(String name, Number value) {
		return new StatementCompare(createName(name), value, OP.LT);
	}

	public static StatementCompare LE(String name, Number value) {
		return new StatementCompare(createName(name), value, OP.LE);
	}

	public static StatementCompare EQ(String name, Number value) {
		return new StatementCompare(createName(name), value, OP.EQ);
	}

	public static StatementCompare GE(String name, Number value) {
		return new StatementCompare(createName(name), value, OP.GE);
	}

	public static StatementCompare GT(String name, Number value) {
		return new StatementCompare(createName(name), value, OP.GT);
	}

	private static String[] createName(String name) {
		return name.split("/");
	}
}
