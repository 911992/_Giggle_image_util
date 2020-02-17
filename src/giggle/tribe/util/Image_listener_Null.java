/**
 * 
 */
package giggle.tribe.util;

/**
 * @author Arash
 * 
 *         On Feb 16, 2020 2:31 AM
 * @version 20200215 (Feb 15, 2020)
 *
 *          This class acts an null-object , as an Image_Resize_Listener
 */
public class Image_listener_Null implements Image_Resize_Listener {

	/* Global shared instance to be used */
	public static final Image_listener_Null GLOBAL_INSTANCE = new Image_listener_Null();

	public Image_listener_Null() {

	}

	/* func skeleton from Image_Resize_Listener interface */
	public void trigger(Image_Resize_State arg_event_type) {
		/* nothing really */
	}

}
