package utillib.utilities;

import utillib.utilities.SystemProperties.Property;

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
