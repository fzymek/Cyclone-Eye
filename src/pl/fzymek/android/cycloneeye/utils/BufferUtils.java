package pl.fzymek.android.cycloneeye.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public final class BufferUtils {

	private BufferUtils() {
	}

	public static FloatBuffer makeFloatBuffer(float[] buff) {
		final ByteBuffer bb = ByteBuffer.allocateDirect(buff.length * 4);
		bb.order(ByteOrder.nativeOrder());
		final FloatBuffer fb = bb.asFloatBuffer();
		fb.put(buff);
		fb.position(0);
		return fb;
	}

	public static ShortBuffer makeShortBuffer(short[] buff) {
		final ByteBuffer bb = ByteBuffer.allocateDirect(buff.length * 2);
		bb.order(ByteOrder.nativeOrder());
		final ShortBuffer sb = bb.asShortBuffer();
		sb.put(buff);
		sb.position(0);
		return sb;
	}

	public static IntBuffer makeIntBuffer(int[] buff) {
		final ByteBuffer bb = ByteBuffer.allocateDirect(buff.length * 4);
		bb.order(ByteOrder.nativeOrder());
		final IntBuffer sb = bb.asIntBuffer();
		sb.put(buff);
		sb.position(0);
		return sb;
	}

	public static ByteBuffer makeByteBuffer(byte[] buff) {
		final ByteBuffer bb = ByteBuffer.allocateDirect(buff.length * 2);
		bb.order(ByteOrder.nativeOrder());
		bb.put(buff);
		bb.position(0);
		return bb;
	}

}
