# simulation-proie-predateur
projet d'info 2A INSA

##

## Lancer le programme
Pour éviter que ça consomme 1Go de RAM, il faut le lancer avec `java -Xmx100M Affichage`. Ça marche même avec 25M de RAM, par contre on ne peut pas zoomer beaucoup.

Sous Linux, on peut compiler et lancer avec `ant` (disponible avec `sudo apt install ant`).

## Vicéo
utilier `ffmpeg -r 60 -f image2 -s 500x500 -i %09.png -vcodec libx264 -crf 25  -pix_fmt yuv420p test.mp4`
si on zoome pendant la capture, il va y avoir des truc bizarres, mais la vidéo aura toujours la même taille.

ffmpeg -r 60 -f image2 -s 500x500 -i "simul-Thu Nov 15 18:35:23 CET 2018/*.png" -vcodec libx264 -crf 25  -pix_fmt yuv420p "simul-Thu Nov 15 18:35:23 CET 2018.mp4"