note
	description: "Summary description for {GAME}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	GAME

inherit
	EXECUTION_ENVIRONMENT

create
	make

feature {NONE} -- Initialization

	make
			-- Create player
		do
			create player
		end

feature -- Implementation
	start
			--
			-- Start the game loop
		do
			create player
			run
		end

feature {NONE} -- Implementation
	cnt: INTEGER = 0

	run
			-- Implementation of the game loop
		do
			print ("Hello World%N")

			from
			until
				is_game_over
			loop
				input
				update
				render
			end
		end

	is_game_over: BOOLEAN
			-- Check whether the game is over
		do
			Result := True
		end

	input
			-- Get input of user
		do
				-- TODO call keyboardhandler to get input
		end

	update
			-- Update player and mice
		do
				-- TODO player.update()
		end

	render
			-- Render new game state
		do
				-- TODO call MAIN_WINDOW to re-render
		end

feature {NONE} -- Implementation

	player: PLAYER
			-- player

end
