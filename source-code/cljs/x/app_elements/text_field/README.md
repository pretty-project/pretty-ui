
# field-content-f
  Az adatbázisban tárolt érték a field-content-f függvény alkalmazásával kerül a mezőbe.

  Pl.: A text-field elem React-fába csatolásakor a value-path útvonal tárolt érték a
       mezőbe íródik és a tárolt értéket a field-content-f függvény alkalmazásával írja
       a mezőbe.

       A text-field elem React-fába csatolásakor az initial-value érték a mezőbe és a
       value-path útvonalra íródik és az initial-value értéket a field-content-f függvény
       alkalmazásával írja a mezőbe.

       A {:required? true} beállítással használt mező kitöltöttségének vizsgálatakor
       a value-path útvonalon tárolt értéket a field-content-f függvény alkalmazása után
       vizsgálja meg.


# set-value-f
  A mezőbe írt szöveg a set-value-f függvény alkalmazásával kerül az adatbázisba.

  Pl.: A mezőbe írt szöveg a set-value-f függvény alkalmazásával kerül a value-path
       útvonalra

       A mező kiürítésekor az üres string érték a set-value-f függvény alkalmazásával
       kerül a value-path útvonalra
