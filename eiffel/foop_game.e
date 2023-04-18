note
	description: "Summary description for {FOOP_GAME}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	FOOP_GAME

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

			build_subways (i_img_entrance)
			spawn_player (i_img_cat)
			spawn_mice (i_img_mouse)

			projector.project
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
feature {NONE} -- Implementation

	build_subways (i_img_entrance: EV_PIXMAP)
			-- build subways
		local
			l_random: RANDOM
			l_random_number: INTEGER_32
		do
			create l_random.make
			l_random_number := l_random.item
		end

	spawn_player (i_img_cat: EV_PIXMAP)
			-- spawn the palyer
		do
			create player.make_with_pixmap (i_img_cat)

			world.extend (player)
			player.set_point_position (40, 100)
		end

	spawn_mice (i_img_mouse: EV_PIXMAP)
			-- spawn some mice
		do

		end

end

