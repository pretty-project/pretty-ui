
# Az item-handler plugin adhatja az alapját a többi pluginnak
- Adat(ok) letöltése (letöltés, újratöltés, backup, local-changes ...)
- Adat(ok) kezelése  (törlés, duplikálás, műveletek visszavonása ...)



# Problémák amire megoldást jelent:
- Az item-browser plugin az item-lister pluginnal közös resolver-t használ ezért a resolver elnevezése
  a my-type-lister kifejezést tartalmazza my-type-browser helyett
- Az item-browser egy elem törlése, duplikálása, stb műveletek szinte teljesen megegyeznek
  az item-editor hasonló műveleteivel, felesleges, hogy duplán létezzenek
- Az item-lister get-items lekérése-e tartalmazza az :item-id kulcsot, amire az item-browsernek lenne szüksége,
  mégis az item-listerbe kellett tenni
  



# xxx
- Az elem duplikálása művelet után megjelenő notification-ön paraméterként átadható kell
  legyen a műveleti gomb felirata és on-click eseménye, mert az item-editorból duplikált elemnél
  a művelet után a másolat szerkesztése gomb jelenik meg a notification-ön az item-browser, pedig
  csak kiirja, hogy elem duplikálva.
