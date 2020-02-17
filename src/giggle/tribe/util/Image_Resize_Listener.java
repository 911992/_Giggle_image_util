/**
 * 
 */
package giggle.tribe.util;

/**
 * @author Arash
 * 
 *         On Feb 16, 2020 00:11 AM
 * @version 20200215 (Feb 15, 2020)
 * 
 *          This interface is used for reporting back processing steps to
 *          callee.
 */
public interface Image_Resize_Listener {

	/**
	 * Enum type which is used by image resize-er to pass out events to a
	 * listener(if any)
	 * 
	 * @see Image_Util
	 */
	public enum Image_Resize_State {
		/**
		 * When an exception/error was raised NOTE: Image_Resize_State_PROCESS_FINISHED
		 * may not be send by after this event
		 * 
		 * @see Image_Util
		 */
		Image_Resize_State_ERROR,
		/**
		 * When system begins reading data from associated source image stream, to read
		 * one image
		 */
		Image_Resize_State_IMAGE_IO_READ_BEGIN,

		/**
		 * When reading image from source is done, and it's about loading the image info
		 */
		Image_Resize_State_IMAGE_FETCH_DIMENSION_BEGIN,

		/**
		 * When target image dimension is about to be calculated(beside the watermark)
		 */
		Image_Resize_State_CALCULATING_FINAL_IMAGE_DIMENSION_BEGIN,

		/**
		 * When there is nothing to do(no watermark and no resize) on source image, so
		 * starget is going be actual source file
		 */
		Image_Resize_State_SOURCE_IMAGE_AS_TARGET,

		/**
		 * When target image bitmap/matrix creation is about to begin
		 */
		Image_Resize_State_TARGET_IMAGE_CREATION_BEGIN,

		/**
		 * When image resizing begins
		 */
		Image_Resize_State_IMAGE_RESIZING_BEGIN,

		/**
		 * When target image is ready, and it is started to be streamed to given output
		 */
		Image_Resize_State_TARGET_IMAGE_IO_WRITE_BEGIN,

		/**
		 * When eveything is done
		 */
		Image_Resize_State_PROCESS_FINISHED

	};

	/**
	 * callback function, which is called by core function to listener
	 * 
	 * @param arg_event_type the related event type
	 */
	void trigger(Image_Resize_State arg_event_type);
}
