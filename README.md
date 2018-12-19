# simulation-proie-predateur
projet d'info 2A INSA

## Consignes
* rendu le mardi 8 janvier
* donner le code source dans un zip avec les noms de binômes
* si plusieurs versions (si touttes les fonctionnalités ne marchent pas dans toutes)

Le mercredi 9 janvier : démonstration des codes, on raconte ce qu'on a fait
avec un diapo (ce qu'on a fait/essayé de faire, retour sur Lotka-Voltera)
10 minutes+5 minutes de questions


## Lancer le programme
Pour éviter que ça consomme 1Go de RAM, il faut le lancer avec `java -Xmx100M Affichage`. Ça marche même avec 25M de RAM, par contre on ne peut pas zoomer beaucoup.

Sous Linux, on peut compiler et lancer avec `ant` (disponible avec `sudo apt install ant`).

## Vidéo
utilier `ffmpeg -r 30 -f image2 -s 500x500 -i %09d.png -vcodec libx264 -crf 25  -pix_fmt yuv420p test.mp4`
si on zoome pendant la capture, il va y avoir des truc bizarres, mais la vidéo aura toujours la même taille.

ffmpeg -r 60 -f image2 -s 500x500 -i "simul-Thu Nov 15 18:35:23 CET 2018/*.png" -vcodec libx264 -crf 25  -pix_fmt yuv420p "simul-Thu Nov 15 18:35:23 CET 2018.mp4"