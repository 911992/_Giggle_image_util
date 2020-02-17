/**
 * 
 */
package giggle.tribe.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Arash
 * 
 *         On Feb 16, 2020 4:03 AM
 * 
 *         This file contains some auxiliary IO functions. For sure it is
 *         supposed to be part of another dedicated lib, something like IO_Util,
 *         but since there is no such thing yet, we could use it for this lib,
 *         for now
 * 
 *         This is a stateless type/class in heart, so it's thread-safe too.
 *
 */
public class IO_Aux {

	/**
	 * Default buffer size for declaring local buff array in blocking io mode.
	 * negative or values smaller than 16 will be ignored
	 */
	public static final int BIO_BUFFER_SIZE = 2048;

	/* nothing spcial with this */
	private IO_Aux() {

	}

	/**
	 * Copies content of given input stream, to output stream until it reaches EOF.
	 * Note: if given input stream might not send the EOF(assuming a HTTP associated
	 * stream to request), you need to code a wrapper between reald stream and given
	 * arg to support stream read by a len
	 * 
	 * @param arg_in_stream  the input stream as source
	 * @param arg_out_stream destination stream
	 * @throws IOException when underlying layer(SKD) throws any exception, it will
	 *                     be forwarded
	 */
	public static void stream_cpy_bio(InputStream arg_in_stream, OutputStream arg_out_stream) throws IOException {
		/* creating a local buffer */
		byte _buff[] = new byte[Math.max(16, BIO_BUFFER_SIZE)];

		/* io op read res */
		int _read;

		/* while not reached EOF */
		while ((_read = arg_in_stream.read(_buff)) > 0) {
			arg_out_stream.write(_buff, 0, _read);
		}

		/* flushing the out */
		arg_out_stream.flush();
	}
}
