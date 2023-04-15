note
	description: "Main window for this application."
	author: "Generated by the Vision Application Wizard."
	date: "$Date: 2023/4/15 20:46:7 $"
	revision: "1.0.1"

class
	MAIN_WINDOW

inherit
	EV_TITLED_WINDOW
		redefine
			create_interface_objects,
			initialize,
			is_in_default_state
		end

	INTERFACE_NAMES
		export
			{NONE} all
		undefine
			default_create, copy
		end

create
	default_create

feature {NONE} -- Initialization

	create_interface_objects
			-- <Precursor>
		do
				-- Create main container.
			create main_container
				-- Create the menu bar.
			create standard_menu_bar
				-- Create game menu.
			create game_menu.make_with_text (Menu_game_item)
				-- Create help menu.
			create help_menu.make_with_text (Menu_help_item)
		end

	initialize
			-- Build the interface for this window.
		do
			Precursor {EV_TITLED_WINDOW}

				-- Create and add the menu bar.
			build_standard_menu_bar
			set_menu_bar (standard_menu_bar)

			build_main_container
			extend (main_container)

				-- Execute `request_close_window' when the user clicks
				-- on the cross in the title bar.
			close_request_actions.extend (agent request_close_window)

				-- Set the title of the window.
			set_title (Window_title)

				-- Set the initial size of the window.
			set_size (Window_width, Window_height)
		end

	is_in_default_state: BOOLEAN
			-- Is the window in its default state?
			-- (as stated in `initialize')
		do
			Result := (width = Window_width) and then
				(height = Window_height) and then
				(title.is_equal (Window_title))
		end

feature {NONE} -- Menu Implementation

	standard_menu_bar: EV_MENU_BAR
			-- Standard menu bar for this window.

	game_menu: EV_MENU
			-- "game" menu for this window (contains New, Open, Close, Exit...)

	help_menu: EV_MENU
			-- "Help" menu for this window (contains About...)

	build_standard_menu_bar
			-- Create and populate `standard_menu_bar'.
		do
				-- Add the "game" menu.
			build_game_menu
			standard_menu_bar.extend (game_menu)
				-- Add the "Help" menu.
			build_help_menu
			standard_menu_bar.extend (help_menu)
		ensure
			menu_bar_initialized: not standard_menu_bar.is_empty
		end

	build_game_menu
			-- Create and populate `game_menu'.
		local
			menu_item: EV_MENU_ITEM
		do
			create menu_item.make_with_text (Menu_game_new_item)
			menu_item.select_actions.extend (agent request_new_game)
			game_menu.extend (menu_item)

				-- Create the game/Exit menu item and make it call
				-- `request_close_window' when it is selected.
			create menu_item.make_with_text (Menu_game_exit_item)
			menu_item.select_actions.extend (agent request_close_window)
			game_menu.extend (menu_item)
		ensure
			game_menu_initialized: not game_menu.is_empty
		end

	build_help_menu
			-- Create and populate `help_menu'.
		local
			menu_item: EV_MENU_ITEM
		do
			create menu_item.make_with_text (Menu_help_about_item)
			menu_item.select_actions.extend (agent on_about)
			help_menu.extend (menu_item)

		ensure
			help_menu_initialized: not help_menu.is_empty
		end

feature {NONE} -- About Dialog Implementation

	on_about
			-- Display the About dialog.
		local
			about_dialog: ABOUT_DIALOG
		do
			create about_dialog
			about_dialog.show_modal_to_window (Current)
		end

feature {NONE} -- Implementation, Events

	request_close_window
			-- Process user request to close the window.
		local
			question_dialog: EV_CONFIRMATION_DIALOG
		do
			create question_dialog.make_with_text (Label_confirm_close_window)
			question_dialog.show_modal_to_window (Current)

			if question_dialog.selected_button ~ (create {EV_DIALOG_CONSTANTS}).ev_ok then
					-- Destroy the window.
				destroy

					-- End the application.
				if attached (create {EV_ENVIRONMENT}).application as a then
					a.destroy
				end
			end
		end

	request_new_game
			-- Process user request to create a new game.
		local
			game: GAME
		do
			create game.make
			game.start
		end

feature {NONE} -- Implementation

	main_container: EV_VERTICAL_BOX
			-- Main container (contains all widgets displayed in this window).

	build_main_container
			-- Populate `main_container'.
		do
			main_container.extend (create {EV_TEXT})
		ensure
			main_container_created: main_container /= Void
		end

feature {NONE} -- Implementation / Constants

	Window_title: STRING = "foop"
			-- Title of the window.

	Window_width: INTEGER = 2880
			-- Initial width for this window.

	Window_height: INTEGER = 1800
			-- Initial height for this window.
end
