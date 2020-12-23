#!/bin/sh
BASE=.

jsonnet $BASE/BlanktopiaItems/src/main/resources/config.jsonnet -o $BASE/BlanktopiaItems/src/main/resources/config.yml

rsync -iavzhP \
	$BASE/Blanktopia/build/libs/Blanktopia-1.0.0.jar \
	$BASE/BlanktopiaCore/build/libs/BlanktopiaCore-1.0.0.jar \
	$BASE/BlanktopiaItems/build/libs/BlanktopiaItems-1.0.0.jar \
	$BASE/BlanktopiaPortals/build/libs/BlanktopiaPortals-1.0.0.jar \
	$BASE/BlanktopiaShop/build/libs/BlanktopiaShop-1.0.0.jar \
	$BASE/BlanktopiaTweaks/build/libs/BlanktopiaTweaks-1.0.0.jar \
	$BASE/BlanktopiaLikes/build/libs/BlanktopiaLikes-1.0.0.jar \
	$BASE/BlanktopiaTutorial/build/libs/BlanktopiaTutorial-1.0.0.jar \
	ubuntu@play.blanktopia.com:~/docker/minecraft/blanktopia/plugins

rsync -iavzhP \
	ubuntu@play.blanktopia.com:~/docker/minecraft/blanktopia/plugins/BlanktopiaItems/config.yml \
	"$BASE/BlanktopiaItems/src/main/resources/config.yml.bak"

rsync -iavzhP \
	"$BASE/BlanktopiaItems/src/main/resources/config.yml" \
	ubuntu@play.blanktopia.com:~/docker/minecraft/blanktopia/plugins/BlanktopiaItems/config.yml

rsync -iavzhP \
	ubuntu@play.blanktopia.com:~/docker/minecraft/blanktopia/plugins/Blanktopia/config.yml \
	"$BASE/Blanktopia/src/main/resources/config.yml.bak"
jsonnet BlanktopiaItems/src/main/resources/config.jsonnet -o BlanktopiaItems/src/main/resources/config.yml
rsync -iavzhP \
	"$BASE/Blanktopia/src/main/resources/config.yml" \
	ubuntu@play.blanktopia.com:~/docker/minecraft/blanktopia/plugins/Blanktopia/config.yml
