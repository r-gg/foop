note
	description: "Main window for this application."
	author: "Generated by the Vision Application Wizard."
	date: "$Date: 2023/4/15 20:46:7 $"
	revision: "1.0.1"

class
	FOOP_PLAYER

inherit
	EV_MODEL_MOVE_HANDLE

create
	list_make,
	make_with_pixmap

feature {NONE} -- Implementation

	make_with_pixmap (pixmap: EV_PIXMAP)
		local
			l_hex: EV_MODEL_PICTURE
		do
			default_create
			create l_hex.make_with_pixmap (pixmap)
			extend (l_hex)
			l_hex.set_point_position (0, 0)

			enable_move
			enable_events_sended_to_group
		end

feature {NONE} -- Move features

	on_pointer_motion_on_world (ax, ay: INTEGER; x_tilt, y_tilt, pressure: DOUBLE; a_screen_x, a_screen_y: INTEGER)
		do
			if has_capture then
				print ("Dragged tile view: (" + ax.out + "," + ay.out + ")%N")
				set_point_position (ax, ay)
			end
		end

	on_pointer_button_press_on_world (ax, ay, button: INTEGER; x_tilt, y_tilt, pressure: DOUBLE; a_screen_x, a_screen_y: INTEGER)
		do
			enable_capture
		end

	on_pointer_button_release_on_world (ax, ay, button: INTEGER; x_tilt, y_tilt, pressure: DOUBLE; a_screen_x, a_screen_y: INTEGER)
		do
			if has_capture then
				disable_capture
			end
		end

feature {NONE} -- Status setting

	enable_move
		do
			pointer_motion_actions.extend (agent on_pointer_motion_on_world)
			pointer_button_press_actions.extend (agent on_pointer_button_press_on_world)
			pointer_button_release_actions.extend (agent on_pointer_button_release_on_world)
		end

end
