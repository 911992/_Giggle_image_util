/**
 * 
 */
package giggle.tribe.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import giggle.tribe.util.Image_Resize_Listener.Image_Resize_State;
import giggle.tribe.util.ex.Bad_Image_Processing_State_Exception;

/**
 * @author Arash
 * 
 *         On Feb 15, 2020 15:17
 * @version 20200215 (Feb 15, 2020)
 *
 *          A stateless class for handling and manipulating image file type.
 */
public class Image_Util {

	/**
	 * Default width value for resizing image. In scale fit mode
	 */
	public static final int DEFAULT_IMAGE_SCALE_WIDTH = 1920;

	/**
	 * Default height value for resizing image. In scale fit mode
	 */
	public static final int DEFAULT_IMAGE_SCALE_HEIGHT = 0;

	/**
	 * Default placing watermark
	 */
	public static final boolean DEFAULT_IMAGE_WATERMARK = false;

	/**
	 * Text to be placed at watermark
	 */
	public static final String WATERMARK_TEXT_MESSAGE = "Giggle";

	/**
	 * The font family is used to for drawing watermark text
	 */
	public static final String WATERMARK_TEXT_FONT_FAMILY = Font.MONOSPACED;

	/**
	 * Fonts style to be applied on watermark text
	 */
	public static final int WATERMARK_TEXT_FONT_STYLE = Font.PLAIN;

	/**
	 * Font size to be used for watermark text drawing
	 */
	public static final int WATERMARK_TEXT_FONT_SIZE = 16;

	/**
	 * Shadow(first watermark write) foreground color of watermark label
	 */
	public static final Color WATERMARK_SHADOW_COLOR = Color.WHITE;

	/**
	 * Foreground(top) color of watermark
	 */
	public static final Color WATERMARK_FORE_COLOR = Color.MAGENTA;

	/**
	 * Default background color of target image.
	 */
	public static final Color TARGET_IMAGE_BACKGROUND_COLOR = Color.WHITE;

	/**
	 * Default output artifact type
	 */
	public static final String TARGET_IMAGE_TYPE = "jpeg";

	/**
	 * Pixel amount needed to be spaced between top foregound drawing of watermark,
	 * and bottom layer shadow. Negative values means shadow should be placed at
	 * top-left, while positive vals means shadow should be placed at right-bottom
	 */
	public static final int WATERMARK_SHADOW_OFFSET = 1;

	/**
	 * Private constructor to prevent instantiating.
	 */
	private Image_Util() {
	}

	/**
	 * Generates a default setup for image resize op
	 * 
	 * @return an instance with some hard-coded default state
	 */
	public static Image_Process_Setup get_default_image_setup() {
		/* build an instance using its builder class */
		Image_Process_Setup _res = Image_Process_Setup.Builder
				.fit_scale(DEFAULT_IMAGE_SCALE_WIDTH, DEFAULT_IMAGE_SCALE_HEIGHT).watermark(DEFAULT_IMAGE_WATERMARK)
				.build();

		/* returning the result */
		return _res;
	}

	/**
	 * Resize, and watermark the given image which is could be found in associated
	 * input argument. It reads an image from given input stream, and then tries to
	 * resize and watermark it if it's applicable, based on given setup instance
	 * 
	 * Target image will be streamed to given output stream using a BIO ops
	 * 
	 * @param arg_img_instream   the stream contains the image data, could be marked
	 *                           as non-EOF
	 * @param arg_setup_ins      The setup instance contaisn the policy need to
	 *                           considered while processing the image and
	 *                           generating the output image
	 * @param arg_out_result     stream output that target image will be streamed
	 *                           too
	 * @param arg_event_listener event listener to inform caller/listener about
	 *                           detailed processing steps. This can be null
	 * @throws NullPointerException                 when any of given args are
	 *                                              null(except arg_event_listener)
	 * @throws IOException                          When any IOException is risen by
	 *                                              underlying io ops.
	 * @throws Bad_Image_Processing_State_Exception when given image convert
	 *                                              setup(arg_setup_ins) contains
	 *                                              conflict(s)
	 * 
	 * @see Image_Resize_Listener
	 * @see Image_Process_Setup
	 * @see IO_Aux
	 */
	public static void image_process(InputStream arg_img_instream, Image_Process_Setup arg_setup_ins,
			OutputStream arg_out_result, Image_Resize_Listener arg_event_listener)
			throws NullPointerException, IOException, Bad_Image_Processing_State_Exception {
		Image_Resize_Listener _listener = arg_event_listener;

		/* check if given listener is null */
		if (_listener == null) {
			/* uing a null-object to prevent null ptr exception */
			_listener = Image_listener_Null.GLOBAL_INSTANCE;
		}

		/* check if given setup is null */
		if (arg_setup_ins == null) {

			/* sending error event */
			_listener.trigger(Image_Resize_State.Image_Resize_State_ERROR);

			/* throw the exception to caller */
			throw new NullPointerException("Given Image_Process_Setup is null");
		}

		/* check if given img in source is null */
		if (arg_img_instream == null) {

			/* sending error event */
			_listener.trigger(Image_Resize_State.Image_Resize_State_ERROR);

			/* throw the exception to caller */
			throw new NullPointerException("Given source image InputStream is null");
		}

		/* check if given img out source is null */
		if (arg_out_result == null) {

			/* sending error event */
			_listener.trigger(Image_Resize_State.Image_Resize_State_ERROR);

			/* throw the exception to caller */
			throw new NullPointerException("Given target image OutputStream is null");
		}

		if (arg_setup_ins.is_applicable_sync() == false) {

			/* sending error event */
			_listener.trigger(Image_Resize_State.Image_Resize_State_ERROR);

			/* throw the exception to caller */
			throw new Bad_Image_Processing_State_Exception(arg_setup_ins.get_instance_state_conflict_message());
		}

		/* sending the image read event */
		_listener.trigger(Image_Resize_State.Image_Resize_State_IMAGE_IO_READ_BEGIN);

		/* trying to read one image from input stream, may throws some exceptions */
		BufferedImage _img = ImageIO.read(arg_img_instream);

		/* sending the image get dimension */
		_listener.trigger(Image_Resize_State.Image_Resize_State_IMAGE_FETCH_DIMENSION_BEGIN);

		/* getting source image width and height */
		int _src_width = _img.getWidth();
		int _src_height = _img.getHeight();

		/* sending the calculating target image dimension event */
		_listener.trigger(Image_Resize_State.Image_Resize_State_CALCULATING_FINAL_IMAGE_DIMENSION_BEGIN);
		/* calc image aspect ratio */
		float _src_aspect = ((float) _src_width) / ((float) _src_height);

		/* var declare to hold the final */
		int _target_width;
		int _target_heght;

		/* switch one resize policy */
		switch (arg_setup_ins.getResize_policy()) {
			/* constant resize mode */
			case ImageResizeMode_SCALE_FIT: {
				/* when width need to applied */
				if (arg_setup_ins.getScale_fit_width() > 0) {
					_target_width = arg_setup_ins.getScale_fit_width();
				} else {
					_target_width = _src_width;
				}

				/* when height need to applied */
				if (arg_setup_ins.getScale_fit_height() > 0) {
					_target_heght = arg_setup_ins.getScale_fit_height();
				} else {
					_target_heght = _src_height;
				}

				/*
				 * check if image is landscape/portrait to take the larger egde for calculating
				 */
				/* when image is a landscape, so take the width to calc height */
				if (_src_aspect > 1.0f) {
					_target_heght = (int) (((float) _target_width) / _src_aspect);
				}
				/* when image is portrait, so viceversa */
				else {
					_target_width = (int) (((float) _target_heght) * _src_aspect);
				}
				break;
			}
			case ImageResizeMode_SCALE_RATIO: {
				/* calculating width and height based on given ratio */
				_target_width = (int) (_src_width * arg_setup_ins.getScale_ratio());
				_target_heght = (int) (_src_height * arg_setup_ins.getScale_ratio());
				break;
			}

			/* when resizing is not a thing */
			case ImageResizeMode_NONE:
			default: {
				_target_width = _src_width;
				_target_heght = _src_height;
				break;
			}
		}

		/* check if watermark is one thing */
		boolean _watermark = arg_setup_ins.isWith_watermark();

		/* check if enlargment is needed */
		boolean _enlarging_ignored = arg_setup_ins.isIgnore_image_enlarging();

		/* check if nothing should be done at the end. */
		if (!_watermark && ((_src_width == _target_width) || ((_src_width < _target_width) && _enlarging_ignored))) {
			/* tells the listener that the source is going to be as same as target */
			_listener.trigger(Image_Resize_State.Image_Resize_State_SOURCE_IMAGE_AS_TARGET);

			/* informing listener, beginning of target image write */
			_listener.trigger(Image_Resize_State.Image_Resize_State_TARGET_IMAGE_IO_WRITE_BEGIN);

			/* copy the source image content to output as equal result */
			ImageIO.write(_img, "jpeg", arg_out_result);

			/* telling listener, op has been done */
			_listener.trigger(Image_Resize_State.Image_Resize_State_PROCESS_FINISHED);

			/* retutning */
			return;
		}

		/* firing event to listener, about creating target image */
		_listener.trigger(Image_Resize_State.Image_Resize_State_TARGET_IMAGE_CREATION_BEGIN);

		/* creating the target image in memory */
		BufferedImage _target_img = new BufferedImage(_target_width, _target_heght, BufferedImage.TYPE_INT_RGB);

		/* grabbing a graphic handler from target image */
		Graphics2D _g_handler = _target_img.createGraphics();

		/* setting drawing hints */
		set_graphic_hints(_g_handler);

		/* setting fill color */
		_g_handler.setColor(TARGET_IMAGE_BACKGROUND_COLOR);

		/* Filling the whole canvas */
		_g_handler.fillRect(0, 0, _target_width, _target_heght);

		/* sending event to listener about beginning of image resizing */
		_listener.trigger(Image_Resize_State.Image_Resize_State_IMAGE_RESIZING_BEGIN);

		/* Begining resizing, and drawing the source to target image buffer */
		_g_handler.drawImage(_img, 0, 0, _target_width, _target_heght, null);

		/* watermark part */

		/* setting the watermark font, needed to findout glyph accent */
		_g_handler.setFont(new Font(WATERMARK_TEXT_FONT_FAMILY, WATERMARK_TEXT_FONT_STYLE, WATERMARK_TEXT_FONT_SIZE));

		/*
		 * getting the max positive asceding val from baseline of available glyphs.
		 * NOTE: this func may be a little time-consuming, so using a hard-coded value,
		 * or other algs are appreciated
		 */
		int _max_asc_val = _g_handler.getFontMetrics().getMaxAscent();

		/* vars to hold start pos of shadow and foreground text */
		int _shadow_x;
		int _shadow_y;
		int _text_x;
		int _text_y;

		/* find out the max height possible characte */

		/* calc fore and shadow pos */
		/* when shadow should be on bottom-right */
		if (WATERMARK_SHADOW_OFFSET > 0) {
			_shadow_x = WATERMARK_SHADOW_OFFSET;
			_shadow_y = WATERMARK_SHADOW_OFFSET;
			_text_x = 0;
			_text_y = 0;
		}
		/* when shadow should be on rop-left */
		else {
			_shadow_x = 0;
			_shadow_y = 0;
			_text_x = WATERMARK_SHADOW_OFFSET;
			_text_y = WATERMARK_SHADOW_OFFSET;
		}

		/* adding asceding val to positions */
		_shadow_y = _shadow_y + _max_asc_val;
		_text_y = _text_y + _max_asc_val;

		/* setting shadow color to handler */
		_g_handler.setColor(WATERMARK_SHADOW_COLOR);

		/* draw shadow */
		_g_handler.drawString(WATERMARK_TEXT_MESSAGE, _shadow_x, _shadow_y);

		/* setting forground watermark text to handler */
		_g_handler.setColor(WATERMARK_FORE_COLOR);

		/* draw watermark */
		_g_handler.drawString(WATERMARK_TEXT_MESSAGE, _text_x, _text_y);

		/*
		 * Finishing up drawing(if any), and start writing down image stream to given
		 * output
		 */

		/* informing listener, beginning of target image write */
		_listener.trigger(Image_Resize_State.Image_Resize_State_TARGET_IMAGE_IO_WRITE_BEGIN);

		/*
		 * Convert matrix/bitmap to jpeg, as it starts writing it to associated out with
		 * default quality. Seems tt supposed to be 70%
		 */
		/*
		 * about jpeg:
		 * https://docs.oracle.com/javase/7/docs/api/javax/imageio/package-summary.html
		 */
		ImageIO.write(_target_img, TARGET_IMAGE_TYPE, arg_out_result);

		/* telling listener, op has been done */
		_listener.trigger(Image_Resize_State.Image_Resize_State_PROCESS_FINISHED);

	}

	/**
	 * Apply default graphic drawing hints on given arg_g_handler
	 * 
	 * @param arg_g_handler instance to target graphic handler
	 */
	private static void set_graphic_hints(Graphics2D arg_g_handler) {
		/* activating text anti-aliasing for smoother text rendering */
		arg_g_handler.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	}

}
