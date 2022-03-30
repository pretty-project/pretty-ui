
# XXX#0439
- Az elemeken végzett műveletek sikertelen befejeződésekor csak akkor fejezi be a progress-bar
  elemen 15%-ig szimulált folyamatot, ha a body komponens a React-fába van csatolva.
  Így elkerülhető, hogy ha a felhasználó esetlegesen elhagyta a plugint, akkor egy a plugintól
  független folyamat által szimulált állapotot jelezzen befejezettnek.
