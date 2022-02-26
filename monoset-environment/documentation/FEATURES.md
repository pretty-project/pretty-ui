
# x4.6.2

- Új x.%-db.api függvény és esemény: db/toggle-item! & [:db/toggle-item! ...]

- Az x.app-config.edn fájlban felsorolt css, favicon és js fájlok kiszolgálása ezentúl a {:core-js "..."}
  tulajdonság használatával egy meghatározott core-js (pl. "app.js", "site.js" ...) fájlhoz köthetők

- Az x.app-config.edn fájlban felsorolt css, favicon és js fájlok {:cache-control? ...} beállítása
  ezentúl automatikus.
  A saját szerverről kiszolgált fájlok url-je mostantól mindig tartalmazza az app-build értékét
  query paraméterként, így az app-build értékének megváltozásakor ezek a fájlok a felhasználók eszközein
  automatikusan re-cache-elődnek.
