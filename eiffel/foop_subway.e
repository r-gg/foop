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
			entrance: FOOP_ENTRANCE
		do
			
		end


feature {NONE} -- Attributes

	entrances: LIST [FOOP_ENTRANCE]
			-- entrances of the subway

end
