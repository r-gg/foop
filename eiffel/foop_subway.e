note
	description: "Summary description for {FOOP_SUBWAY}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	FOOP_SUBWAY

create
	make

feature {NONE} -- Initialization

	make
		-- create a subway with 2 entrances
		local
			l_entrance: FOOP_ENTRANCE
			l_entrances: LINKED_LIST[FOOP_ENTRANCE]
		do
			create l_entrances.make
			entrances := l_entrances
		end


feature {NONE} -- Attributes

	entrances: LIST [FOOP_ENTRANCE]
			-- entrances of the subway

end
