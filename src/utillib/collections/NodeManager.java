/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package utillib.collections;

import utillib.interfaces.IProperty;

import utillib.strings.StringUtil;

public class NodeManager extends Node {
	public NodeManager() {
		super("root");
	}

	/**
	 * 
	 * @param key
	 *            ex. root.sub1.sub1.propertyname
	 * @param defaultvalue
	 * @return
	 */
	public boolean getBool(String key, boolean defaultvalue) {
		final String[] SPLIT = StringUtil.split(key, '.');

		if(SPLIT != null && SPLIT.length > 0) {
			Node T_Node = super.getChild(SPLIT[0]);

			if(T_Node != null) {
				for(int X = 1; X < (SPLIT.length - 1); X++) {
					T_Node = T_Node.getChild(SPLIT[X]);
				}

				final IProperty PROP = T_Node.getProperty(SPLIT[SPLIT.length - 1]);

				if(PROP == null) {
					//Property Not Found
					return defaultvalue;
				} else {
					final Object OBJ = PROP.getVariable();

					if(OBJ instanceof Boolean) {
						return (OBJ == null ? defaultvalue : (boolean)(Boolean)OBJ);
					} else {
						return defaultvalue;
					}
				}
			}
		}

		return defaultvalue;
	}

	public byte getByte(String key, byte defaultvalue) {
		final String[] SPLIT = StringUtil.split(key, '.');

		if(SPLIT != null && SPLIT.length > 0) {
			Node T_Node = super.getChild(SPLIT[0]);

			if(T_Node != null) {
				for(int X = 1; X < (SPLIT.length - 1); X++) {
					T_Node = T_Node.getChild(SPLIT[X]);
				}

				final IProperty PROP = T_Node.getProperty(SPLIT[SPLIT.length - 1]);

				if(PROP == null) {
					//Property Not Found
					return defaultvalue;
				} else {
					final Object OBJ = PROP.getVariable();

					if(OBJ instanceof Byte) {
						return (OBJ == null ? defaultvalue : (byte)(Byte)OBJ);
					} else {
						return defaultvalue;
					}
				}
			}
		}

		return defaultvalue;
	}

	public int getInt(String key, int defaultvalue) {
		final String[] SPLIT = StringUtil.split(key, '.');

		if(SPLIT != null && SPLIT.length > 0) {
			Node T_Node = super.getChild(SPLIT[0]);

			if(T_Node != null) {
				for(int X = 1; X < (SPLIT.length - 1); X++) {
					T_Node = T_Node.getChild(SPLIT[X]);
				}

				final IProperty PROP = T_Node.getProperty(SPLIT[SPLIT.length - 1]);

				if(PROP == null) {
					//Property Not Found
					return defaultvalue;
				} else {
					final Object OBJ = PROP.getVariable();

					if(OBJ instanceof Integer) {
						return (OBJ == null ? defaultvalue : (int)(Integer)OBJ);
					} else {
						return defaultvalue;
					}
				}
			}
		}

		return defaultvalue;
	}

	public String getStr(String key, String defaultvalue) {
		final String[] SPLIT = StringUtil.split(key, '.');

		if(SPLIT != null && SPLIT.length > 0) {
			Node T_Node = super.getChild(SPLIT[0]);

			if(T_Node != null) {
				for(int X = 1; X < (SPLIT.length - 1); X++) {
					T_Node = T_Node.getChild(SPLIT[X]);
				}

				final IProperty PROP = T_Node.getProperty(SPLIT[SPLIT.length - 1]);

				if(PROP == null) {
					//Property Not Found
					return defaultvalue;
				} else {
					final Object OBJ = PROP.getVariable();

					if(OBJ instanceof String) {
						return (OBJ == null ? defaultvalue : (String)OBJ);
					} else {
						return defaultvalue;
					}
				}
			}
		}

		return defaultvalue;
	}

	public Object getObject(String key, Object defaultvalue) {
		final String[] SPLIT = StringUtil.split(key, '.');

		if(SPLIT != null && SPLIT.length > 0) {
			Node T_Node = super.getChild(SPLIT[0]);

			if(T_Node != null) {
				for(int X = 1; X < (SPLIT.length - 1); X++) {
					T_Node = T_Node.getChild(SPLIT[X]);
				}

				final IProperty PROP = T_Node.getProperty(SPLIT[SPLIT.length - 1]);

				if(PROP == null) {
					//Property Not Found
					return defaultvalue;
				} else {
					return PROP.getVariable();
				}
			}
		}

		return defaultvalue;
	}

	//Cats.Names.Like(boolean)
	public static void main(String[] args) {
		final NodeManager MANAGER = new NodeManager();

		Node T_Node = MANAGER.createChild("Cats");

		T_Node = T_Node.createChild("Moew");
		T_Node.addProperty(new DefaultProperty("like", true));

		T_Node = T_Node.createChild("Rat");
		T_Node.addProperty(new DefaultProperty("like", false));

		T_Node = T_Node.createChild("Bones");
		T_Node.addProperty(new DefaultProperty("like", true));

		T_Node = T_Node.createChild("Fluffy");
		T_Node.addProperty(new DefaultProperty("like", true));

		System.out.println("Like Moew: " + MANAGER.getBool("Cats.Moew.like", false));
	}
}
