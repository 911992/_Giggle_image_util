/**
 * 
 */
package giggle.tribe.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Arash
 * 
 *         On Feb 16, 2020 17:03
 *
 *         This class contains some functions for testing Image_Util
 *         functionality
 */
public class Image_Util_Usage {

	/**
	 * Set a working path, where artifact will be saved, like ~/_tmp/, or C:/_tmp/ ,
	 * etc... Make sure about permission stuffs
	 * 
	 * NOTE: make sure to add / at the end
	 * 
	 * If it's null, main func will print en error, and ignores the process
	 */
	static final private String WORKING_PATH = null;

	/**
	 * Sample about resizing to 1080(even when source is smaller), with watermark
	 * 
	 */
	public static void resize1080_watermark() {
		try {
			/* generating the setup instance */
			Image_Process_Setup _con_ins = Image_Process_Setup.Builder.fit_scale(1919, 0).watermark(true)
					.ignore_image_enlargement(false).build();

			/* local embedded file name */
			/**
			 * CC-(C) Jason Tang image by interfacelift.com
			 */
			final String _src_mbde_image_path = "03374_mthoodpainted_2880x1620.jpg";

			/* verbose */
			System.out.printf("Resizing to 1080, with watermark example. File: %s\n", _src_mbde_image_path);

			/* loading the image */
			InputStream _img_src_stream = Image_Util_Usage.class.getResourceAsStream(_src_mbde_image_path);

			/* a local path for saving the result */
			final String _dest_image_path = String.format("%sresized_%s", WORKING_PATH, _src_mbde_image_path);

			/* created the associated file(target image) */
			File _out_f = new File(_dest_image_path);

			/* creating the file, if not exist */
			_out_f.createNewFile();

			/* creating the final file, to save result */
			FileOutputStream _img_out_stream = new FileOutputStream(_out_f);

			Image_Util.image_process(_img_src_stream, _con_ins, _img_out_stream, null);

		} catch (Throwable wt) {
			wt.printStackTrace();
		}

	}

	/**
	 * asks to create the target image with same dimension source image has, plus
	 * watermark
	 */
	public static void watermark_only_no_resize() {
		try {
			/* generating the setup instance */
			Image_Process_Setup _con_ins = Image_Process_Setup.Builder.no_scale_with_watermark().build();

			/* local embedded file name */
			/**
			 * CC-(C) Woojin Bae image by interfacelift.com
			 */
			final String _src_mbde_image_path = "03824_autumnskyinnewyork_5120x2880.jpg";

			/* verbose */
			System.out.printf("No resizing, with watermark example. File: %s\n", _src_mbde_image_path);

			/* loading the image */
			InputStream _img_src_stream = Image_Util_Usage.class.getResourceAsStream(_src_mbde_image_path);

			/* a local path for saving the result */
			final String _dest_image_path = String.format("%swatermarked_%s", WORKING_PATH, _src_mbde_image_path);

			/* created the associated file(target image) */
			File _out_f = new File(_dest_image_path);

			/* creating the file, if not exist */
			_out_f.createNewFile();

			/* creating the final file, to save result */
			FileOutputStream _img_out_stream = new FileOutputStream(_out_f);

			Image_Util.image_process(_img_src_stream, _con_ins, _img_out_stream, null);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	public static void no_enlarging_resize_no_watermark() {
		try {
			/* generating the setup instance */
			Image_Process_Setup _con_ins = Image_Process_Setup.Builder.fit_scale(0, 3073).ignore_image_enlargement(true)
					.watermark(false).build();

			/* local embedded file name */
			/**
			 * CC-(C) Jarred Decker image by interfacelift.com
			 * https://twitter.com/jdphotopdx
			 */
			final String _src_mbde_image_path = "04053_portlandsunrise_3840x2160.jpg";

			/* verbose */
			System.out.printf("No enlarging on target, with watermark example. File: %s\n", _src_mbde_image_path);

			/* loading the image */
			InputStream _img_src_stream = Image_Util_Usage.class.getResourceAsStream(_src_mbde_image_path);

			/* a local path for saving the result */
			final String _dest_image_path = String.format("%snon_enlarged_%s", WORKING_PATH, _src_mbde_image_path);

			/* created the associated file(target image) */
			File _out_f = new File(_dest_image_path);

			/* creating the file, if not exist */
			_out_f.createNewFile();

			/* creating the final file, to save result */
			FileOutputStream _img_out_stream = new FileOutputStream(_out_f);

			Image_Util.image_process(_img_src_stream, _con_ins, _img_out_stream, null);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * Resizes the image based on a percent, with watermark
	 */
	public static void resize_ratio() {
		try {
			/* generating the setup instance, with 73% of input */
			Image_Process_Setup _con_ins = Image_Process_Setup.Builder.ratio_scale(0.73f).watermark(true).build();

			/* local embedded file name */
			/**
			 * CC-(C) Dionisis Partheniadis image by interfacelift.com
			 */
			final String _src_mbde_image_path = "04050_manhattanview_3840x2160.jpg";

			/* loading the image */
			InputStream _img_src_stream = Image_Util_Usage.class.getResourceAsStream(_src_mbde_image_path);

			/* a local path for saving the result */
			final String _dest_image_path = String.format("%sratio_scaled_%s", WORKING_PATH, _src_mbde_image_path);

			/* created the associated file(target image) */
			File _out_f = new File(_dest_image_path);

			/* creating the file, if not exist */
			_out_f.createNewFile();

			/* creating the final file, to save result */
			FileOutputStream _img_out_stream = new FileOutputStream(_out_f);

			Image_Util.image_process(_img_src_stream, _con_ins, _img_out_stream, null);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * Resizes the image based on a percent, with watermark
	 */
	public static void resize_ratio_with_listener() {
		try {
			/* generating the setup instance, with 50% of input */
			Image_Process_Setup _con_ins = Image_Process_Setup.Builder.ratio_scale(0.49f).watermark(true).build();

			/* local embedded file name */
			/**
			 * CC-(C) Sven Mueller image by interfacelift.com
			 */
			final String _src_mbde_image_path = "04112_grosunset_3840x2160.jpg";

			/* to hold last known ts */
			final AtomicLong _last_known_ts = new AtomicLong(System.currentTimeMillis());

			/* local anon listener */
			Image_Resize_Listener _evt_listener = new Image_Resize_Listener() {

				public void trigger(Image_Resize_State arg_event_type) {
					/* getting current ts */
					long _now = System.currentTimeMillis();

					/* calculating elapsed time */
					long _duration = _now - _last_known_ts.get();

					/* preparing the msg and log it */
					System.out.printf("%s(%d)\n", arg_event_type.toString(), _duration);

					/* settign now as last known time */
					_last_known_ts.set(System.currentTimeMillis());
				}
			};

			/* loading the image */
			InputStream _img_src_stream = Image_Util_Usage.class.getResourceAsStream(_src_mbde_image_path);

			/* a local path for saving the result */
			final String _dest_image_path = String.format("%sratio_scaled_%s", WORKING_PATH, _src_mbde_image_path);

			/* created the associated file(target image) */
			File _out_f = new File(_dest_image_path);

			/* creating the file, if not exist */
			_out_f.createNewFile();

			/* creating the final file, to save result */
			FileOutputStream _img_out_stream = new FileOutputStream(_out_f);

			/* pass listener as last arg */
			Image_Util.image_process(_img_src_stream, _con_ins, _img_out_stream, _evt_listener);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * @param args from cmd line
	 */
	public static void main(String args[]) {

		/**
		 * Check if you have declared the working_path successfully
		 */
		if (WORKING_PATH == null) {
			System.err.printf(
					"Error, please define WORKING_PATH variable as target accessible path for saving sample...\n");
			/* fail respond as process */
			System.exit(1);
		}

		/* Greeting */
		System.out.printf("%s v%s\nWorking path:%s\n", Giggle_Project_Meta.PROJECT_NAME,
				Giggle_Project_Meta.PROJECT_VERSION, WORKING_PATH);

		/* testing 1080p resize, with watermark */
		resize1080_watermark();

		/* no resizing, just adding the watermark */
		watermark_only_no_resize();

		/* No enlargement when source is smaller than target, no watermark */
		no_enlarging_resize_no_watermark();

		/* Watermark with ratio scale to 73% */
		resize_ratio();

		/* resize, with a listener which logs out event on stdout */
		resize_ratio_with_listener();

	}

}
