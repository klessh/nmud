array1 = {
"страсто",	"клено",	"плодо",	"корне",	"угло", 
"дубо", 	"глазо",	"низко",	"желчно",	"камне", 
"глино",	"земле",	"шизо"}

array2 = {
"цвет",		"лист",		"плод",		"лом",		"лов",
"ед",		"глаз",		"глазик",	"рост", 	"дрюм",
"шхим"}

array3 = {
"обыкновенный",	"дикий",	"домашний",		"красный",	"синий",
"белый",		"зеленый",	"фиолетовый",	"красивый",	"грязевой",
"горный",		"душистый"}

function doit()
  i = math.floor(math.random()*(#array1)+1);
  j = math.floor(math.random()*(#array2)+1);
  k = math.floor(math.random()*(#array3)+1);

  word1 = array1[i];
  word2 = array2[j];
  word3 = array3[k];

  return word1..word2.." "..word3;
end

function chat()
	client:send(doit());
end

function help(...)
	client:send("Команда: grow\n\tЭто тестовый скрипт генерации названий растений. Придуман в 15'м году.");
end
