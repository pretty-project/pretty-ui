
# BUG#0310
- Ha egy elem szerkesztése közben az elemet duplikálod, majd egy olyan szöveges mezőre kattintasz,
  aminek kötelező a kitöltése és ezután a sikeres duplikálás utáni felugró értesítésen,
  a "Másolat szerkesztése" gombra kattintasz, akkor a kattintás miatt a kötelezően kitöltendő mező,
  amin a fókusz volt eddig, most elveszíti a fókuszt és {:visited? true} állapotba lép, miközben
  az item-editor újraindulása miatt az aktuálisan szerkesztett elem adatai törlődnek a Re-Frame
  adatbázisból és ezek miatt a mező egy pillanatra {:input-required-warning? true} állapotba lép.



# XXX#3907
- ...



# XXX#3905
- Az item-editor plugint is lehetséges popup elemen megjelenítve használni.
  A szerver-oldali {:routed? false} beállítás használatával, az item-editor plugin
  útvonala nem adódik hozzá (a popup elemen való megjelenítéshez szükségtelen az útvonalat beállítani).
  Az ilyen típusú használatnak a kliens-oldali kezelése nincs kidolgozva.
