package dev.maow.memory;

public final class MemorySegment {
	private final long address;
	private final long bytes;
	private final MemoryScope scope;

	MemorySegment(long address, long bytes, MemoryScope scope) {
		this.address = address;
		this.bytes = bytes;
		this.scope = scope;
	}

	public static MemorySegment allocate(long bytes, MemoryScope scope) {
		return scope.allocate(bytes);
	}

	public MemorySegment reallocate(long bytes) {
		return this.scope.reallocate(this, bytes);
	}

	public void initialize(byte value) {
		UnsafeAccess.UNSAFE.setMemory(this.address, this.bytes, (byte) 0);
	}

	public void free() {
		this.scope.free(this);
	}

	public long address() {
		return this.address;
	}
}