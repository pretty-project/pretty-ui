
# BUG#0310
- Ha egy elem szerkesztése közben az elemet duplikálod, majd egy olyan szöveges mezőre kattintasz,
  aminek kötelező a kitöltése és ezután a sikeres duplikálás utáni felugró értesítésen,
  a "Másolat szerkesztése" gombra kattintasz, akkor a kattintás miatt a kötelezően kitöltendő mező,
  amin a fókusz volt eddig, most elveszíti a fókuszt és {:visited? true} állapotba lép, miközben
  az item-editor újraindulása miatt az aktuálisan szerkesztett elem adatai törlődnek a Re-Frame
  adatbázisból és ezek miatt a mező egy pillanatra {:input-required-warning? true} állapotba lép.



# XXX#3907
- ...


# x4.5.7
- Az item-lister és item-browser plugin mintájára ...
  - A view komponensek önálló feliratkozással rendelkezzenek
  - A subscription függvények nem kellenek az api-ba
