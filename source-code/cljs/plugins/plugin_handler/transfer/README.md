
# XXX#1467
# Milyen adatokat fogad a plugin transfer modulja?
  clj/plugins.plugin-handler/transfer/README.md



# XXX#8173
# Mire való a plugin body komponensének transfer-id tulajdonsága?
  A plugin body komponensének transfer-id tulajdonságának használatával, lehetséges
  az egyes pluginokat egy időben, több példányban is megjeleníteni, úgy,
  hogy mindössze egy darab szerver-oldali kezelőt szükséges inicializálni
  az eltérő azonosítójú és más-más kliens-oldali tulajdonságokkal használt
  példányok mellé.

  A plugin szerver-oldalról, kliens-oldalra küldött tulajdonságainak eltárolásakor
  a szerver-oldali plugin-id (pl. editor-id, lister-id, ...) azonosítóval különbözeti
  meg a többi szerver-oldali plugin-példány tulajdonságaitól.

  Ha a plugin kliens-oldali és szerver-oldali plugin-id azonosítója ...
  ... megegyezik, akkor a plugin kliens-oldali kezelője alapértelmezés szerint
      a kliens-oldali plugin-id azonosítóval hozzáfér ezekhez a tulajdonságokhoz.
  ... NEM egyezik meg, akkor a plugin kliens-oldali kezelőjének szüksége van
      a szerver-oldali plugin-id azonosítóra, hogy hozzáférjen ezekhez
      a tulajdonságokhoz, ezért azt a plugin body komponensének transfer-id
      tulajdonságaként szükséges átadni.
      Pl.: Az egyes pluginokat lehetséges úgy használni, hogy egy darab szerver-oldali
           kezelő van inicializálva (pl. [:item-picker/init-picker! :my-picker {...}]),
           miközben a kliens-oldalon egy időben egyszerre több példány jelenik meg,
           különböző kliens-oldali beállításokkal, amik miatt szükséges különböző
           kliens-oldali plugin-id azonosítókat használni.

  Ha az esetlegesen egy időben, egyszerre több példányban megjelenő plugin szükség
  szerint – a body komponens transfer-id tulajdonságaként – megkapja a szerver-oldalon
  a plugin inicializálásakor használt azonosítót, akkor a body komponens ...
  ... React-fába csatolódása UTÁN a plugin számára elérhetővé válik a transfer-id
      azonosító, amivel hozzáfér a plugin szerver-oldali tulajdonságaihoz.
  ... React-fába csatolódása ELŐTT a plugin számára még nem elérhető a transfer-id
      azonosító, így addig nem fér hozzá a plugin szerver-oldali tulajdonságaihoz!
      A plugin body komponensének React-fába csatolódásáig a transfer-id használata
      nem működik!
