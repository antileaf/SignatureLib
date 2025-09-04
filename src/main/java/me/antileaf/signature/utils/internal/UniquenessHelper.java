package me.antileaf.signature.utils.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class UniquenessHelper {
	public static final String ID = "signature_lib_uniqueness_identifier";

	public static boolean check() {
		return isUnique;
	}

	private static final boolean isUnique = acquireLock();

	private static boolean acquireLock() {
		try {
			ClassLoader loader = ClassLoader.getSystemClassLoader();

			Field packagesField = ClassLoader.class.getDeclaredField("packages");
			packagesField.setAccessible(true);

			@SuppressWarnings("unchecked")
			Map<String, Package> packages = (Map<String, Package>) packagesField.get(loader);

			if (packages.containsKey(ID))
				return false;

			Method definePackageMethod = ClassLoader.class.getDeclaredMethod("definePackage",
					String.class, String.class, String.class, String.class,
					String.class, String.class, String.class, java.net.URL.class);
			definePackageMethod.setAccessible(true);

			definePackageMethod.invoke(loader, ID, null, null, null, null, null, null, null);

			return true;
		}
		catch (Exception e) {
			System.out.println("[SignatureLib] Failed to acquire uniqueness lock.\nFalling back to system property method.");
			e.printStackTrace();
			return acquireLockFallback();
		}
	}

	private static boolean acquireLockFallback() {
		try {
			if (System.getProperty(ID) != null)
				return false;

			System.setProperty(ID, "acquired");
			return true;
		}
		catch (SecurityException e) {
			System.out.println("[SignatureLib] Failed to acquire uniqueness lock (fallback).");
			e.printStackTrace();
			return false;
		}
	}
}
