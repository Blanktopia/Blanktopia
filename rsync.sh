#!/bin/sh
BASE="."

scp "./Blanktopia/build/libs/Blanktopia-1.2.0.jar" "./BlanktopiaPortals/build/libs/BlanktopiaPortals-1.2.0.jar" "./BlanktopiaTweaks/build/libs/BlanktopiaTweaks-1.2.0.jar" ssh.piggyp.ink:/opt/nomad/volumes/blanktopia/plugins
