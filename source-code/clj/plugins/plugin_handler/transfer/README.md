
# XXX#1467
# Milyen adatokat küld a plugin transfer modulja?
  Az egyes pluginok transfer modulja felel azért, hogy a pluginok szerver-oldali
  inicializáló eseményének (pl. [:item-editor/init-editor! ...]) átadott
  tulajdonságokhoz a pluginok kliens-oldali kezelői is hozzáférjenek.

  Minden plugin a szerver-oldali inicializálásakor regisztrál egy transzfer-függvényt,
  ami az applikáció kliens-oldali első lekérésékor letölti az adott plugin
  tulajdonságait.
