
# Something for your mind, your body and your soul!

- A ghost-ok, horizontal-separator-ok, indent-ek, stb. méret skálájból kimaradt
  a 30px, mert kevés a 7 lépés 6-48px ig

- a re-frame db sokszorosan tartalmazza önmagát!!

- gombok presetjei nem szükségesek

- a tool-ok nem hasznánalk x-et szoval clj-tools cljs-tools vagy mitomén hogy de
  mehetnek repora

- az element-ek style tulajdonsága a body komponensekene legyen

- text-field hack5041-et lecserélni a text-editor plugin synchronizer-ére

- elements.api elemek body komponensein legyenek a body-ra jellemző data-attribute-ok
  és a style

- Használj set-eket! #(1 2 3)
  + (set ...) set függvény!
  - Egy elem egyszer fordulhat elő benne

- Az xxx-ek tartalmazzák a filepath-et is
  XXX#0551 (...)

- (defn f [{:keys [] {:keys [name]} :customer}])
  (f {:customer {:name "Peti" :address "Valahol"}})
  =>
  "Peti"

- Az elementeknél inább element-structure sok helyen, mint element-body a proper elnevezés

- Read this bible: https://developer.mozilla.org/en-US/docs/Web/Performance

- OPTIMISATION:
  Felesleges Re-Frame írások:
  -

- Vannak olyan [:x.ui/render-bubble! ...] események, amik egy metamorphic-content-et
  adnak át {:content ...} tulajdonságként, ez igy nem maradhat, a bubble layout-ja
  ki kell kerüljön az x-ből!

- SEO: https://schema.org/docs/gs.html
       https://stackoverflow.com/questions/29928974/what-is-the-purpose-of-meta-itemprop

- HACK#3031 probléma!

- BUG#4506 vizsgálata, mivel a szöveg emliti az on-type-ended eseményt, ami megszünt
  mivel az on-change esemény most már az on-type-ended-kor történik meg ezért összevonásra
  kerültek

- Ha több popup van megnyitva, akkor az ENTER és ESC billentyűk csak a legfelsőre hassanak!

- Az item-listerekben a kereso mezo jobb lenne ha disabled true allapotban lenne
  amig a tobbi elem is abban van, de akkor elvesziti a fokuszt, ha esetleg fokuszalt
  allapotban volt a request elkuldesekor (pl kereses)

- Ha az item-browser-ben kitörölsz egy elemet, kilépsz a engine-ből és rákattintasz a törlés
  visszaálíltása gombra, akkor nem tudja visszaállítani az elemet, mert a meta-item-ek már
  nincsenek meg (gondolom ez más engine-nél is probléma)

- Amikor keresel (pl. item-listerben), akkor kereséskor az útvonalba is bele kell irni
  a keresőszot pl.: "/app/clients/search=xyz"
  Mindenhol igy csinálják és ha ráfrissitesz akkor megmarad a keresőszo, illetve megosztható
  a találati lista

- Az item-lister reset-downloads! függvény {:data-received? false} állapotba lépteti
  az engine-t ami miatt a header eltünik az elemek letöltése utánig kereséskor is,
  és emiatt a search-field újra-fókuszál és a mező elejére teszi a kurzort.

- Az item-lister {:memory-mode? true} beállítását ki lehet kerülni!
  - Rákattintasz a listaelemre, ami memory-mode? true állapotba lépteti a listát
  - megnyilik az item-viewer, -> menü -> beállítások -> föoldal -> vissza a listába
    és hoppá!

- Miután a UI renderer események refaktorálása megtörtént szükséges lellenőrizni,
  hogy az item-viewer engine undo-delete-item folyamata által megjelenített
  undo-delete-item-failed (failed) értesítése megjelenik-e újra, ha az értesítésen
  lévő "Retry" gombra kattintva a folyamat ismételten failed.
  - Elem törlésének sikertelen visszaállítása
  - Értesítés megjelenik
  - "Retry" gomb megnyomása
  - close-bubble! folyamat elindul
  - Visszajön a szerver-válasz, hogy (megint) nem sikerült a visszaállítás
  - Értesítés (megint) megjelenik
  - close-bubble folyamat annyira hosszú, hogy most fejeződik be és bezárja
    az azóta újra megjelent értesítést

- A cljs egy alig típusos nyelv, engedd el a sok (boolean ...) függvényt! Felesleges.

- Jó lenne az engine-eket leválasztani a mongo-db-ről, hogy univerzálisak legyenek.
  Vagy legalább ne kelljen kollekció/dokument struktura lehessen egy item, bármilyen map,
  ne kelljen dokumentumnak lennie.

- https://www.behance.net/gallery/130899041/Healthcare-management-system-UXUI?tracking_source=search_projects%7Cux
  Item lister new layout, fontos lesz majd, hogy a listázóban a listaelemek sok adatot mutatssanak,
  hogy össze lehessen öket hasonlitani, és legyen egy fejlec az adatok cimeivel (excel-like)

- A css class és data-attribute selector-ok elerési ideje köbö egyforma.
  Egyes elemeken pl. buttonokon rengeteg data-attribute van, amit ugy lehet gyorsitani, hogy
  a css fájlba lennének preset-ek! [:div.x-button {:data-preset :my-preset}]
  Így a border-radius, hover-color, color, stb nem 5-6-7 data-attribute használatával
  volna meghatározva, hanem volna 20-30 preset azt csá.
  Ezt majd akkor amikor több fájlra lesz darabolva az elements/style.css

- Mi lenne ha az X (ui) névterek nem tartalmaznának design-t?
  A popup csak egy fekete layer lenne. A notification csak egy pozicio lenne ...
  És a layouts-ban volna benne a notification-bubble és boxed-popup, ...
  layout-a -> boxed-surface, layout-b -> card-surface, flip-popup, boxed-popup, ...
  Akkor az elements is elhagyhatná az x-et

- Az item editor fülei ne csak a változást jelezzék, ha az egyik mező required-warning
  állapotban van, akkor legyen warning badge a fülön

- Fájlfeltöltéskor a line-diagram fél másodpercenként új id-t kap.
  Egyáltalán kell az elemeknek, hogy dom id-ként megkapják az id-t?
  Soha sehol nem volt még megtargetelve!

- request-inspector időrendben a legfirssebb request legyne mindig felül

- Beállítható temporary-parent, ami a kövi [:x.router/go-xxx! ...] eseménnyel törlődik
  - Így megadhato, hogy a "/models/:model-id/types/:type-id" útvonal vissza gombja
    a "/models/:model-id" útvonalra dobjon
  - A view-selector load-selector eseménye is állítsa be hogy az "/@app-home/extension/:view-id"
    ne az "/@app-home/extension" utvonalra vigyen vissza, hanem az "/@app-home" -ra

- Az item-editor duplikálás -> majd másolat szerkesztése átirányit az uj route-ra de nem
  történik ujratöltés

- Az item-editor elem törlése utáni visszaállítás után már nincsenek ez editor beállításai a db-ben
  ami alapján összerakná a visszaállított elem útvonalát amire irányítania kéne

; - A megfelelő hely:
;   [:engines :item-browser/data-items :my-browser {...}]
;   [:engines :item-browser/meta-items :my-browser {...}]
;   (ez ahhoz is kell, hogy amikor az item-browser megkapja a szervertöl, hogy megváltozott az xy
;   kollekció, akkor végig megy a browserer adatain és megnézi, hogy ki van rá feliratkozva)
;   És amugy is ez a természetes helye
; - Az item-editor pedig kaphat {:value-path [...]} tulajdonságot vagy inkább document-path vagy ...

- az elementek min-width tulajdonságara lehet hogy nincs többé szükség, ha a form-ok
  wrapper-jei határozzák meg az inputok szélességét,

- A szerver kezelje a 404 képernyőt, ne a kliens! Ne kelljen a 404-hez letölteni az appot!
  (Változás: Le lett írva miért van így!)

- Ha a szerver-oldalon müködik a dispatch-later, akkor leválthatja a dispatch-tick-et
  a boot-loader.clj-ben és aztán a dispatch-tick törölhető

- ha egy button keypress tulajdonságának megadsz egy billentyűt pl 13 / enter és rátenyerelsz az
  adott billentyűre, akkor sokszor egymás utánban megtörténik az button on-click eventje,
  ami full cink, pl fájl letöltésnél hatszor tölti le a fájlt és hasonlok

ha a notification@monotech.hu rol mennek ki a woermann os es kesobb mas oldalak levelei akkor valahol vezetni kell hogy az smtp adatok esetleges valtozasakor at kell allitano a projektet is!!!!

- Az item-lister pluginban amikor elemeket keresel, akkor ne legyen érzékeny az ékezetekre

- A mongo-db kezelje a permission-öket, a local-db ne kezelje a permission-öket

- (def a [0 1 2 3 4 5])
  (println (str (apply > a)))

- (ns xyz (:require [x.user.api :as x.user]))
  (println (str ::user/primary)) => :x.user.api/primary

- nem mukodik a goog.net.cookies törlés! (ez nem meg lett javitva már egyszer?)

- Multi-combo-box elemben igy nézzen ki egy placeholder: "green, red, purple"
  Szóval vesszővel lehet elválasztani a tag-eket és a vessző billentyű leütése is viselkedjen
  ugyanugy mint az enter (kiüriti a mezőt és hozzáad egy chip-et)

- A text-field mérete megnő ha elég hosszu a helper alatta

- A timestamp->today? nem veszi figyelmbe az időzonat ezért éjfél és egy között a tegnapit mainak hiszi
  (A user locale alapján tudna korrigálni)

- Text-field nem reagál az ESC billentyűra, amikor emptiable? true (a combo-box reagál)

- Text-field szélessége megváltozik az emptiable? true adornment (és más adornment) ki-bekapcsolásakor

- Input validálás: egy input több validátort is fogadjon több hibaüzenettel és néha több input egymástól
  függ pl. password összehasonlító

- Az infinite-loader komponensben az intersection-observer objektumbol minden ujrarenderelessel
  leterjon egy uj intersection-observe object? És ha már nincs a komponens a react-faban, akkor
  mi lesz az observer-el?

- A select popup-on ha elkezdesz beirni valamit, akkor arra reagaljon

- Pathom handler-ekbe user-auth-ot vizsgálni

- Fájlkezelőben lecsekkolni, h ne lehessen egy mappát saját magába move-olni vagy (?másolni?, ez télleg cenk?)

- A normalize.css -ből kikerült a html { scroll-behavior: smooth} mert azt hittem,
  hogy az csak arra való, hogy a scrollto és scrollby fgv-ek viselkedését meghatározza,
  ezért átkerült az app-fruits.dom névtérbe. De Paul szerint ez befolyásolja azt is, hogy ha
  gyorsan szkrollolok, akkor késve / villanva jelenik meg a kontent ami beér a viewport-ba
  SZÉT KELL TESZTELNI

- Az on-scroll és on-touch-move események stopPropagation függvénye nem gátolja meg,
  hogy a modalon szkrollolás szkrollolja a modal mögött kontentet?

- Multifield nem kell hogy mind text-field legyen.
  Csak amit épp szerkeszt a user

- Combo box field nem olyan széles mint a text-field-ek @Paul

- Date-field elem gyári "reset!" end-adornment-jét x-esre cserélni
  + Calendar icon end-adornment ami megnyitja a Calendar-t

- FOUT és FOUT material

- Aria és egyéb disabled cuccok

- Fájlkezelő
  - Szerver óra nem helyi időt mutat

- Multi field keypress vezérlés

- Pre-render-t fogadni és betenni a DOM-ba.

- load timeout errort is teszelni kell!

- A load-timeout error felülirja a hibaképernyőt!

- Ha egy popup label bar label nem fér, ki -> akkor text-overflow: ellipsis

- DRAG-OVERLAY-t berakni!

- Sortable mozgatás picit laggos iOS Chrome-on

- Adatkezelés és ÁSZF
  https://policies.google.com/terms?hl=hu (ez is kell!, ?ezt fogadod el a cookie-val?)
  https://policies.google.com/technologies/cookies?hl=hu
  Localstorage vs süti / gdpr (a sütik elmennek a szerora a request-el)

- Megvizsgálni, hogy szükséges-e minden esetben, hogy a response-handler
  read-string-elje a szerver válaszát. Paul szerint talán a :transit-params megoldja,
  hogy clojure adatszerkezeket lehessen küldeni a kliensre

- https://www.spar.hu/uzletek/spar-express-szeged-6723-makkoshazi-krt-1-

- Nem megy a viewport tetejére a szkroll-csík iOS-on relative tabon

- https://developers.google.com/web/fundamentals/native-hardware/fullscreen/
  Ebböl a manifest-es részt tedd bele az appba!
  https://web.dev/add-manifest/

- Favicon-ok rendbetétele, home-screen icon méretek használata, apple-touch-icon
  beállítása
  https://css-tricks.com/favicon-quiz/

- aria-label, aria-hidden

- Az og:url meta tag tartalmát ellenőrizni szerver környezetben is, nem csak localhoston
  + az og-preview-t ellenörizni pl. facebookon

- A resolution-blockert leváltotta a viewport meta tag átkonfigurálása
  width=device-width beállításról width=320px belállításra.
  Tesztelni kell asztali- és mobileszközökön is!

- init, remove, reg, destruct, clean, add, set, store, save, delete
  elnevezéseket rendezni!  

- Access webapp when offline:
  https://en.wikipedia.org/wiki/Cache_manifest_in_HTML5  

- Ha az applikáció full-screen modban van vagy headless browser-ben akkor
  a felhasználó nem feltétlenül tud frissíteni, ezért szükséges lehet,
  az app-menu-be egy "Restart app" gomb ami reboot-olja az applikációt.

- Ha egy üzenet érkezik, vagy esetleg valamilyen fontos event történt, akkor
  a favicon-on jelenjen meg egy piros pont, és 1000ms-enként váltogassa a
  browser-tab title-t!

- Mobilokon néha hiába tiltod a html-en a scroll amikor elé teszel egy modalt, akkor
  is engedi néha szkrolloni a bodyt! iOS ontouchmove preventDefault

- css :in-range selector

- Minden regisztrált felhasználó meghivat új felhasználókat, de az általa
  meghivottak csak annyi jogosultságot kapnak, mint a meghivó személy, igy
  lényegében semmilyen kockázatot nem jelent(?) az, hogy bárki behivhat uj embereket
  a céghez ahol dolgozik.

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

- `@media only screen and (max-width: 50vh) {}`
  Igy tudsz képernyő arány-t vizsgálni(?)

- nuclei: mag, atommag, lényeg, középpont

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
