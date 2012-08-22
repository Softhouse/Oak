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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author mis
 * 
 */
public class ABasicRegister implements ARegister {

	private static final Gson gson;

	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ABasicRegister.class, new ABasicRegisterSerializer());
		gson = builder.create();
	}

	protected Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public Object get(String name) {
		return get(new ABasicRegisterPtr(name));
	}

	@Override
	public Object get(ARegisterPtr ptr) {
		return getVar(this.map, ptr.getParts().iterator());
	}

	@Override
	public Object get(ARegisterPtr... ptrs) {
		List<Iterator<Object>> iterators = getIterators(ptrs);
		return getVar(this.map, new IteratorIterator<Object>(iterators));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAs(String name, Class<T> klass) {
		return (T) get(new ABasicRegisterPtr(name));
	}

	@Override
	public ARegister set(String name, Object value) {
		set(new ABasicRegisterPtr(name), value);
		return this;
	}

	@Override
	public ARegister set(ARegisterPtr ptr, Object value) {
		setVar(this.map, ptr.getParts().iterator(), value);
		return this;
	}

	@Override
	public ARegister set(Object value, ARegisterPtr... ptrs) {
		List<Iterator<Object>> iterators = getIterators(ptrs);
		setVar(this.map, new IteratorIterator<Object>(iterators), value);
		return this;
	}

	@Override
	public ARegister getSubRegister(ARegisterPtr name) {
		return new ABasicSubRegister(this, name);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ARegister addListEntry(ARegisterPtr... name) {
		List list = (List) get(name);
		if (list == null) {
			list = new ArrayList<Object>();
			set(list, name);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		list.add(map);
		ARegisterPtr[] nptrs = new ARegisterPtr[name.length + 1];
		System.arraycopy(name, 0, nptrs, 0, name.length);
		nptrs[name.length] = new ABasicRegisterPtr(list.size() - 1);
		return new ABasicSubRegister(this, new ABasicRegisterPtr(nptrs));
	}

	@SuppressWarnings("rawtypes")
	static Object getVar(Object parent, Iterator<Object> nameIterator) {
		Object result = parent;
		while (nameIterator.hasNext()) {
			Object key = nameIterator.next();
			if ("..".equals(key)) {
				return null;
			}
			if (parent instanceof Map) {
				result = getVar(((Map) parent).get(key), nameIterator);
			} else if (parent instanceof List) {
				Integer idx = key instanceof Integer ? (Integer) key : Integer.parseInt((String) key);
				result = getVar(((List) parent).get(idx), nameIterator);
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void setVar(Object parent, Iterator<Object> iterator, Object value) {
		while (iterator.hasNext()) {
			Object key = iterator.next();
			if ("..".equals(key)) {
				return;
			}
			if (parent instanceof Map) {
				if (iterator.hasNext()) {
					Object object = ((Map) parent).get(key);
					if (object == null) {
						object = new HashMap<String, Object>();
						((Map) parent).put(key, object);
					}
					setVar(object, iterator, value);
				} else {
					((Map) parent).put(key, value);
				}
			} else if (parent instanceof List) {
				Integer idx = key instanceof Integer ? (Integer) key : Integer.parseInt((String) key);
				if (iterator.hasNext()) {
					Object object = ((List) parent).get(idx);
					if (object != null) {
						setVar(object, iterator, value);
					}
				} else {
					((List) parent).set(idx, value);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	static Object removeVar(Object parent, Iterator<Object> nameIterator) {
		while (nameIterator.hasNext()) {
			Object key = nameIterator.next();
			if ("..".equals(key)) {
				return null;
			}
			if (nameIterator.hasNext()) {
				if (parent instanceof Map) {
					return removeVar(((Map) parent).get(key), nameIterator);
				} else if (parent instanceof List) {
					Integer idx = key instanceof Integer ? (Integer) key : Integer.parseInt((String) key);
					return removeVar(((List) parent).get(idx), nameIterator);
				}
			} else {
				if (parent instanceof Map) {
					return ((Map) parent).remove(key);
				} else if (parent instanceof List) {
					Integer idx = key instanceof Integer ? (Integer) key : Integer.parseInt((String) key);
					return ((List) parent).remove(idx);
				}
			}
		}
		return null;
	}

	private List<Iterator<Object>> getIterators(ARegisterPtr... ptrs) {
		List<Iterator<Object>> iterators = new ArrayList<Iterator<Object>>();
		for (int i = 0; i < ptrs.length; i++) {
			if (!ptrs[i].isRelative()) {
				iterators.clear();
			}
			iterators.add(ptrs[i].getParts().iterator());
		}
		return iterators;
	}

	@Override
	public String toString() {
		return gson.toJson(this.map);
	}

	private static class ABasicRegisterSerializer implements JsonSerializer<ABasicRegister> {
		@Override
		public JsonElement serialize(ABasicRegister src, Type typeOfSrc, JsonSerializationContext context) {
			return context.serialize(src.map);
		}
	}

	@Override
	public Object remove(String name) {
		return remove(new ABasicRegisterPtr(name));
	}

	@Override
	public Object remove(ARegisterPtr ptr) {
		return removeVar(this.map, ptr.getParts().iterator());
	}

	@Override
	public Object remove(ARegisterPtr... ptrs) {
		List<Iterator<Object>> iterators = getIterators(ptrs);
		return removeVar(this.map, new IteratorIterator<Object>(iterators));
	}

	@Override
	public Object evaluate(Object value, ARegisterPtr... ptrs) {
		if (value instanceof String) {
			String expr = (String) value;
			if (expr.startsWith("${") && expr.endsWith("}")) {
				expr = expr.substring(2, expr.length() - 1);
				if (ptrs.length == 0) {
					return get(expr);
				}
				ARegisterPtr[] nptrs = new ARegisterPtr[ptrs.length + 1];
				System.arraycopy(ptrs, 0, nptrs, 0, ptrs.length);
				nptrs[ptrs.length] = new ABasicRegisterPtr(expr);
				return get(nptrs);

			}
		}
		return value;
	}

	@Override
	public int getSize() {
		return getSize(this.map);
	}

	@SuppressWarnings("unchecked")
	static int getSize(Object container) {
		int size = 0;
		if (container instanceof Map) {
			size = ((Map<String, Object>) container).size();
			for (Object o : ((Map<String, Object>) container).values()) {
				size += getSize(o);
			}
		} else if (container instanceof List) {
			size = ((List<Object>) container).size();
			for (Object o : ((List<Object>) container)) {
				size += getSize(o);
			}
		}
		return size;
	}

	@Override
	public void removeAll() {
		this.map.clear();
	}

}
