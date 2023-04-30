note
	description: "Summary description for {FOOP_ENTRANCE}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	FOOP_ENTRANCE

inherit
	EV_MODEL_MOVE_HANDLE

create
	list_make,
	make_with_pixmap

feature {NONE} -- Initialization

	make_with_pixmap (i_x, i_y: INTEGER i_pixmap: EV_PIXMAP)
			-- create a player with a pixmap
		local
			l_hex: EV_MODEL_PICTURE
		do
			default_create

			create l_hex.make_with_pixmap (i_pixmap)
			extend (l_hex)
			l_hex.set_point_position (0, 0)

			set_point_position (i_x, i_y)
		end

end
