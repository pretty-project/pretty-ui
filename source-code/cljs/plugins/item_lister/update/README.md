
# XXX#0439
- Az elemeken végzett műveletek sikertelen befejeződésekor csak akkor fejezi be a progress-bar
  elemen 15%-ig szimulált folyamatot, ha a body komponens a React-fába van csatolva.
  Így elkerülhető, hogy ha a folyamat befejeződése előtt a felhasználó esetlegesen elhagyta a plugint
  és egy a plugintól független művelet szimulál a progress-bar elemen folyamatot, akkor ezt a független
  folyamatot a sikertelen befejeződéskor megtörténő esemény "véletlenül" befejezze.



# XXX#7002
- A "Kijelölt elemek duplikálása" művelet nem visszavonható, ...
  ... mert a törlési műveletekkel ellentétben, a művelet végét jelző értesítésen megjelenő
      "Visszavonás" lehetőségtől függetlenül is lehetséges visszaállítani az elemek eredeti állapotát.
      (a duplikált elemek törlésével)
  ... az item-browser plugin használatakor problémát okozna, ha a felhasználó a duplikált
      elemet (vagy annak alelemét) nyitná meg böngészésre, majd a még látható értesítésen
      a "Visszavonás" lehetőséget választva törölné a duplikált (aktuálisan böngészett) elemet.
