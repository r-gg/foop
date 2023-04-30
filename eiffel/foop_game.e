note
	description: "Summary description for {FOOP_GAME}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	FOOP_GAME

inherit
	FOOP_CONSTANTS

create
	make

feature {NONE} -- Initialization

	make (i_world: EV_MODEL_WORLD i_projector: EV_MODEL_DRAWING_AREA_PROJECTOR i_img_cat, i_img_mouse, i_img_entrance: EV_PIXMAP)
			-- creation of the game
		local
			l_pixmap: EV_PIXMAP
		do
			world := i_world
			projector := i_projector

			create random.make
			random.start

			goal_x := 500
			goal_y := 500

			spawn_subways (i_img_entrance)
			spawn_player (i_img_cat)
			spawn_mice (i_img_mouse)

			create {WORKER_THREAD} game_runner.make (agent run)
			game_runner.launch
		end

feature {NONE} -- Attributes

	world: EV_MODEL_WORLD
			-- world

	projector: EV_MODEL_DRAWING_AREA_PROJECTOR
			-- projector

	player: FOOP_PLAYER
			-- player

	mice: LIST [FOOP_MOUSE]
			-- all mice

	subways: LIST [FOOP_SUBWAY]
			-- all subways

	game_runner: THREAD
			-- thread for game loop

	playing: BOOLEAN
			-- flag if playing

	random: RANDOM
			-- random generator

	goal_x: INTEGER
			-- x coordinate of goal

	goal_y: INTEGER
			-- y coordinate of goal
feature {NONE} -- Implementation

	spawn_subways (i_img_entrance: EV_PIXMAP)
			-- spawn subways
		local
			l_subway: FOOP_SUBWAY
			l_cnt: INTEGER
			l_x_1: INTEGER
			l_y_1: INTEGER
			l_x_2: INTEGER
			l_y_2: INTEGER
		do
			create {LINKED_LIST [FOOP_SUBWAY]} subways.make

			from
				l_cnt := 0
			until
				l_cnt = 4
			loop
				random.forth
				l_x_1 := random.item \\ window_width

				random.forth
				l_y_1 := random.item \\ window_height

				random.forth
				l_x_2 := random.item \\ window_width

				random.forth
				l_y_2 := random.item \\ window_height

				create l_subway.make_with_pixmap (l_x_1, l_y_1, l_x_2, l_y_2, i_img_entrance)

				subways.extend (l_subway)
				world.extend (l_subway.entrance_begin)
				world.extend (l_subway.entrance_end)

				l_cnt := l_cnt + 1
			end
		end

	spawn_player (i_img_cat: EV_PIXMAP)
			-- spawn the palyer
		do
			create player.make_with_pixmap (i_img_cat)

			world.extend (player)
		end

	spawn_mice (i_img_mouse: EV_PIXMAP)
			-- spawn some mice
		local
			l_mouse: FOOP_MOUSE
			l_cnt: INTEGER
			l_x: INTEGER
			l_y: INTEGER
		do
			create {LINKED_LIST [FOOP_MOUSE]} mice.make

			from
				l_cnt := 0
			until
				l_cnt = 4
			loop
				random.forth
				l_x := random.item \\ window_width

				random.forth
				l_y := random.item \\ window_height

				create l_mouse.make_with_pixmap (l_x, l_y, goal_x, goal_y, i_img_mouse)

				mice.extend (l_mouse)
				world.extend (l_mouse)

				l_cnt := l_cnt + 1
			end
		end

	run
			-- game loop
		local
			l_cnt: INTEGER
		do
			l_cnt := 0

			from
				playing := True
			until
				playing = False
			loop
				if l_cnt \\ 10000 = 0 then
					update
					check_collisions
					check_finished
				end
				l_cnt := l_cnt + 1
			end
		end

	update
			-- update world objects
		local
			l_random_number: INTEGER
		do
			across
				mice as m
			loop
				random.forth
				l_random_number := random.item
				if (l_random_number \\ 2) = 0 then
					m.item.move_random
				end
			end
		end

	check_collisions
			-- check for collisions
		local
			l_mouse: FOOP_MOUSE
			l_subway: FOOP_SUBWAY
			l_player_x: INTEGER
			l_player_y: INTEGER
			l_mouse_x: INTEGER
			l_mouse_y: INTEGER
			l_subway_begin_x: INTEGER
			l_subway_begin_y: INTEGER
			l_subway_end_x: INTEGER
			l_subway_end_y: INTEGER
		do
			l_player_x := player.x
			l_player_y := player.y

			across
				mice as m
			loop
				l_mouse := m.item

				l_mouse_x := l_mouse.x
				l_mouse_y := l_mouse.y

				if (l_player_x - l_mouse_x) < 50 and (l_player_x - l_mouse_x) > -50 and
					(l_player_y - l_mouse_y) < 10 and (l_player_y - l_mouse_y) > -10 then
						-- TODO remove mouse
				end

				across
					subways as s
				loop
					l_subway := s.item

					l_subway_begin_x := l_subway.entrance_begin.x
					l_subway_begin_y := l_subway.entrance_begin.y

					l_subway_end_x := l_subway.entrance_end.x
					l_subway_end_y := l_subway.entrance_end.y

					if ((l_mouse_x - l_subway_begin_x) < 50 and (l_mouse_x - l_subway_begin_x) > -50 and
							(l_mouse_y - l_subway_begin_y) < 50 and (l_mouse_y - l_subway_begin_y) > -50) or
						((l_mouse_x - l_subway_end_x) < 50 and (l_mouse_x - l_subway_end_x) > -50 and
							(l_mouse_y - l_subway_end_y) < 50 and (l_mouse_y - l_subway_end_y) > -50) then

						l_mouse.enter_subway (l_subway)

					end
				end
			end
		end

	check_finished
			-- check if game is finished
		local
			l_mouse: FOOP_MOUSE
			l_subway: FOOP_SUBWAY
			l_mouse_x: INTEGER
			l_mouse_y: INTEGER
		do
			across
				mice as m
			loop
				l_mouse := m.item

				l_mouse_x := l_mouse.x
				l_mouse_y := l_mouse.y

				if goal_x = l_mouse_x and goal_y = l_mouse_y then
						-- TODO display game lost
				end
			end
		end

end

