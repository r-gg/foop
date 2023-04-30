note
	description: "Summary description for {FOOP_MOUSE}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	FOOP_MOUSE

inherit
	EV_MODEL_MOVE_HANDLE
		redefine
			default_create
		end

	FOOP_CONSTANTS
		export
			{NONE} all
		undefine
			default_create, copy
		end

create
	list_make,
	make_with_pixmap

feature {NONE} -- Initialization

	default_create
		do
			create random.make
			random.start

			goal_x := 0
			goal_y := 0

			Precursor {EV_MODEL_MOVE_HANDLE}
		end

	make_with_pixmap (i_x, i_y, i_goal_x, i_goal_y: INTEGER i_pixmap: EV_PIXMAP)
			-- create a mouse with a pixmap
		local
			l_hex: EV_MODEL_PICTURE
			l_random_number: INTEGER
		do
			default_create

			goal_x := i_goal_x
			goal_y := i_goal_y

			create l_hex.make_with_pixmap (i_pixmap)
			extend (l_hex)
			l_hex.set_point_position (0, 0)

			set_point_position (i_x, i_y)
		end

feature {NONE} -- Attributes
	random: RANDOM
			-- random generator

	goal_x: INTEGER
			-- x coordinate of goal

	goal_y: INTEGER
			-- y coordinate of goal

	subway: detachable FOOP_SUBWAY
			-- subway if currently in

	last_subway: detachable FOOP_SUBWAY
			-- last subway

	time_in_subway: detachable INTEGER
			-- time to spend in subway

	time_spent_in_subway: detachable INTEGER
			-- time already spent in subway
feature -- Implementation
	move_random
			-- move mouse by one position
		local
			l_random_number: INTEGER
			l_x: INTEGER
			l_y: INTEGER
		do
			if attached subway as s then
				if time_spent_in_subway = time_in_subway then
					random.forth
					l_random_number := random.item
					if (l_random_number \\ 2) = 0 then
						l_x := s.entrance_begin.x
						l_y := s.entrance_begin.y

						set_x (l_x)
						set_y (l_y)
					else
						l_x := s.entrance_end.x
						l_y := s.entrance_end.y

						set_x (l_x)
						set_y (l_y)
					end

					last_subway := subway
					subway := Void
				else
					time_spent_in_subway := time_spent_in_subway + 1
				end
			else

				random.forth
				l_random_number := random.item
				if (l_random_number \\ 3) /= 0 then
					if x /= goal_x then
						if x < goal_x then
							set_x (x + 1)
						else
							set_x (x - 1)
						end
					end

					if y /= goal_y then
						if y < goal_y then
							set_y (y + 1)
						else
							set_y (y - 1)
						end
					end
				else
					random.forth
					l_random_number := random.item
					if (l_random_number \\ 2) = 0 then
						if x < (window_width - 200) then
							set_x (x + 1)
						end
					else
						if x > 0 then
							set_x (x - 1)
						end
					end

					random.forth
					l_random_number := random.item
					if (l_random_number \\ 2) = 0 then
						if y < (window_height - 200) then
							set_y (y + 1)
						end
					else
						if y > 0 then
							set_y (y - 1)
						end
					end
				end
			end

			set_center()
		end

	enter_subway (i_subway: FOOP_SUBWAY)
		local
			l_random_number: INTEGER
		do
			if i_subway /= last_subway then
				subway := i_subway

				random.forth
				l_random_number := random.item

				time_in_subway := l_random_number \\ 10
				time_spent_in_subway := 0
			end
		end

end
