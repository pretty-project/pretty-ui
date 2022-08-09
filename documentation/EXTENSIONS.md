
# Actions

# Calendar

# Charts

# Devices

# Employees

# Inventories
- Több raktárat párhuzamos kezelése / cég
- Külön kell legyen a products modul, ahol termékeket és termékkategóriákat lehet kezelni,
  majd ezeket a termékeket lehet tárolni a raktárakban.

# Jobs

# Machines

# Notifications

# Pages
- extension/pages: page-eket tudsz kezelni.
  Egy page megjelenhet az extensions/main-menu-ben (ha bepipálod). (?ezmitjelent?)
  Megjelenhet sectionként a főoldalon, ha a főoldal megjeleníti az összes page-et.
  Beállíthatsz neki url-t.

- Egy page a köv tipsu sectionokbol allhat: szöveg, kép
  kép: mekkora, absolute vagy relativ (rá lóg a szöveg), pozicio
  kép(rákattintva galériát nyit meg, opcionális)

# Price quotes
https://documento.hu/landing

- Elfogadás visszajelző (az ügyfél elfogadhatja az árajánlatot online)
- Email küldése, amiben egy link van, ahol neten meg lehet nézni az árajánlatot
- PDF-ben letölthető

# Products
- A termékeket lehet group-olni: ez kell a tartozékcsoportokhoz

- És lehet több darab címkét hozzáadni {:product/tags ["..." "..."]}
  {:product/name     "..."
   :product/tags     ["..." "..."]
   :product/price    123}

- Meg kell oldani, hogy az árajánlat tudja, hogy egy termék jármű-e vagy kiegészítő!
  A products-ban lehet hozzáadni, új tulajdonságokat séma-editorral de ha ez is hozzáadható, akkor
  van egy emberi tényező ami veszélyes, mert az árjaánlatnak pontosan kell tudni, hogy melyik a primary-termék
  szal melyik maga a jármű

- A webhely a termékkategóriák alapján fogja felsorolni, hogy milyen termékek jelennek meg a kategóriákban
- A termékkategóriák és alkategóriák külön collection (mint a directories).

- Hogyan kapcsoljuk egy járműhöz a tartozékokat boolx-esen? A webhelyen felkell sorolni a választható
  tartozékokat.

- Modell/típus: product/subproduct

- product-categories collection
- products           collection
- product-groups     collection

# Services

# Vehicles
- Flottakezelo extension

- Jarmuvek adatai
- Pl.: Muszaki ervenyesseg
- Szerviztortenet
- Fotot is fel lehet tölteni (pl forgalmi, egyéb dokumentumok scan-jei)
- A szerviztortenetben barmelyik bejegyzeshez beallithatod, hogy x km vagy x ido
  elteltevel ujra figyelmeztessen
- Barki aki vezet egy ceges autot, elokapja a telefonjat ...
  hozzaad az autohoz egy megjegyzest, pl.: "Top up screenwash!" ...
  Ha akarja, betaggeli valami szinnel. Pl. pirossal
  Ha akarja csinalhat belole todo-t egy kattintassal es a todo-hoz betaggalhet
  masik userhez vagy siman anonim todo-nak is meghagyhatja

# Webshops
- Több webshop / cég
