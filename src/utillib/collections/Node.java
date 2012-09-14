package utillib.collections;

import utillib.arrays.ResizingArray;

import utillib.interfaces.IProperty;

/**
 * 
 * @author Justin Palinkas
 */
public class Node {
	private final boolean _MATCH_CASE;

	private final String _NAME;

	private final ResizingArray<Node> _CHILD_NODES = new ResizingArray<Node>();
	private final ResizingArray<IProperty> _PROPERTIES = new ResizingArray<IProperty>();

	public Node() {
		this("root", false);
	}

	public Node(String rootname) {
		this(rootname, false);
	}

	public Node(String rootname, boolean matchcase) {
		if(rootname == null) {
			throw new RuntimeException("Variable[key] - Is Null");
		}

		_NAME = rootname;
		_MATCH_CASE = matchcase;
	}

	public String getName() {
		return _NAME;
	}

	public Node createChild(String name) {
		if(getChild(name) == null) {
			Node Title = new Node(name);

			_CHILD_NODES.put(Title);

			return Title;
		}

		return null;
	}

	public Node getChild(int index) {
		return _CHILD_NODES.getAt(index);
	}

	public Node getChild(String name) {
		for(int X = 0; X < _CHILD_NODES.length(); X++) {
			if(match(name, _CHILD_NODES.getAt(X)._NAME)) {
				return _CHILD_NODES.getAt(X);
			}
		}

		return null;
	}

	public void removeChild(String name) {
		for(int X = 0; X < _CHILD_NODES.length(); X++) {
			if(match(name, _CHILD_NODES.getAt(X)._NAME)) {
				_CHILD_NODES.removeAt(X);
				break;
			}
		}
	}

	public boolean childExists(Node title) {
		return childExists(title._NAME);
	}

	public boolean childExists(String name) {
		for(int X = 0; X < _CHILD_NODES.length(); X++) {
			if(match(name, _CHILD_NODES.getAt(X)._NAME)) {
				return true;
			}
		}

		return false;
	}

	public int childernCount() {
		return _CHILD_NODES.length();
	}

	public void addProperty(IProperty property) {
		if(!propertyExists(property)) {
			_PROPERTIES.put(property);
		}
	}

	public void setProperty(IProperty property) {
		final IProperty PROP = getProperty(property.getId());

		if(PROP != null) {
			PROP.setVariable(property.getVariable());
		} else {
			_PROPERTIES.put(property);
		}
	}

	public void addProperties(String name, IProperty[] properties) {
		for(int X = 0; X < properties.length; X++) {
			addProperty(properties[X]);
		}
	}

	public IProperty getProperty(int index) {
		return _PROPERTIES.getAt(index);
	}

	public IProperty getProperty(String name) {
		for(int X = 0; X < _PROPERTIES.length(); X++) {
			if(match(name, _PROPERTIES.getAt(X).getId())) {
				return _PROPERTIES.getAt(X);
			}
		}

		return null;
	}

	//Used To Set A Property's Variable
	public void setObject(String name, Object value) {
		final IProperty PROP = getProperty(name);

		if(PROP != null) {
			PROP.setVariable(value);
		}
	}

	public void removeProperty(String key) {
		for(int X = 0; X < _PROPERTIES.length(); X++) {
			if(_PROPERTIES.getAt(X).getId().equals(key)) {
				_PROPERTIES.removeAt(X);
				break;
			}
		}
	}

	public boolean propertyExists(IProperty property) {
		return propertyExists(property.getId());
	}

	public boolean propertyExists(String propertyname) {
		for(int X = 0; X < _PROPERTIES.length(); X++) {
			if(match(propertyname, _PROPERTIES.getAt(X).getId())) {
				return true;
			}
		}

		return false;
	}

	public int propertyCount() {
		return _PROPERTIES.length();
	}

//    private static String[] splitKey(String key) {
//        final ResizingArray<String> RESULTS = new ResizingArray<String>();
//        final MyStringBuffer BUFFER = new MyStringBuffer();
//
//        for(int X = 0; X < key.length(); X++) {
//            if(key.charAt(X) == '.') {
//                RESULTS.put(BUFFER.toString());
//                BUFFER.reset();
//            } else {
//                BUFFER.append(key.charAt(X));
//            }
//        }
//
//        return (RESULTS.length() > 0 ? RESULTS.toArray(new String[RESULTS.length()]) : null);
//    }
	/*
	 * public boolean getBoolCache(String fullkey, boolean defaultvalue) {
	 * if(CACHEMANAGER.isCacheAvailable()) { int IsCached =
	 * CACHEMANAGER.isCached(this, fullkey);
	 * 
	 * if(IsCached > -1) { return
	 * (boolean)(Boolean)CACHEMANAGER.getCacheObject(IsCached); }
	 * 
	 * IProperty Prop = getProperty(fullkey);
	 * 
	 * if(Prop == null) { return defaultvalue; } else { Object Value =
	 * Prop.getVariable();
	 * 
	 * if(Value == null) { return defaultvalue; } else { return
	 * (boolean)(Boolean)CACHEMANAGER.addToCache(this, fullkey,
	 * Prop.getVariable()); } } } else { throw new
	 * RuntimeException("Cache Not Created"); } }
	 * 
	 * public String getStrCache(String fullkey, String defaultvalue) {
	 * if(CACHEMANAGER.isCacheAvailable()) { int IsCached =
	 * CACHEMANAGER.isCached(this, fullkey);
	 * 
	 * if(IsCached > -1) { return (String)CACHEMANAGER.getCacheObject(IsCached);
	 * }
	 * 
	 * IProperty Prop = getProperty(fullkey);
	 * 
	 * if(Prop == null) { return defaultvalue; } else { Object Value =
	 * Prop.getVariable();
	 * 
	 * if(Value == null) { return defaultvalue; } else { return
	 * (String)CACHEMANAGER.addToCache(this, fullkey, Prop.getVariable()); } } }
	 * else { throw new RuntimeException("Cache Not Created"); } }
	 * 
	 * public int getIntCache(String fullkey, int defaultvalue) {
	 * if(CACHEMANAGER.isCacheAvailable()) { int IsCached =
	 * CACHEMANAGER.isCached(this, fullkey);
	 * 
	 * if(IsCached > -1) { return
	 * (int)(Integer)CACHEMANAGER.getCacheObject(IsCached); }
	 * 
	 * IProperty Prop = getProperty(fullkey);
	 * 
	 * if(Prop == null) { return defaultvalue; } else { Object Value =
	 * Prop.getVariable();
	 * 
	 * if(Value == null) { return defaultvalue; } else { return
	 * (int)(Integer)CACHEMANAGER.addToCache(this, fullkey, Prop.getVariable());
	 * } } } else { throw new RuntimeException("Cache Not Created"); } }
	 * 
	 * public Object getObjectCache(String fullkey, Object defaultvalue) {
	 * if(CACHEMANAGER.isCacheAvailable()) { int IsCached =
	 * CACHEMANAGER.isCached(this, fullkey);
	 * 
	 * if(IsCached > -1) { return CACHEMANAGER.getCacheObject(IsCached); }
	 * 
	 * IProperty Prop = getProperty(fullkey);
	 * 
	 * if(Prop == null) { return defaultvalue; } else { return
	 * CACHEMANAGER.addToCache(this, fullkey, Prop.getVariable()); } } else {
	 * throw new RuntimeException("Cache Not Created"); } }
	 */

	//STATIC
	private boolean match(String str1, String str2) {
		if(_MATCH_CASE) {
			return str1.equals(str2);
		} else {
			return str1.equalsIgnoreCase(str2);
		}
	}

//    public static void main(String[] args) {
//        final Node SETTINGS = new Node("root");
//
//        final Node MAIN = SETTINGS.createNode("main");
//
//        final Node WINDOW = MAIN.createNode("window");
//        WINDOW.addProperty(new DefaultProperty("title", "Test Window"));
//        WINDOW.addProperty(new DefaultProperty("location", new java.awt.Dimension(0, 0)));
//        WINDOW.addProperty(new DefaultProperty("size", new java.awt.Dimension(0, 0)));
//
//        MAIN.addProperty(new DefaultProperty("hide", false));
//        MAIN.addProperty(new DefaultProperty("fullscreen", false));
//
//        System.out.println(SETTINGS.getNode("main").getNode("window").getStr("title", "ERROR"));
//
//        System.out.println(SETTINGS.getNode("main").getBool("hide", true));
//    }
}
