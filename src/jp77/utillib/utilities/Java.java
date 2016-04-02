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

package jp77.utillib.utilities;

import jp77.utillib.utilities.SystemProperties.Property;

public class Java {
//	public static enum Java_Version {
//		JAVA5,
//		JAVA6,
//		JAVA7;
//	};

	public static final boolean _IS_JAVA7_ = isVersion(1, 7);
	public static final boolean _IS_JAVA6_ = isVersion(1, 6);
	public static final boolean _IS_JAVA5_ = isVersion(1, 5);

//	public static final boolean _IS_JAVA4_ = isVersion(1, 4);

	public static String getVersionString() {
		return SystemProperties.getProperty(Property.JAVA_RUNTIME_VERSION);
	}

	public static boolean isVersion(int major, int minor) {
		return isVersion(major, minor, -1, -1);
	}

	public static boolean isVersion(int major, int minor, int release) {
		return isVersion(major, minor, release, -1);
	}

	public static boolean isVersion(int major, int minor, int release, int build) {
		final String[] SPLIT = getVersionString().split("\\W");

		final int MAJOR = (SPLIT.length > 0 ? Integer.parseInt(SPLIT[0]) : -1);
		final int MINOR = (SPLIT.length > 1 ? Integer.parseInt(SPLIT[1]) : -1);

		final int RELEASE;
		final int UPDATE;

		if(SPLIT.length > 2) {
			final int INDEX = SPLIT[2].indexOf('_');

			if(INDEX == -1) {
				RELEASE = Integer.parseInt(SPLIT[2]);
				UPDATE = 0;
			} else {
				RELEASE = Integer.parseInt(SPLIT[2].substring(0, INDEX));
				UPDATE = Integer.parseInt(SPLIT[2].substring(INDEX + 1));
			}
		} else {
			RELEASE = -1;
			UPDATE = -1;
		}

		final int BUILD = (SPLIT.length > 3 ? Integer.parseInt(SPLIT[3].substring(1)) : -1);

		if(MAJOR == major) {
			if(MINOR == minor) {
				if(release != -1) {
					if(RELEASE == release) {
						if(build != -1) {
							if(BUILD == build) {
								return true;
							}
						} else {
							return true;
						}
					}
				} else {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isAtLeastVersion(int major, int minor) {
		return isAtLeastVersion(major, minor, -1, -1);
	}

	public static boolean isAtLeastVersion(int major, int minor, int release) {
		return isAtLeastVersion(major, minor, release, -1);
	}

	public static boolean isAtLeastVersion(int major, int minor, int release, int build) {
		final String[] SPLIT = getVersionString().split("\\W");

		final int MAJOR = Integer.parseInt(SPLIT[0]);
		final int MINOR = Integer.parseInt(SPLIT[1]);

		final int RELEASE;
		final int UPDATE;

		if(SPLIT.length > 2) {
			final int INDEX = SPLIT[2].indexOf('_');

			if(INDEX == -1) {
				RELEASE = Integer.parseInt(SPLIT[2]);
				UPDATE = 0;
			} else {
				RELEASE = Integer.parseInt(SPLIT[2].substring(0, INDEX));
				UPDATE = Integer.parseInt(SPLIT[2].substring(INDEX + 1));
			}
		} else {
			RELEASE = -1;
			UPDATE = -1;
		}

		final int BUILD = Integer.parseInt(SPLIT[3].substring(1));

		if(MAJOR == major) {
			if(MINOR == minor) {
				if(release != -1) {
					if(RELEASE == release) {
						if(build != -1) {
							if(BUILD == build) {
								return true;
							} else if(BUILD > build) {
								return true;
							} else {
								return false;
							}
						} else {
							return true;
						}
					} else if(RELEASE > release) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else if(MINOR > minor) {
				return true;
			} else {
				return false;
			}
		} else if(MAJOR > major) {
			return true;
		} else {
			return false;
		}
	}
}
