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

/**
 * @author mis
 * 
 */
public class ABasicSubRegister implements ARegister {

	private ARegister register;
	private ARegisterPtr ptr;

	public ABasicSubRegister(ARegister register, ARegisterPtr ptr) {
		this.register = register;
		this.ptr = ptr;
	}

	@Override
	public Object get(String name) {
		return this.register.get(this.ptr, new ABasicRegisterPtr(name));
	}

	@Override
	public Object get(ARegisterPtr ptr) {
		return this.register.get(this.ptr, ptr);
	}

	@Override
	public Object get(ARegisterPtr... ptrs) {
		return this.register.get(getPtrs(ptrs));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAs(String name, Class<T> klass) {
		return (T) get(name);
	}

	@Override
	public ARegister set(String name, Object value) {
		return set(new ABasicRegisterPtr(name), value);
	}

	@Override
	public ARegister set(ARegisterPtr ptr, Object value) {
		return this.register.set(value, this.ptr, ptr);
	}

	@Override
	public ARegister set(Object value, ARegisterPtr... ptrs) {
		return this.register.set(value, getPtrs(ptrs));
	}

	@Override
	public ARegister getSubRegister(ARegisterPtr name) {
		return new ABasicSubRegister(this, name);
	}

	private ARegisterPtr[] getPtrs(ARegisterPtr... ptrs) {
		ARegisterPtr[] nptrs = new ARegisterPtr[ptrs.length + 1];
		System.arraycopy(ptrs, 0, nptrs, 1, ptrs.length);
		nptrs[0] = this.ptr;
		return nptrs;
	}

	@Override
	public ARegister addListEntry(ARegisterPtr name) {
		return this.register.addListEntry(new ABasicRegisterPtr(this.ptr, name));
	}

	@Override
	public Object remove(String name) {
		return this.register.remove(this.ptr, new ABasicRegisterPtr(name));
	}

	@Override
	public Object remove(ARegisterPtr ptr) {
		return this.register.remove(this.ptr, ptr);
	}

	@Override
	public Object remove(ARegisterPtr... ptrs) {
		return this.register.remove(getPtrs(ptrs));
	}

	@Override
	public Object evaluate(Object value, ARegisterPtr... ptrs) {
		return this.register.evaluate(value, getPtrs(ptrs));
	}
}
