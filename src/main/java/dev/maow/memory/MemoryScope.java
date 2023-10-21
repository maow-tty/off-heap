package dev.maow.memory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public final class MemoryScope implements Closeable {
	private final List<MemorySegment> segments;

	public MemoryScope() {
		this.segments = new ArrayList<>();
	}

	public MemorySegment allocate(long bytes) {
		final var address = UnsafeAccess.UNSAFE.allocateMemory(bytes);
		final var segment = new MemorySegment(address, bytes, this);
		this.segments.add(segment);
		return segment;
	}

	MemorySegment reallocate(MemorySegment segment, long bytes) {
		segments.remove(segment);
		final var address = UnsafeAccess.UNSAFE.reallocateMemory(segment.address(), bytes);
		final var newSegment = new MemorySegment(address, bytes, this);
		segments.add(newSegment);
		return newSegment;
	}

	void free(MemorySegment segment) {
		if (segments.contains(segment)) {
			segments.remove(segment);
			UnsafeAccess.UNSAFE.freeMemory(segment.address());
		}
	}

	@Override
	public void close() {
		final var segments = this.segments.iterator();
		while (segments.hasNext()) {
			final var segment = segments.next();
			segments.remove();
			UnsafeAccess.UNSAFE.freeMemory(segment.address());
		}
	}
}