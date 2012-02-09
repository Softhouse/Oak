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
import java.util.HashMap;
import java.util.Map;

import se.softhouse.garden.oak.Constants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Mikael Svahn
 * 
 */
public class ABasicMap implements AMap {

	private static final Gson gson;

	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ABasicMap.class, new ABasicMapSerializer());
		builder.registerTypeAdapter(ABasicList.class, new ABasicListSerializer());
		gson = builder.create();
	}

	protected Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public Object getParameter(AParameterName name) {
		String key = name.next();
		Object value = this.map.get(key);
		if (!name.hasMore()) {
			return value;
		} else if (value instanceof AMap) {
			return ((AMap) value).getParameter(name);
		}
		return null;
	}

	@Override
	public void setParameter(AParameterName name, Object value) {
		String key = name.next();
		if (!name.hasMore()) {
			this.map.put(key, value);
		} else {
			Object submap = this.map.get(key);
			if (submap instanceof AMap) {
				((AMap) submap).setParameter(name, value);
			} else if (submap == null) {
				submap = new ABasicMap();
				this.map.put(key, submap);
				((AMap) submap).setParameter(name, value);
			}
		}
	}

	@Override
	public AList createList(AParameterName name) {
		ABasicList list = new ABasicList();
		setParameter(name, list);
		return list;
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public Object getParameter(String name) {
		return getParameter(new AParameterName(name.split(Constants.PARAM_SEPARATOR)));
	}

	@Override
	public void setParameter(String name, Object value) {
		setParameter(new AParameterName(name.split(Constants.PARAM_SEPARATOR)), value);
	}

	@Override
	public String toString() {
		return gson.toJson(this.map);
	}

	private static class ABasicMapSerializer implements JsonSerializer<ABasicMap> {
		@Override
		public JsonElement serialize(ABasicMap src, Type typeOfSrc, JsonSerializationContext context) {
			return context.serialize(src.map);
		}
	}

	private static class ABasicListSerializer implements JsonSerializer<ABasicList> {
		@Override
		public JsonElement serialize(ABasicList src, Type typeOfSrc, JsonSerializationContext context) {
			return context.serialize(src.list);
		}
	}
}
