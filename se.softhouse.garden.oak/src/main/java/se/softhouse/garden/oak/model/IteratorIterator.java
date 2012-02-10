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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author mis
 * 
 */
public class IteratorIterator<E> implements Iterator<E> {

	private final List<Iterator<E>> iterator;
	private int current = 0;

	public IteratorIterator(List<Iterator<E>> iterators) {
		this.iterator = iterators;
	}

	@Override
	public boolean hasNext() {
		return this.iterator.get(this.iterator.size() - 1).hasNext();
	}

	@Override
	public E next() {
		if (this.current < this.iterator.size()) {
			if (this.iterator.get(this.current).hasNext()) {
				return this.iterator.get(this.current).next();
			}
			this.current++;
			return next();
		}
		throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		this.iterator.get(this.current).remove();
	}

}
