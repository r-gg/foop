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

	goal_subway: FOOP_SUBWAY
			--  goal subway
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

			goal_subway := subways.at (random.item \\ 4)
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
			l_distance_to_first: REAL_64
			l_distance_to_second: REAL_64
			l_goal_x: INTEGER
			l_goal_y: INTEGER
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

					-- find the nearest entrance of goal subway and set it as goal
				l_distance_to_first := world.distance (goal_subway.entrance_begin.point_x, goal_subway.entrance_begin.point_y, l_x, l_y)
				l_distance_to_second := world.distance (goal_subway.entrance_end.point_x, goal_subway.entrance_end.point_y, l_x, l_y)
				if l_distance_to_first < l_distance_to_second then
					l_goal_x := goal_subway.entrance_begin.point_x
					l_goal_y := goal_subway.entrance_begin.point_y
				else
					l_goal_x := goal_subway.entrance_end.point_x
					l_goal_y := goal_subway.entrance_end.point_y
				end

				create l_mouse.make_with_pixmap (l_x, l_y, l_goal_x, l_goal_y, i_img_mouse)

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
				if l_cnt \\ 100000 = 0 then
					update
					check_collisions
					check_finished
				end
				l_cnt := l_cnt + 1
			end
		end

	update
			-- update world objects
		do
			across
				mice as m
			loop
				if m.item.active = TRUE and m.item.subway /= goal_subway then
					random.forth
					if (random.item \\ 2) = 0 then
						m.item.move_random
					end
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

			from
				mice.start
			until
				mice.exhausted
			loop
				l_mouse := mice.item

				l_mouse_x := l_mouse.x
				l_mouse_y := l_mouse.y

				if (l_player_x - l_mouse_x) < 50 and (l_player_x - l_mouse_x) > -50 and
					(l_player_y - l_mouse_y) < 10 and (l_player_y - l_mouse_y) > -10 then
						l_mouse.deactivate

					from
						world.start
					until
						world.exhausted
					loop
						if world.item = l_mouse then
							world.item.hide
							world.forth
						else
							world.forth
						end
					end

				else
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
				mice.forth
			end
		end

	check_finished
			-- check if game is finished
		local
			l_mouse: FOOP_MOUSE
			l_subway: FOOP_SUBWAY
			l_mouse_x: INTEGER
			l_mouse_y: INTEGER
			l_game_end_text: EV_MODEL_TEXT
			l_all_mice_in_goal_subway: BOOLEAN
			l_all_mice_caught: BOOLEAN
		do
			l_all_mice_caught := True

			across
				mice as m
			loop
				l_mouse := m.item

				if l_mouse.active = TRUE then
					l_all_mice_caught := False
				end
			end

			if l_all_mice_caught then
				create l_game_end_text.make_with_text ("Game Won")
				l_game_end_text.set_point_position (500, 500)
				world.extend (l_game_end_text)
					-- playing := False
			else
				l_all_mice_in_goal_subway := True

				across
					mice as m
				loop
					l_mouse := m.item

					if l_mouse.subway /= goal_subway then
						l_all_mice_in_goal_subway := False
					end
				end

				if l_all_mice_in_goal_subway then
					create l_game_end_text.make_with_text ("Game Lost")
					l_game_end_text.set_point_position (500, 500)
					world.extend (l_game_end_text)
						-- playing := False
				end
			end
		end

end

