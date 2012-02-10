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
import java.util.Arrays;
import java.util.List;

/**
 * @author mis
 * 
 */
public class ABasicRegisterPtr implements ARegisterPtr {

	private List<Object> names;

	public ABasicRegisterPtr(Integer index) {
		this.names = new ArrayList<Object>();
		this.names.add(index);
	}

	public ABasicRegisterPtr(String path) {
		this.names = parse(path);
	}

	public ABasicRegisterPtr(ARegisterPtr... ptrs) {
		this.names = new ArrayList<Object>();
		for (ARegisterPtr ptr : ptrs) {
			this.names.addAll(ptr.getParts());
		}
	}

	@Override
	public List<Object> getParts() {
		return this.names;
	}

	private List<Object> parse(String path) {
		List<Object> list = new ArrayList<Object>();
		if (path != null) {
			String[] pe = path.split("/");
			if (pe.length > 0) {
				list.addAll(Arrays.asList(pe));
			}
		}
		return list;
	}

	@Override
	public boolean isRelative() {
		if (this.names.size() > 0) {
			Object o = this.names.get(0);
			return !(o instanceof String && ((String) o).isEmpty());
		}
		return true;
	}

}
