
# xxx
# Az item-handler plugin adhatja az alapját a többi pluginnak
- Adat(ok) letöltése (letöltés, újratöltés, backup, local-changes ...)
- Adat(ok) kezelése  (törlés, duplikálás, műveletek visszavonása ...)



# Problémák amire megoldást jelent
- Az item-browser egy elem törlése, duplikálása, stb műveletek szinte teljesen megegyeznek
  az item-editor hasonló műveleteivel, felesleges, hogy duplán létezzenek
- Az item-lister get-items lekérése-e tartalmazza az :item-id kulcsot, amire az item-browsernek lenne szüksége,
  mégis az item-listerbe kellett tenni



# Új problémák
- A get-item-suggestions validálása sem történhet az item-handler pluginban, mert tul specifikus
- Az item-handler letöltés kezelője nem foglalkozhat az infinite-loader újratöltésével a dokumentumok
  megérkezése után, mert nincs köze hozzá, hogy egy-egy plugin infinite-loader-rel vagy pagination-nal,
  vagy akármivel tölti le az elemeket
- Az item-handler letöltés kezelője nem foglalkozhat a downloaded-suggestions eltárolásával az item-editor
  dokumentum megérkezésekor, mert nincs köze hozzá
- Hogyan tárolja egyszerre az item-browser a letöltött dokumentumokat és az egy darab letöltött dokumentumot?



# xxx
- Az elem duplikálása művelet után megjelenő notification-ön paraméterként átadható kell
  legyen a műveleti gomb felirata és on-click eseménye, mert az item-editorból duplikált elemnél
  a művelet után a másolat szerkesztése gomb jelenik meg a notification-ön az item-browser, pedig
  csak kiirja, hogy elem duplikálva.
