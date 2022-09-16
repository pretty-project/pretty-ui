
# x4.7.4
- Új plugin: plugins.config-editor.api (edn fájlok szerkesztéséhez)
- Új plugin: plugins.text-editor.api


# x4.7.3
- A [:pathom/send-query! ...] eseménnyel küldött lekérésekre adott szerver-válaszban,
  ha a válasz egy térkép, akkor elhelyezhető benne a :pathom/target-path
  kulcs, ami az adott elem kliens-oldali érkeztetését biztosítja!



# x4.6.7

- Ezentúl szerver-oldalon is leheséges az [:environment/add-css! ...] eseménnyel,
  css fájlokat hozzáadni a rendszerhez!



# x4.6.2

- Új x.%-db.api függvény és esemény: db/toggle-item! & [:db/toggle-item! ...]

- Az x.app-config.edn fájlban felsorolt css, favicon és js fájlok kiszolgálása ezentúl a {:core-js "..."}
  tulajdonság használatával egy meghatározott core-js (pl. "app.js", "site.js" ...) fájlhoz köthetők

- Az x.app-config.edn fájlban felsorolt css, favicon és js fájlok {:cache-control? ...} beállítása
  ezentúl automatikus.
  A saját szerverről kiszolgált fájlok url-je mostantól mindig tartalmazza az app-build értékét
  query paraméterként, így az app-build értékének megváltozásakor ezek a fájlok a felhasználók eszközein
  automatikusan re-cache-elődnek.
