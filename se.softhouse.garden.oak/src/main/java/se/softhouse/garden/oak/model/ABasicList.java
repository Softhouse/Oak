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

package se.softhouse.garden.oak.model;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Mikael Svahn
 * 
 */
public class ABasicList implements AList {

	protected List<AMap> list = new ArrayList<AMap>();

	public ABasicList() {
	}

	@Override
	public AMap add() {
		ABasicMap map = new ABasicMap();
		this.list.add(map);
		return map;
	}

	@Override
	public List<AMap> asList() {
		return this.list;
	}

}
