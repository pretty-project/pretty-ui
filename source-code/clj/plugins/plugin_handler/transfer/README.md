
# Milyen adatokat küld a plugin transfer modulja?
  XXX#1467
  Az egyes pluginok transfer modulja felel azért, hogy a pluginok szerver-oldali
  inicializáló eseményének (pl. [:item-editor/init-editor! ...]) átadott
  tulajdonságokhoz a pluginok kliens-oldali kezelői is hozzáférjenek.
  A szerver-oldali inicializáláskor a plugin regisztrál egy transfer-függvényt,
  ami az applikáció kliens-oldali első lekérésékor letölti az adott plugin
  tulajdonságait.
