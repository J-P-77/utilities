package utillib.utilities;

/**
 * <pre>
 * <b>Current Version 1.2.0</b>
 * 
 * March 19, 2010 (Version 1.0.0)
 *     -First Released
 *     
 * January 14, 2011 (Version 1.1.0)
 *     -Fixed Bug
 *     	   -Invalid Index Number In Method getAvailableCache()
 * 
 * January 26, 2011 (Version 1.2.0)
 *     -Fixed Bug
 *     	   -Did Not Fix Invalid Index Number In [getAvailableCache()] This Should Now Be Fixed
 * 
 * @author Justin Palinkas
 * 
 * </pre>
 */
public class CacheManager {
	private Cache[] _Cache = null;
	private int _CacheTop = 0;

	private int _CacheAvailableIndex = -1;

	private boolean _UseTimeCache = false;

//    public CacheManager() {}

	public CacheManager(int cachesize, boolean usetimecache) {
		createCache(cachesize, usetimecache, false);
	}

	/**
	 * 
	 * @param cachesize
	 *            if cachesize is less then or equal to zero cache object will
	 *            be destroyed
	 * @param usetimecache
	 * @param preserve
	 */
	public void createCache(int cachesize, boolean usetimecache, boolean preserve) {
		if(cachesize <= 0) {
			destroyCache();
		} else {
			_UseTimeCache = usetimecache;
			_CacheAvailableIndex = -1;

			if(preserve && _Cache != null) {
				Cache[] T_Cache = _Cache;
				final int T_TOP = _CacheTop;

				_Cache = new Cache[cachesize];
				_CacheTop = 0;
				_CacheAvailableIndex = -1;

				for(int X = 0; X < T_TOP && _CacheTop < _Cache.length; X++, _CacheTop++) {
					_Cache[_CacheTop] = T_Cache[X];

					T_Cache[X] = null;
				}
				T_Cache = null;
				_CacheAvailableIndex = _CacheTop;
			} else {
				destroyCache();
				_Cache = new Cache[cachesize];
			}
		}
	}

	private void destroyCache() {
		if(_Cache != null) {
			while(--_CacheTop >= 0) {
				_Cache[_CacheTop]._Key = null;
				_Cache[_CacheTop]._Object = null;
				_Cache[_CacheTop] = null;
			}

			_Cache = null;
		}
	}

	public boolean isCacheAvailable() {
		return (_Cache != null);
	}

	public void useTimeCache() {
		_UseTimeCache = true;
	}

	public void useNextCache() {
		_UseTimeCache = false;
	}

	public int getCacheObjectIndex(String name) {
		return isCached(name);
	}

	public Object getCacheObject(int cacheindex) {
		return _Cache[cacheindex].getObject();
	}

	public CacheManager.Cache getCacheEntry(int cacheindex) {
		return _Cache[cacheindex];
	}

	public void removeAllCacheEntries() {
		for(int X = 0; X < _CacheTop; X++) {
			_Cache[X] = null;
		}
	}

	public void removeCacheEntry(int index) {
		if(index >= 0 && index < _CacheTop) {
			_Cache[index] = null;
		}
	}

	public Object addToCache(String key, Object obj) {
		if(isCacheAvailable()) {
			final int A_INDEX = (_UseTimeCache ? getAvailableCacheTime() : getAvailableCache());

			if(_Cache[A_INDEX] == null) {
				_Cache[A_INDEX] = new Cache();
				_CacheTop++;
			} else {
				_Cache[A_INDEX]._Key = null;
				_Cache[A_INDEX]._Object = null;
				_Cache[A_INDEX]._LastAccessedTime = 0;
			}

			_Cache[A_INDEX]._Key = key;
			_Cache[A_INDEX]._Object = obj;
			_Cache[A_INDEX]._CreatedTime = System.currentTimeMillis();
			_Cache[A_INDEX]._LastAccessedTime = _Cache[A_INDEX]._CreatedTime;

			return obj;
		} else {
			return null;
		}
	}

	public int isCached(String key) {
		for(int X = 0; X < _CacheTop; X++) {
			if(_Cache[X] != null) {
				if(key.equals(_Cache[X]._Key)) {
					return X;
				}
			}
		}

		return -1;
	}

	private int getAvailableCache() {
		_CacheAvailableIndex++;

		if(_CacheAvailableIndex >= _Cache.length) {
			_CacheAvailableIndex = 0;
		}

		return _CacheAvailableIndex;
	}

	private int getAvailableCacheTime() {
		int SmallestIndex = 0;
		long SmallestTime = System.currentTimeMillis();

		for(int X = 0; X < _Cache.length; X++) {
			if(_Cache[X] == null) {
				return X;
			} else {
				if(_Cache[X]._LastAccessedTime < SmallestTime) {
					SmallestTime = _Cache[X]._LastAccessedTime;
					SmallestIndex = X;
				}
			}
		}

		return SmallestIndex;
	}

	//CLASSES
	public class Cache {
		private String _Key = null;
		private Object _Object = null;

		private long _CreatedTime = 0;
		private long _LastAccessedTime = 0;

		public String getKey() {
			return _Key;
		}

		public long getCreatedTime() {
			return _CreatedTime;
		}

		public long getLastAccessedTime() {
			return _LastAccessedTime;
		}

		public Object getObject() {
			_LastAccessedTime = System.currentTimeMillis();
			return _Object;
		}
	}

//    public static void main(String[] args) {
//        CacheManager Cm = new CacheManager();
//
//        Cm.createCache(2, false, false);
//
//        Cm.addToCache("1", 1);
//        Cm.addToCache("2", 2);
//
//        Cm.addToCache("3", 3);
//        Cm.addToCache("4", 4);
//
//        Cm.addToCache("5", 5);
//        Cm.addToCache("6", 6);
//    }
}
