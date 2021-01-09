# This is that one minecraft server's description
 so this is all about my server and how im ruining the standart minecraft experience

its just hardmode 0 spawn protection natural regeneration world border far pregenerated chunks
with
im using spigot-api and eclipse as an ide. server running on paper. got pregenerated 10k overworld. 

helloworld:
description: says hello to the world. make proxychat limits next.
usage: /helloworld

heal:
description: heal yourself  or another, prioritizing others.
usage: /heal

eatdirt:
description: look at a dirt block and type /eatdirt to eat dirt. it will take away your EGO
usage: /eatdirt
eatsand:
description: look at a sand block and type /eatsand to eat sand. it will take away your EGO
usage: /eatsand

tellmemore:
description: tells more
amiworthy:
description: evaluates your efforts

usage: /blockrace <start,yes,ready,score,cancel,ruleset,help,set<rules,blocksperrace,minutesperblock,prep>,xp,rejoin>
blockrace start:
description: spams chat to get players involved in blockrace. auto involves the one who starts it
usage: /blockrace start
blockrace yes:
description: accepts blockrace invite
usage: /blockrace yes
blockrace ready:
description: remmembers location and the amount of exp the player has
usage: /blockrace ready
blockrace score:
description: messages you the latest score on the race
usage: /blockrace score
blockrace cancel:
description: op command to kill the race. woohoo!
usage: /blockrace cancel
blockrace set rules:
description: sets a ruleset for blockrace. just plain text
usage: /blockrace set rules
blockrace help:
description: gives racers critical info
usage: /blockrace help
blockrace set blocksperrace:
 description: set how many rounds there are
 usage: /blockrace set blocksperrace <rounds>
blockrace set minutesperblock:
 description: set how long before lose
 usage: /blockrace set minutesperblock <minutes>
blockrace set preptime:
 description: set prep time - it's a short period for players in the server to do /blockraceyes and join the race
 usage: /blockrace set preptime <seconds>
blockrace rejoin:
 description: a small helping hand, when a player disconnects for any reason while racing and relogs while the race is still going on. 
 usage: /blockrace rejoin
blockracetesttp:
description: test a tp function. is it safe, how does it break?
usage: /blockracetesttp
permission: blockrace.tests
permission-message: You don't have the tester permission.
blockracetestdetection:
description: An example command tests area for GRASS_BLOCK
usage: /blockracetestdetection
permission: blockrace.tests
permission-message: You don't have the tester permission.


name: RandomRespawnsanddrugs
description:  training to respawn and handout drugs


name: killOP
description: welcome to blocknarchy, blockplucker
commands:
killop:
	description: kills every online op and deops them on the server
	usage: /killop

name: logdeath
version: 1.21
description:  logs player deaths for bugs and "pls give me back my shit" purposes. i do write many bugs that kill lately. also fuck authme. 
api-version: 1.16


