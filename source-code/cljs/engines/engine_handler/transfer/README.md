
### What kind of data is sent by the transfer.clj file?

###### XXX#1467 (source-code/clj/engines/engine_handler/transfer/README.md)  

### Mire való az engine body komponensének transfer-id tulajdonsága?

###### XXX#8173

Az engine body komponensének transfer-id tulajdonságának használatával, lehetséges
az egyes engine-eket egy időben, több példányban is megjeleníteni, úgy,
hogy mindössze egy darab szerver-oldali kezelőt szükséges inicializálni
az eltérő azonosítójú és más-más kliens-oldali tulajdonságokkal használt
példányok mellé.

Az engine szerver-oldalról, kliens-oldalra küldött tulajdonságainak eltárolásakor
a szerver-oldali engine-id (pl. editor-id, lister-id, ...) azonosítóval különbözeti
meg a többi szerver-oldali engine-példány tulajdonságaitól.

Ha az engine kliens-oldali és szerver-oldali engine-id azonosítója ...
... megegyezik, akkor az engine kliens-oldali kezelője alapértelmezés szerint
    a kliens-oldali engine-id azonosítóval hozzáfér ezekhez a tulajdonságokhoz.
... NEM egyezik meg, akkor az engine kliens-oldali kezelőjének szüksége van
    a szerver-oldali engine-id azonosítóra, hogy hozzáférjen ezekhez
    a tulajdonságokhoz, ezért azt az engine body komponensének transfer-id
    tulajdonságaként szükséges átadni.
    Pl.: Az egyes engine-ekat lehetséges úgy használni, hogy egy darab szerver-oldali
         kezelő van inicializálva (pl. [:item-picker/init-picker! :my-picker {...}]),
         miközben a kliens-oldalon egy időben egyszerre több példány jelenik meg,
         különböző kliens-oldali beállításokkal, amik miatt szükséges különböző
         kliens-oldali engine-id azonosítókat használni.

Ha az esetlegesen egy időben, egyszerre több példányban megjelenő engine szükség
szerint – a body komponens transfer-id tulajdonságaként – megkapja a szerver-oldalon
az engine inicializálásakor használt azonosítót, akkor a body komponens ...
... React-fába csatolódása UTÁN az engine számára elérhetővé válik a transfer-id
    azonosító, amivel hozzáfér az engine szerver-oldali tulajdonságaihoz.
... React-fába csatolódása ELŐTT az engine számára még nem elérhető a transfer-id
    azonosító, így addig nem fér hozzá az engine szerver-oldali tulajdonságaihoz!
    Az engine body komponensének React-fába csatolódásáig a transfer-id használata
    nem működik!
