/**
 * 
 */
package giggle.tribe.util;

import java.io.Serializable;

/**
 * @author Arash
 * 
 *         On Feb 15, 2020 18:19
 * @version 20200215 (Feb 15, 2020)
 * 
 *          Holds an image processing config. Use the hosted {@code Builder}
 *          class, to instancing, and cloning. This class is immutable, and is
 *          safe to be used in multithread env.
 * @see Image_Util
 */
public class Image_Process_Setup implements Serializable {

	/**
	 * States the minimum possible target scaled image width/height(in pixel) with
	 * fit policy
	 */
	public static final int MIN_SCALE_FIT_WIDTH_HEIGHT = 129;

	/**
	 * States the maximum possible target scaled image width/height(in pixel) with
	 * fit policy
	 */
	public static final int MAX_SCALE_FIT_WIDTH_HEIGHT = 7681;

	/**
	 * Statest he minimum scale ratio for ratio scale image resize policy
	 */
	public static final float MIN_SCALE_RATIO = 0.1147f;

	/**
	 * Statest he minimum scale ratio for ratio scale image resize policy
	 */
	public static final float MAX_SCALE_RATIO = 1.991992f;

	/**
	 * generated serial id to avoid unexpectd erro on unmarshalling
	 */
	private static final long serialVersionUID = -494623724313121036L;

	/**
	 * Tells how to scale up/down the image
	 */
	private Image_Resize_Mode resize_policy;

	/**
	 * holds the maximum possible image width to be scaled to
	 */
	private int scale_fit_width;

	/**
	 * holds the maximum possible image height to be scaled to
	 */
	private int scale_fit_height;

	/**
	 * Holds the ratio scale when policy is set to ImageResizeMode_SCALE_RATIO
	 */
	private float scale_ratio;

	/**
	 * Specifies if the target image need to carry a watermark
	 */
	private boolean with_watermark;

	/**
	 * indicates if the image enlarging is one have to be done. For example if image
	 * is smaller than appreciated target size, so ignore resizing, and let the
	 * target client does image enlarging itself Note: ignoring is no affective(take
	 * a check) when ratio scale image resize policy is set. By default it's true
	 */
	private boolean ignore_image_enlarging = true;

	/**
	 * indicates if the current config with associated object state is applicable.
	 * null value indicates unknown state
	 */
	private transient volatile Boolean has_applicable_state = null;

	/**
	 * Holds the message related to object being-applicable conflict. It's filled by
	 * is_applicable method
	 * 
	 * @see is_applicable(void):boolean
	 */
	private transient volatile String instance_conflict_message;

	/**
	 * Checks if the current object state is an applicable image resize thing. This
	 * function is not thread safe, use is_applicable_sync when thread-safety is an
	 * essential For any conflict, it will place the reason as a human-firndly
	 * string message which could be found at instance_conflict_message
	 * 
	 * @return true if the current instance has a valid state for image resizing,
	 *         otherwise false
	 * 
	 * @see is_applicable_sync(void):boolean
	 * @see instance_conflict_message
	 */
	public boolean is_applicable() {
		/*
		 * check if state has already checked, since the instance is immutable, so cache
		 * would work for sure
		 */
		if (has_applicable_state != null) {
			return has_applicable_state;
		}

		/* temp var to hold the result , by default let's assume it's okay */
		boolean _res = true;
		switch (this.resize_policy) {
			/* when resizing is not an action */
			case ImageResizeMode_NONE: {
				/* if no watermark, and no resize, so what to do then? */
				if (this.with_watermark == false) {
					/* setting the conflict error message */
					instance_conflict_message = "[error 1024] No watermark, and no resize. Nothing to do";

					_res = false;
				}
				break;
			}
			/* when scale to a constant width/height is desired */
			case ImageResizeMode_SCALE_FIT: {
				/* local var to hold if width is in range or not */
				boolean _width_in_range;

				/* local var to state if width needs to be placed in calculating or not */
				boolean _width_has_place;

				/* local var to hold if height is in range or not */
				boolean _height_in_range;

				/* local var to state if height needs to be placed in calculating or not */
				boolean _height_has_place;

				/* check if width can be join in calculating */
				if (this.scale_fit_width > 0) {
					_width_has_place = true;

					/* check if width is in bound of width, between min and max, if not */
					if (this.scale_fit_width < MIN_SCALE_FIT_WIDTH_HEIGHT
							|| this.scale_fit_width > MAX_SCALE_FIT_WIDTH_HEIGHT) {

						_width_in_range = false;
					} else {
						_width_in_range = true;
					}
				} else {
					_width_has_place = false;
					_width_in_range = false;
				}

				/* check if height can be join in calculating */
				if (this.scale_fit_height > 0) {
					_height_has_place = true;

					/* check if the height is in bound */
					if (this.scale_fit_height < MIN_SCALE_FIT_WIDTH_HEIGHT
							|| this.scale_fit_height > MAX_SCALE_FIT_WIDTH_HEIGHT) {

						_height_in_range = false;
					} else {
						_height_in_range = true;
					}
				} else {
					_height_has_place = false;
					_height_in_range = false;
				}

				/* all states in one int with am asked val, for easier check */
				int _masked_res = (_width_has_place ? (1 << 0) : 0) | (_height_has_place ? (1 << 1) : 0)
						| (_width_in_range ? (1 << 2) : 0) | (_height_in_range ? (1 << 3) : 0);

				/* check if neither width or heigh has take in place */
				if ((_masked_res & 0x3) == 0) {
					/* setting the conflict error message */
					instance_conflict_message = "[error 2048] cannot mark width and height as non-effective together";

					_res = false;
				} else {
					/* check if applied width has valid val if present */
					if (((_masked_res & 0x1) == 0x1) && ((_masked_res & 0x5) != 0x5)) {
						/* setting the conflict error message */
						instance_conflict_message = "[error 3072] Out of range for applied width value";

						_res = false;
					} else if (((_masked_res & 0x2) == 0x2) && ((_masked_res & 0xa) != 0xa)) {
						/* setting the conflict error message */
						instance_conflict_message = "[error 4096] Out of range for applied height value";

						_res = false;
					}
				}

				break;
			}
			/* when scaling to a percent/ratio */
			case ImageResizeMode_SCALE_RATIO: {
				if (this.scale_ratio < MIN_SCALE_RATIO || this.scale_ratio > MAX_SCALE_RATIO) {

					/* setting the conflict error message */
					instance_conflict_message = "[error 5120] Out of range associated scale ratio value";

					_res = false;
				}
				break;
			}

		}/* end switch */

		/* set the cache var about object applicable state */
		this.has_applicable_state = new Boolean(_res);

		/* returning result */
		return this.has_applicable_state;
	}

	/**
	 * Returns the message was associated to instance_conflict_message by method
	 * is_applicable. If it returns null, it means either object has a
	 * correct/valid, or no any message could be associated(bug) Note: this function
	 * calls is_applicable before return anything in case to make sure message get's
	 * filled correctly
	 * 
	 * @return instance_conflict_message
	 * @see is_applicable(void):boolean
	 */
	public String get_instance_state_conflict_message() {
		is_applicable();
		return this.instance_conflict_message;
	}

	/**
	 * Calls the is_applicable function, in a safe/locked manner. Where current
	 * object gets locked.
	 * 
	 * @return true if the current instance has a valid state for image resizing,
	 *         otherwise false
	 * 
	 * @see is_applicable(void):boolean
	 */
	public synchronized boolean is_applicable_sync() {
		return is_applicable();
	}

	/* getters */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Image_Resize_Mode getResize_policy() {
		return resize_policy;
	}

	public int getScale_fit_width() {
		return scale_fit_width;
	}

	public int getScale_fit_height() {
		return scale_fit_height;
	}

	public float getScale_ratio() {
		return scale_ratio;
	}

	public boolean isWith_watermark() {
		return with_watermark;
	}

	public boolean isIgnore_image_enlarging() {
		return ignore_image_enlarging;
	}

	/* setters */
	private void setResize_policy(Image_Resize_Mode resize_policy) {
		this.resize_policy = resize_policy;
	}

	private void setScale_fit_width(int scale_fit_width) {
		this.scale_fit_width = scale_fit_width;
	}

	private void setScale_fit_height(int scale_fit_height) {
		this.scale_fit_height = scale_fit_height;
	}

	private void setScale_ratio(float scale_ratio) {
		this.scale_ratio = scale_ratio;
	}

	private void setWith_watermark(boolean with_watermark) {
		this.with_watermark = with_watermark;
	}

	private void setIgnore_image_enlarging(boolean ignore_image_enlarging) {
		this.ignore_image_enlarging = ignore_image_enlarging;
	}

	/**
	 * For specifying resize mode policy
	 *
	 * @see Builder
	 */
	public enum Image_Resize_Mode {
		/**
		 * No any image resizing, use the same demi as input arg
		 */
		ImageResizeMode_NONE,

		/**
		 * Keeps the image aspect ratio, and scale it down/up to given associated scale
		 * ratio(percent)
		 */
		ImageResizeMode_SCALE_RATIO,

		/**
		 * Keeps the image aspect ratio, and scale the image down/up to fit either as
		 * width or height without overlay
		 */
		ImageResizeMode_SCALE_FIT,
	};

	public static class Builder {

		/**
		 * private state var to be returned later
		 */
		private Image_Process_Setup _private_state;

		/**
		 * No any default const.
		 */
		private Builder() {
			/* init the local private instance */
			_private_state = new Image_Process_Setup();
		}

		/**
		 * sets the resize policy to ImageResizeMode_NONE, and setting watermark to true
		 * Call this only from class type. Calling it from an instance might lead to a
		 * state consistency, and redundant memory allocation.
		 * 
		 * @return a statefull Builder instance
		 */
		public static Builder no_scale_with_watermark() {
			/* new instance of builder */
			Builder _ins = new Builder();

			/* marking the scale policy to none */
			_ins._private_state.setResize_policy(Image_Resize_Mode.ImageResizeMode_NONE);

			/* setting the placing watermark to true */
			_ins._private_state.setWith_watermark(true);

			return _ins;
		}

		/**
		 * sets the resize policy to ImageResizeMode_SCALE_RATIO, and setting watermark
		 * to true Call this only from class type. Calling it from an instance might
		 * lead to a state consistency, and redundant memory allocation.
		 * 
		 * @param arg_scale scale value to be applied. Value 0.0 means 0%, and 1.0 as
		 *                  100%
		 * @return builder instance
		 */
		public static Builder ratio_scale(float arg_scale) {
			/* new instance of builder */
			Builder _ins = new Builder();

			/* marking the scale policy to scale ratio */
			_ins._private_state.setResize_policy(Image_Resize_Mode.ImageResizeMode_SCALE_RATIO);

			/* setting required sacle value */
			_ins._private_state.setScale_ratio(arg_scale);

			/* setting the placing watermark to true */
			_ins._private_state.setWith_watermark(true);

			/* return the builder instance */
			return _ins;
		}

		/**
		 * sets the resize policy to ImageResizeMode_SCALE_FIT, and sets the given vals
		 * for appreciated width or height target size. Note: final image will have same
		 * aspect ratio as source file has. Note: If given width arg is set to zero
		 * or(negative), it means ignoring max check for target image. Same about height
		 * 
		 * @param arg_width  the appreciated target image width, set it to zero(0)
		 *                   meaning take the height first (ignore width)
		 * @param arg_height the appreciated target image height, set it to zero(0)
		 *                   meaning take the weight first (ignore height)
		 * @return builder instance
		 */
		public static Builder fit_scale(int arg_width, int arg_height) {
			/* new instance of builder */
			Builder _ins = new Builder();

			/* marking the scale policy to scale fit */
			_ins._private_state.setResize_policy(Image_Resize_Mode.ImageResizeMode_SCALE_FIT);

			/* setting desired fit width */
			_ins._private_state.setScale_fit_width(arg_width);

			/* setting desired fit height */
			_ins._private_state.setScale_fit_height(arg_height);

			/* returning the builder instance */
			return _ins;
		}

		/**
		 * sets the scale ratio from given argument. Note: calling this function would
		 * be effective if and if image resize policy is set to
		 * ImageResizeMode_SCALE_RATIO
		 * 
		 * @param arg_scale image scale need to applied (1.0 means 100%, the same size)
		 * @return this builder instance
		 */
		public Builder scale_ratio(float arg_scale) {
			/* setting the scale to associated image resize conf instance */
			this._private_state.setScale_ratio(arg_scale);

			/* returning self instance */
			return this;
		}

		public Builder scale_fit(int arg_width, int arg_height) {
			/* setting the given width to associated conf's width */
			this._private_state.setScale_fit_width(arg_width);

			/* setting the given width to associated conf's height */
			this._private_state.setScale_fit_width(arg_width);

			/* returning this ins */
			return this;
		}

		/**
		 * sets the watermark from given argument. The final image would have a
		 * watermark(text label)
		 * 
		 * @param arg_watermark
		 * @return same(this) builder instance
		 * @see Image_Util
		 */
		public Builder watermark(boolean arg_watermark) {
			/* pass the given cong data to associated instance */
			this._private_state.setWith_watermark(arg_watermark);

			/* return same instance */
			return this;
		}

		/**
		 * Sets the policy about resizing, when orginal/source image is smaller than
		 * target one This is only affective, when scale fit resize policy is set
		 * 
		 * @param arg_ignore_image_enlargment
		 * @return self instance
		 */
		public Builder ignore_image_enlargement(boolean arg_ignore_image_enlargment) {
			/* setting the associated att value */
			this._private_state.setIgnore_image_enlarging(arg_ignore_image_enlargment);

			/* return the same instance */
			return this;
		}

		/**
		 * complete the instance state setup and returns the result
		 * 
		 * @return the build state of the ImageProcessSetup
		 */
		public Image_Process_Setup build() {
			return this._private_state;
		}
	}

}
