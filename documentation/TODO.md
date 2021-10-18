
- Something for your mind, your body and your soul!

# Mono-template
- Compile-kor kitörli a /public/js/plugins mappát? @Paul
- Mono-template project-details.edn {:authenticated-home "..."}
- Mono-template project-name.css -> site.css és admin.css
- Mono-template-ből hiányzik, hogy a /login route az admin.js -t inditsa
- MONOTEMPLATE-bol kivenni az mt-logo-.png fileokat
- demo user monotemplate be is!


# x4.3.0

- freezed-route-ra nincs szükség

- Elméletileg nem szükséges az authenticated-home! Amilyen route-on vagy amikor bejön
  login-screen oda visz vissza a reboot után.
  Csak a /login route-on volt valami basz!
  SSC rányomsz a logout-ra ami átdob a login screen re ami a /-re akar bejelentkezni
  valami a logout miatt nem tud vissza menni a /admin-ra


- Az elementeknél megérné megvizsgálni, hogy szükséges a containernek átadni a subscriber
  tulajdonságot mert sokszor tök felesleges a stated komponens nem static módban megy

- A re-frame-be is lehetne tenni a :require :as -hez hasonló dolgot.
  Meg lehetne mondani a re-frame-nek, hogy pölö az :x.app-router névteret :router névterként
  is elérje.

- Most már nyugodtan bekerülhet a pathom és a mongodb az x-be

- Fájlkezelőben lecsekkolni, h ne lehessen egy mappát saját magába move-olni vagy (?másolni?)

- a/redirect-sub

- Ha az elemeknél különválasztásra kerül a :color tulajdonság, :color, :background-color, :border-color
  tulajdonságokra akkor megvalosítható az, hogy pl.: a label elemnek :color tulajdonságként string kerül átadásra, akkor az inline-style-ként hozzáadja, hogy {:color "..."}

- A görgetéskor tapasztalhato laggolo kontent kirajzolás probléma valsz teljesítmény probléma ami
  adodhat több dolog összeadodásábol, pl.: scroll-handler a re-frame db-be ir, a kártyákon box-shadow
  van ami a legcinkesebb performance szempontbol, lehet hogy még sok helyen van opacity használva,
  ami performance szempontbol cinkes, és

- file-browser nem irja ki hogy nincs ien mappa amikor hianyzik a root mappa

- A normalize.css -ből kikerül a html { scroll-behavior: smooth} mert azt hittem, hogy az
  csak arra való, hogy a scrollto és scrollby fgv-ek viselkedését meghatározza, ezért
  átkerült az app-fruits.dom névtérbe. De Paul szerint ez befolyásolja azt is, hogy ha
  gyorsan szkrollolok, akkor késve / villanva jelenik meg a kontent ami beér a viewport-ba
  SZÉT KELL TESZTELNI

- Fájlkezelőbe ne cover hanem contain legyen a preview

- Jobb tárolni a dokumentumokban a névteret is?

- Scroll handler és mouse handler és más soxor db-be iro namespace-ek atomot vagy saját Re-Frame db
  atom-ot használjanak, hogy ne spam-eljék a db-t, mert tul sokszor futnak a subscription function-ök

- Az on-scroll és on-touch-move események stopPropagation függvénye nem gátolja meg,
  hogy a modalon szkrollolás szkrollolja a modal mögött kontentet?

- Legyen saját tooltip mert geci lassu a browser-szintű tooltip

- Ha kész az SSC átnézni a monotemplate-et
  Ha le van tisztázva a monotemplate, csinálni egy új projektet a monotech.hu oldalnak
  https://github.com/monotech-hq/monoset/tree/14f2a2893ff66967b5e9061eaa212bfab8982c89
  Ha a monotech.hu oldal új projekben van megirni, hogy ne a 3000-es porton fusson,
  és akkor elindithato egy szeron az xgolakoautok.hu oldallal.
  -> után leállítani a perlina-server-t

- Szerződésbe:
  digitalocean-el dolgozunk és nem mással

- Multifield nem kell hogy mind text-field legyen.
  Csak amit épp szerkeszt a user

- dom/set-scroll-y meghalt

- Combo box field nem olyan széles mint a text-field-ek @Paul

- Nyelvi beállítást és más beállításokat jegyezze meg a szerveren.

- Home könyvtárat betenni a project-name storage-ba

- Collapsible event-log

- Date-field elem gyári "reset!" end-adornment-jét x-esre cserélni
  + Calendar icon end-adornment ami megnyitja a Calendar-t

- project-name-et re-strukturálni, föleg az eql-es pathom-os részt az x.server-media
  névtér tapasztalatai alapján

- FOUT és FOUT material

- Aria és egyéb disabled cuccok

- Fájlkezelő
  - Szerver óra nem helyi időt mutat
  - x.server-media.item-handler használná a project-name.auth.api -t, ami felette áll!
    Ezt fel kell oldani!
  - thumbnail-ek készítése
  - file uploading megszakítás
  - a directory-action-select id ja ismétlődik ha a file-browser-en és a file-storage-ban
    is ki van rednerelve

- Multi field keypress vezérlés

- Pre-render-t fogadni és betenni a DOM-ba.

- Ha a route-ok regisztrálása kizárolag szerver oldalon történik, attól látni
  fogják a különbözö js-ek egymás route-jait?

- Az elementek {:color ...} tulajdonsága legyen felbontva:
  {:color ...}, {:background-color ...}, {:border-color ...}
  És ha string típust kap értékként, akkor ne a {:data-color "..."} attributumot
  állítsa be, hanem inline-style-ként alkalmazza a kapott értéket:
  {:border-color "#f02"} => {:style {:border-color "#f02"}}

- load timeout errort is teszelni kell!

- A radio-button és combo-box elemekben nem egyforma (nem szabványos) az {:options [...]}
  tulajdonságként átadott opciók

- Text-field label
  - https://css-tricks.com/html-inputs-and-labels-a-love-story/
  - Ne legyen a label-ben interaktív elem, csak text!
  - Ne label-lel legyen vezérelve a fókusz, hanem natívan (on-mouse-down sets focus)

- A radio-button is kezelje ugy az option-öket, mint az autocomplete?
  {:get-label-f (function)
   :options (* in vector)}
- x.app-elements/autocomplete-surface billentyűzet-vezérlés,
  {:extendable? ... :on-extend tulajdonságok}

- Elements/broker törlése, element-list [element :element-id {...}] formulával
  működöjön
  Paul: akkor a formnak kell kiválasztania, melyik element kell egy adott típushoz
- Ha egy popup label bar label nem fér, ki -> akkor text-overflow: ellipsis
- Peti: elementekre aria-label
- DRAG-OVERLAY-t berakni!
- Sortable mozgatás picit laggos iOS Chrome-on
- iOS belezoomol a text-field-be és nem zoomol ki amikor kilépsz :D
- Bizonyos esetekben a request-eket nem lehet manual megadott azonosítóval küldeni,
  mert, akkor a gyors egymás utánban küldött request-ek közül nem mindegyik megy ki,
  ugyanis a request-handler kezeli, hogy egy request, akkor mehet ki, ha már visszaért
  az előző válasza és letelt az idle-timeout-ja. Erre megoldás, hogy a request-eket
  manual megadott azonosító nélkül kell küldeni és akkor mindegyik kap egy random
  generált azonosítót. Igy korlátlanul lehet küldözgetni őket. Ilyen pl a search-field
  element on-interval eseménye által küldött request-ek esete is, ahol random generált
  azonosítoval kell küldeni a request-eket. Viszont igy az ilyen request-ekre nem
  tudnak az element-ek rasubscribeolni a {:process-id ...} tulajdonságukkal.
  Ezért ha ez egy valós usecase-nél is problémát okoz majd, akkor fontos lenne
  a request-eknek adni egy olyan tulajdonságot, amivel több egymástól független,
  random azonositoju request egy csoportként azonosítható lenne, és akkor rá lehetne kötni
  az element animációját. Pl lehetne a request-eknek {:request-group-id ...} tulajdonságuk.
  És ez a request-group-id is indítana a request processétől függetlenül egy saját
  process-t és erre is rá lehetne subscribe-olni.
- A template resources/public/logo mappábol ki lehet venni az mt-logo- logókat
  elég ha a monosetben van benne. Ugyis ott van használva
- EQL
  Kliens db / server db szinkronizáció
  Ha valamit modositasz, a kliens db-ben, akkor legyen olyan funkcio, hogy
  remote -> ami a szeron is modositja
- Adatkezelés és ÁSZF
  https://policies.google.com/terms?hl=hu (ez is kell!, ?ezt fogadod el a cookie-val?)
  https://policies.google.com/technologies/cookies?hl=hu
  Localstorage vs süti / gdpr (a sütik elmennek a szerora a request-el)
- A button elemnek legyen hover állapota, hogy egyértelmű legyen UX szempontbol
- Megvizsgálni, hogy szükséges-e minden esetben, hogy a response-handler
  read-string-elje a szerver válaszát. Paul szerint talán a :transit-params megoldja,
  hogy clojure adatszerkezeket lehessen küldeni a kliensre

# x4.4.0
- BUG#0054
- https://www.spar.hu/uzletek/spar-express-szeged-6723-makkoshazi-krt-1-
- A privacy-policy beállításokat állandóan elérhetővé kell tenni a felhasználók
  számára. Ezért kell egy x.app-ui.views/privacy-settings felület, amelyet
  a bejelentkezett (applikácót használó) felhasználók a beállítások felület
  egyik tab-ja ként használhatnak, míg a vendég felhasználók (weboldalt látogató)
  a footer-en elhelyezett hivatkozásról érhetnek el.
- Nem megy a tab-background backdrop-filter iOS-on
- Nem megy a viewport tetejére a szkroll-csík iOS-on relative tabon

# x4.5.0
- https://developers.google.com/web/fundamentals/native-hardware/fullscreen/
  Ebböl a manifest-es részt tedd bele az appba!
  https://web.dev/add-manifest/
- Favicon-ok rendbetétele, home-screen icon méretek használata, apple-touch-icon
  beállítása
  https://css-tricks.com/favicon-quiz/
- aria-label, aria-hidden
- Az og:url meta tag tartalmát ellenőrizni szerver környezetben is, nem csak localhoston
  + az og-preview-t ellenörizni pl. facebookon
- Stepped tab layout: (mint az airbnb-nél) Olyan, mint a dialog-tab,
  bal gomb: Skip
  jobb gomb: Next
  tab label bar / bal oldali gomb: back-button
  tab label bar / jobb oldali gomb(opcionálisan): close-button

# x4.6.0
- A resolution-blockert leváltotta a viewport meta tag átkonfigurálása
  width=device-width beállításról width=320px belállításra.
  Tesztelni kell asztali- és mobileszközökön is!
- init, remove, reg, destruct, clean, add, set, store, save, delete
  elnevezéseket rendezni!  

# x4.7.0
- A pdf generator és más service-ek teleszemetelik a /tmp mappát,
  cron-ba be kell állítani, hogy törlödjön egy bizonyos idő után!
- Access webapp when offline:
  https://en.wikipedia.org/wiki/Cache_manifest_in_HTML5  

# x4.8.0
- Ha az applikáció full-screen modban van vagy headless browser-ben akkor
  a felhasználó nem feltétlenül tud frissíteni, ezért szükséges lehet,
  az app-menu-be egy "Restart app" gomb ami reboot-olja az applikációt.
- A tabok sorrendjenek megvaltozasakor, az x.app-components/image-preloader
  komponenst tartalmazo tab ujrarenderelesevel, hogyan viselkedik
  az image-preloader?
  (Az app-components.stated felismeri ha éppen remount-ol!)
- Ha egy üzenet érkezik, vagy esetleg valamilyen fontos event történt, akkor
  a favicon-on jelenjen meg egy piros pont, és 1000ms-enként váltogassa a
  browser-tab title-t!
- Mobilokon néha hiába tiltod a html-en a scroll amikor elé teszel egy modalt, akkor
  is engedi néha szkrolloni a bodyt! iOS ontouchmove preventDefault
- css :in-range selector

# x5.0.0
- Event-id eltávolítása az event-vector-ból trim interceptor használatával
- A reg-event-fx eseménykezelő a cofx map helyett a db map-ot használja kontextusként!
- Minden regisztrált felhasználó meghivat új felhasználókat, de az általa
  meghivottak csak annyi jogosultságot kapnak, mint a meghivó személy, igy
  lényegében semmilyen kockázatot nem jelent az, hogy bárki behivhat uj embereket
  a céghez ahol dolgozik.
- on-click helyett használj on-mouse-down-t, ha request-et inditasz, mert
  ca. 80 ms-el hamarabb megtörténik
- Ha text-field element {:resetable? true}, akkor az ESC billentyű lenyomására
  reseteljen
- CSS fájlok minify-olása
- A monoset CSS fájlait egy CSS fájlba minify-olni













; Más webapp-oknál (pl.: Twitch, Google Drive, ...) nincs relatív pozíciónálású
; tartalom, ami a html szkrollt használná, ehelyett scroll-container DIV-ekben
; van a tartalom. Ennek következtében mobil böngészőkben nem úgy viselkednek,
; mint egy hagyományos weboldal. Bizonyos böngészőkben (iOS Chrome, ...)
; nem lehet az applikációt a felfele túl-szkrollozás gesztussal frissíteni, mivel
; nem lehet a html elemen szkrollolni. És ehhez hasonlóan a böngészű címsora és
; egyéb vezérlő sávja sem tűnik el lefele szkrollozáskor, mivel a szkroll nem
; a html elemen történik.




; Mentés több nyelven:
; Az inputok saját set event-et kapnak ami kezeli ezt is!
;
; Nyelvek kiválasztása:
; Jobb oldali menüben egy zászló ikon
; Popup window-t nyit meg, benne checkbox-ok, ha egynél több van kiválasztva
; Akkor a flag ikonon egy zöld marker jelenik meg
; Ha nincs kiválasztva min 1, akkor nem enged menteni.









# Ideák

- `@media only screen and (max-width: 50vh) {}`
  Igy tudsz képernyő arány-t vizsgálni

- "Stay-in-viewport" renderelés:
  - Az elem amit szeretnél a viewportba mindig beférő magassággal ellátni,
    legyen egy scroll-effect wrapper-ben.

```
(defn- ak7600
  [scroll-signal]
  [:div {:style {:maxHeight (candy/css "vh" scroll-signal)}}])

(defn- stay-in-viewport-element
  []
  [synth/subscriber ak7600
   [:x.app-environment.dom-handler/get-relative-top ::scroll-signal]])         

(defn- test
  []
  [synth/scroll-effect ::scroll-effect stay-in-viewport-element])

```

- nuclei: mag, atommag, lényeg, középpont

- Not the spoon that bends.

- Mondd, jó barát, és lépj be!

- Mi case es su casa

- This milord is my families axe

- Ha az asana-t tul sokszor a frissíted, a köv. üzenetet kapod:
  We're catching up with you.
  You might be sending too many requests or refreshing too often.
  Please wait a few minutes before connecting to Asana again.

- https://github.com/xxx/Hello-World/settings
  A lap alján: Danger Zone szekció

  + Amikor törölnél egy repót:
    Are you absolutely sure?
    Unexpected bad things will happen if you don’t read this!
    This action cannot be undone. This will permanently delete the XY repository...
    Please type in the name of the repository to confirm.
    És utána még jelszót kér!
