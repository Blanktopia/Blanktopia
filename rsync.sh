#!/bin/sh
rsync -iavzhP \
	Blanktopia/build/libs/Blanktopia-1.0.0.jar \
	BlanktopiaCore/build/libs/BlanktopiaCore-1.0.0.jar \
	BlanktopiaItems/build/libs/BlanktopiaItems-1.0.0.jar \
	BlanktopiaPortals/build/libs/BlanktopiaPortals-1.0.0.jar \
	BlanktopiaShop/build/libs/BlanktopiaShop-1.0.0.jar \
	BlanktopiaTweaks/build/libs/BlanktopiaTweaks-1.0.0.jar \
	BlanktopiaLikes/build/libs/BlanktopiaLikes-1.0.0.jar \
	ubuntu@play.blanktopia.com:~/docker/minecraft/blanktopia/plugins
