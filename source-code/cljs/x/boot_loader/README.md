
# Az applikáció betöltésének folyamata:

- Az [core/synchronize-app! ...] esemény az applikáció indításához szükséges
  adatokat letölti a szerverről.
- Az applikáció indításához szükséges adatok letöltődése után az :on-app-init
  események megtörténése.
- Az :on-app-init események megtörténésének kezdete után 100 ms idő elteltével
  az :on-app-boot események megtörténése.
- Az :on-app-boot események megtörténésének kezdete után 100 ms idő elteltével
  az applikáció felépítése.
- Azonosított felhasználó esetén az :on-app-login bejelentkezési események
  megtörténése.
- Applikáció tartalmának renderelése.
- Az applikáció tartalmának renderelése után 100 ms idő elteltével
  az {:on-app-launch ...} események megtörténése.
