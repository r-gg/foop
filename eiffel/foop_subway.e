note
	description: "Summary description for {FOOP_SUBWAY}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	FOOP_SUBWAY

inherit
	FOOP_CONSTANTS

create
	make_with_pixmap

feature {NONE} -- Initialization

	make_with_pixmap (x_1, y_1, x_2, y_2: INTEGER pixmap: EV_PIXMAP)
			-- create a subway with 2 entrances
		do
			create entrance_begin.make_with_pixmap (x_1, y_1, pixmap)
			create entrance_end.make_with_pixmap (x_2, y_2, pixmap)
		end

feature -- Attributes
	entrance_begin: FOOP_ENTRANCE
			-- entrance of the subway
	entrance_end: FOOP_ENTRANCE
			-- entrance of the subway
end
