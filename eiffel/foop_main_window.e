note
	description: "Main window for this application."
	author: "Generated by the Vision Application Wizard."
	date: "$Date: 2023/4/15 20:46:7 $"
	revision: "1.0.1"

class
	FOOP_MAIN_WINDOW

inherit
	EV_TITLED_WINDOW
		redefine
			create_implementation,
			initialize,
			is_in_default_state
		end

	FOOP_CONSTANTS
		export
			{NONE} all
		undefine
			default_create, copy
		end

create
	default_create

feature {NONE} -- Initialization
	create_implementation
			-- Create Implementation.
		do
			Precursor {EV_TITLED_WINDOW}

			build_standard_menu_bar
			set_menu_bar (standard_menu_bar)

			build_main_container
			extend (main_container)
		end

	initialize
			-- Initialize window.
		do
			Precursor {EV_TITLED_WINDOW}

				-- Set actions for menu items.
			close_request_actions.extend (agent request_close_window)
			new_game_menu_item.select_actions.extend (agent request_new_game)
			close_menu_item.select_actions.extend (agent request_close_window)
			about_menu_item.select_actions.extend (agent request_about)

				-- Set the title of the window.
			set_title (Window_title)

				-- Set the initial size of the window.
			set_size (Window_width, Window_height)
		end

	is_in_default_state: BOOLEAN
			-- Is the window in its default state?
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

	new_game_menu_item: EV_MENU_ITEM
			-- "new game" menu item

	close_menu_item: EV_MENU_ITEM
			-- "close" menu item

	about_menu_item: EV_MENU_ITEM
			-- "about" menu item

	help_menu: EV_MENU
			-- "Help" menu for this window (contains About...)

	build_standard_menu_bar
			-- Create and populate `standard_menu_bar'.
		do
			create standard_menu_bar

				-- Add the "game" menu.
			build_game_menu
			standard_menu_bar.extend (game_menu)
				-- Add the "Help" menu.
			build_help_menu
			standard_menu_bar.extend (help_menu)
		ensure
			menu_bar_initialized: standard_menu_bar /= Void
		end

	build_game_menu
			-- Create and populate `game_menu'.
		do
			create game_menu.make_with_text (Menu_game_item)

			create new_game_menu_item.make_with_text (Menu_game_new_item)
			game_menu.extend (new_game_menu_item)

			create close_menu_item.make_with_text (Menu_game_exit_item)
			game_menu.extend (close_menu_item)
		ensure
			game_menu_initialized: game_menu /= Void and new_game_menu_item /= Void and close_menu_item /= Void
		end

	build_help_menu
			-- Create and populate `help_menu'.
		do
			create help_menu.make_with_text (Menu_help_item)

			create about_menu_item.make_with_text (Menu_help_about_item)
			help_menu.extend (about_menu_item)

		ensure
			help_menu_initialized: help_menu /= Void and about_menu_item /= Void
		end

feature {NONE} -- Implementation / Attributes

	main_container: EV_VERTICAL_BOX
			-- Main container (contains all widgets displayed in this window)

	world: EV_MODEL_WORLD
			-- model world

	area: EV_DRAWING_AREA
			-- drawing area

	buffer: EV_PIXMAP
			-- buffer

	projector: EV_MODEL_DRAWING_AREA_PROJECTOR
			-- projector
feature {NONE} -- Implementation

	build_main_container
			-- Create and populate `main_container'.
		local
			l_bg: EV_PIXMAP
			l_bg_pic: EV_MODEL_PICTURE
		do
			create main_container

			create world
			create area
			create buffer.make_with_size (window_width, window_height)
			create projector.make_with_buffer (world, buffer, area)

				-- add the drawing area to the container
			main_container.extend (area)

				-- Create the background pixmap
			create l_bg
			l_bg.set_with_named_file (file_system.pathname_to_string (img_background))

				-- Create background image for model world (base on l_bg pixmap)
			create l_bg_pic.make_with_pixmap (l_bg)

				-- Add background to the world
			world.extend (l_bg_pic)

				-- Refresh the drawing area
			projector.project
		ensure
			main_container_created: main_container /= Void and world /= Void and area /= Void and buffer /= Void and projector /= Void
		end

feature {NONE} -- Events

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
			l_game: FOOP_GAME
			l_pixmap_cat: EV_PIXMAP
			l_pixmap_mouse: EV_PIXMAP
			l_pixmap_entrance: EV_PIXMAP
		do
				-- cat image
			create l_pixmap_cat
			l_pixmap_cat.set_with_named_file (file_system.pathname_to_string (img_cat))

				-- mouse image
			create l_pixmap_mouse
			l_pixmap_mouse.set_with_named_file (file_system.pathname_to_string (img_mouse))

				-- entry image
			create l_pixmap_entrance
			l_pixmap_entrance.set_with_named_file (file_system.pathname_to_string (img_entrance))

			create l_game.make (world, projector, l_pixmap_cat, l_pixmap_mouse, l_pixmap_entrance)
		end

	request_about
			-- Display the About dialog.
		local
			about_dialog: FOOP_ABOUT_DIALOG
		do
			create about_dialog
			about_dialog.show_modal_to_window (Current)
		end

end