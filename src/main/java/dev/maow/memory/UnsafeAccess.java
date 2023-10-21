package dev.maow.memory;

import sun.misc.Unsafe;

final class UnsafeAccess {
	static final Unsafe UNSAFE;
	static {
		try {
			final var field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			UNSAFE = (Unsafe) field.get(null);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("could not reflect unsafe", e);
		}
	}

	private UnsafeAccess() {}
}