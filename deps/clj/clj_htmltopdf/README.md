
### Why the clj_htmltopdf is a self-hosted dependecy?

###### XXX#0506

...

###### XXX#5006

A pdf-generator a pdf fájlban felhasznált forrásfájlok (pl. css fájlok, képek, ...)
elérési útvonalait kiírja a terminálra. Ez egyrészt zavaró, másrészt, amikor
egy forrásfájl base64 enkódolással közvetlenül van felhasználva, akkor a terminál
több esetben lefagyott, emiatt szükségessé vált az elérési útvonalak kiíratásait
kikapcsolni.

###### XXX#5007

Milyen más változtatások vannak a clj_htmltopdf könytárban?

@Paul, @Peti
