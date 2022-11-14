
# debug-mode
  Ha a kliens aktualis utvonala tartalmazza valamelyik specialis kifejezest, ami {:debug-mode? true}
  allapotba lepteti az applikaciot, akkor a kliensen elerhetove valnak a fejlesztoi eszkozok,
  es a szerver-oldali nevterek is megallapithatjak a request tartalmabol, hogy egy {:debug-mode? true}
  allapotban levo kliensrol erkezett a lekeres.

  A kliens {:debug-mode? true} allapota nem ugyanaz, mint a szerver {:dev-mode? true} allapota, amit
  a szerver inditasakor atadott parameterezessel lehet beallitani es a szerver-oldali fejlesztoi eszkozok
  hasznalatahoz szukseges!
