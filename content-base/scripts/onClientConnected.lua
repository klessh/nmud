function on()
	showMenu();
	client:send([[
Please, enter number of your language:
	1. Русский
	2. English
]]);
	handleInput("lang_sel");
end

function handleInput(menuname)
	input = client:read();
	
	if menuname = "lang_sel" then
	elseif menuname == "main" then
		if input == "1" then
			russian();
			return;
		elseif input == "2" then
			english();
			return;
		else
			client:send("Number incorrect. Try again");
			handleInput(menuname);
	else
		output = handleInput(menuname);
	end
	
	return;
end

function russian()

end

function english()

end
