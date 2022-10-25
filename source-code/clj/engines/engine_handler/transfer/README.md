
# XXX#1467
# Milyen adatokat küld az engine transfer modulja?
  Az egyes engine-ek transfer modulja felel azért, hogy az engineok szerver-oldali
  inicializáló eseményének (pl. [:item-editor/init-editor! ...]) átadott
  tulajdonságokhoz az engineok kliens-oldali kezelői is hozzáférjenek.

  Minden engine a szerver-oldali inicializálásakor regisztrál egy transzfer-függvényt,
  ami az applikáció kliens-oldali első lekérésékor letölti az adott engine
  tulajdonságait.
