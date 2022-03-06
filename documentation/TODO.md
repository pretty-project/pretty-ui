
# Something for your mind, your body and your soul!



# Mono-template
- Compile-kor kitörli a /public/js/plugins mappát? @Paul
- Mono-template project-name.css -> site.css és admin.css
- Mono-template-ből hiányzik, hogy a /login route az admin.js -t inditsa
- MONOTEMPLATE-bol kivenni az mt-logo-.png fileokat
- demo user tenni a monotemplate be is!



# x4.6.0
- Az item-editor duplikálás -> majd másolat szerkesztése átirányit az uj route-ra de nem
  történik ujratöltés

- Az item-editor elem törlése utáni visszaállítás után már nincsenek ez editor beállításai a db-ben
  ami alapján összerakná a visszaállított elem útvonalát amire irányítania kéne


- Lehessen route-parent-et és header-title egy eseményben állítani,  a kevesebb írás miatt

- A hosszu require-ök ne kerüljenek a lista végére

; - A megfelelő hely:
;   [:plugins :item-browser/data-items :my-browser {...}]
;   [:plugins :item-browser/meta-items :my-browser {...}]
;   (ez ahhoz is kell, hogy amikor az item-browser megkapja a szervertöl, hogy megváltozott az xy
;   kollekció, akkor végig megy a browserer adatain és megnézi, hogy ki van rá feliratkozva)
;   És amugy is ez a természetes helye
; - Az item-editor pedig kaphat {:value-path [...]} tulajdonságot vagy inkább document-path vagy ...



- az elementek min-width tulajdonságara lehet hogy nincs többé szükség, ha a form-ok
  wrapper-jei határozzák meg az inputok szélességét,

- és minden element olyan magas amielny magas, és van indent-left, indent-right, indent-top, indent-bottom
  mindenhez

- a pluginok :item-actions tulajdonsága hol a header-ön van hol a body-n egységesíteni kell

- pluginok error-mode-ját teszteni kell

- A szerver kezelje a 404 képernyőt, ne a kliens! Ne kelljen a 404-hez letölteni az appot

- A ; @return (component) és (hiccup) nem szükséges a view komponensek leírásába!

- Ha a szerver-oldalon müködik a dispatch-later, akkor leválthatja a dispatch-tick-et
  a boot-loader.clj-ben és aztán a dispatch-tick törölhető

- Az x modulok névterei is legyenek mappákra bontva amiben van subs events ?? really ?? ...
- Az x modulok is tárolják ugy az adatot, mint az extension [:core :load-handler/meta-items ...]

- A ui-renderer az elemek megjelenítésekor több különálló alkalommal ír a re-frame db-be, ami nem jo
  mert sokszor ujrakalkulalodnak a subscription-ök, ha react-transition-el lenne megoldva az animálás,
  akkor nem kellene ennyire felbontani a render-eseményeket

- ha egy button keypress tulajdonságának megadsz egy billentyűt pl 13 / enter és rátenyerelsz az
  adott billentyűre, akkor sokszor egymás utánban megtörténik az button on-click eventje,
  ami full cink, pl fájl letöltésnél hatszor tölti le a fájlt és hasonlok

- x.app-ui modulban az environment/reveal-element-animated és environment/hide-element-animated
  eljárásokat react-transition vezérlésre cserélni

- Media picker SHIFT billentyűvel átrakja magát az item-lsiter select-mode-ba!

- A send-query! {:debug? true} beállítással tegye be a :debug resolver-t a query-vektorba

- db/apply!-t átnevezni db/apply-item!-re

- A view-selector plugin most már nem csak útvonalakkal használható, leváltja a gestures/view-selectort?

ha a notification@monotech.hu rol mennek ki a woermann os es kesobb mas oldalak levelei akkor valahol vezetni kell hogy az smtp adatok esetleges valtozasakor at kell allitano a projektet is!!!!

- Legyen a monoset-environment mappában vagy inkább a project-emulator mappában egy
  project-dictionary.cljc vagy valami fájl, hogy a projekben ne eventekkel kelljen hozzáadogatni
  a dictionary-termeket

- Az item-lister pluginban amikor elemeket keresel, akkor ne legyen érzékeny az ékezetekre

- project-details.edn fájl ne tartalmazzon kommenteket, az legyen egy külön fájlban és akkor
  lehet függvénnyel írni a fájlba az app-build értékét automtikusan (vagy legyen máshova írva az app-build?)

- user-ek jelszavait hash-elve tárolni

- user-ek mongo-db-be
  az isntaller csináljon egy demo(pw: mono) és egy developer(pw: mono) user-t
  a developer usernek legyen egy "developer" role-ja. A developer role inditsa el a developer-modot
  legyen eltárolva a developer user-nek, a mongo-ban, hogy print-events? ...

- A mongo-db kezelje a permission-öket, a local-db ne kezelje a permission-öket

- {:content :content-props :subscriber} formula kivezetése ...

- mid-fruits.string/max-lines függvényt megírni, hogy a log ne töltse meg a tárhelyet!

- pathom/reg-prototype használatával az extension-ök handler függvényeit egységesíteni lehetne
  item-editor/handlers és item-lister/handlers függvényekként

- Loading animations: DEPRECATED

- (def a [0 1 2 3 4 5])
  (println (str (apply > a)))

- (ns xyz (:require [x.app-user.api :as user]))
  (println (str ::user/primary)) => :x.app-user.api/primary

- nem mukodik a goog.net.cookies törlés! ez nem meg lett javitva?

- Multi-combobox elembe igy nézzen ki egy placeholder: "green, red, purple"
  Szóval vesszővel lehet elválasztani a tag-eket és a vessző billentyű leütése is viselkedjen
  ugyanugy mint az enter (kiüriti a mezőt és hozzáad egy chip-et)

- A text-field mérete megnő ha elég hosszu a helper alatta

- A timestamp->today? nem veszi figyelmbe az időzonat ezért éjfél és egy között a tegnapit mainak hiszi

- request->added-prop, modified-prop, ... törölhető

- icon-button külön element lehet ujra, már csak a bubble-ok fogadnak button-props térképeket, amihez
  szükséges a közös elem.

- db/id-handler mehet mid-fruits/eql-be

- A go-back-surface-label-bar és go-back-popup-label-bar nem jo, hogy browser-back-et használ!

- Text-field nem reagál az ESC billentyűra, amikor emptiable? true (a combo-box reagál)

- Text-field szélessége megváltozik az emptiable? true adornment (és más adornment) ki-bekapcsolásakor

- Input validálás egy input több validátort is fogadjon több hibaüzenettel és néha több input egymástól
  függ pl. password összehasonlító

- Az infinite-loader komponensben az intersection-observer objektumbol minden ujrarenderelessel
  leterjon egy uj intersection-observe object? És ha már nincs a komponens a react-faban, akkor
  mi lesz az observer-el?

- A select popup-on ha elkezdesz beirni valamit, akkor arra reagaljon

- Pathom handler-ekbe user-auth-ot vizsgálni

- Legyen e layout fit a default mindenhol?

- Fájlkezelőben lecsekkolni, h ne lehessen egy mappát saját magába move-olni vagy (?másolni?, ez télleg cenk?)

- Ha az elemeknél különválasztásra kerül a :color tulajdonság, :color, :background-color, :border-color
  tulajdonságokra akkor megvalosítható az, hogy pl.: a label elemnek :color tulajdonságként string kerül átadásra, akkor az inline-style-ként hozzáadja, hogy {:color "..."}

- A görgetéskor tapasztalhato laggolo kontent kirajzolás probléma valsz teljesítmény probléma ami
  adodhat több dolog összeadodásábol, pl.: scroll-handler a re-frame db-be ir, a kártyákon box-shadow
  van ami a legcinkesebb performance szempontbol, lehet hogy még sok helyen van opacity használva,
  ami performance szempontbol cinkes, és

- A normalize.css -ből kikerül a html { scroll-behavior: smooth} mert azt hittem, hogy az
  csak arra való, hogy a scrollto és scrollby fgv-ek viselkedését meghatározza, ezért
  átkerült az app-fruits.dom névtérbe. De Paul szerint ez befolyásolja azt is, hogy ha
  gyorsan szkrollolok, akkor késve / villanva jelenik meg a kontent ami beér a viewport-ba
  SZÉT KELL TESZTELNI

- Az on-scroll és on-touch-move események stopPropagation függvénye nem gátolja meg,
  hogy a modalon szkrollolás szkrollolja a modal mögött kontentet?

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

- Date-field elem gyári "reset!" end-adornment-jét x-esre cserélni
  + Calendar icon end-adornment ami megnyitja a Calendar-t

- FOUT és FOUT material

- Aria és egyéb disabled cuccok

- Fájlkezelő
  - Szerver óra nem helyi időt mutat

- Multi field keypress vezérlés

- Pre-render-t fogadni és betenni a DOM-ba.

- Az elementek {:color ...} tulajdonsága legyen felbontva:
  {:color ...}, {:background-color ...}, {:border-color ...}
  És ha string típust kap értékként, akkor ne a {:data-color "..."} attributumot
  állítsa be, hanem inline-style-ként alkalmazza a kapott értéket:
  {:border-color "#f02"} => {:style {:border-color "#f02"}}

- load timeout errort is teszelni kell!

- Text-field label
  - https://css-tricks.com/html-inputs-and-labels-a-love-story/
  - Ne legyen a label-ben interaktív elem, csak text!
  - Ne label-lel legyen vezérelve a fókusz, hanem natívan (on-mouse-down sets focus)

- x.app-elements/autocomplete-surface billentyűzet-vezérlés,
  {:extendable? ... :on-extend tulajdonságok}

- Ha egy popup label bar label nem fér, ki -> akkor text-overflow: ellipsis

- DRAG-OVERLAY-t berakni!

- Sortable mozgatás picit laggos iOS Chrome-on

- A template resources/public/logo mappábol ki lehet venni az mt-logo- logókat
  elég ha a monosetben van benne. Ugyis ott van használva

- Adatkezelés és ÁSZF
  https://policies.google.com/terms?hl=hu (ez is kell!, ?ezt fogadod el a cookie-val?)
  https://policies.google.com/technologies/cookies?hl=hu
  Localstorage vs süti / gdpr (a sütik elmennek a szerora a request-el)

- A button elemnek legyen hover állapota, hogy egyértelmű legyen UX szempontbol

- Megvizsgálni, hogy szükséges-e minden esetben, hogy a response-handler
  read-string-elje a szerver válaszát. Paul szerint talán a :transit-params megoldja,
  hogy clojure adatszerkezeket lehessen küldeni a kliensre

# x4.6.0
- BUG#0054
- https://www.spar.hu/uzletek/spar-express-szeged-6723-makkoshazi-krt-1-
- A privacy-policy beállításokat állandóan elérhetővé kell tenni a felhasználók
  számára. Ezért kell egy x.app-ui.views/privacy-settings felület, amelyet
  a bejelentkezett (applikácót használó) felhasználók a beállítások felület
  egyik tab-ja ként használhatnak, míg a vendég felhasználók (weboldalt látogató)
  a footer-en elhelyezett hivatkozásról érhetnek el.
- Nem megy a tab-background backdrop-filter iOS-on
- Nem megy a viewport tetejére a szkroll-csík iOS-on relative tabon

# x4.7.0
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

# x4.8.0
- A resolution-blockert leváltotta a viewport meta tag átkonfigurálása
  width=device-width beállításról width=320px belállításra.
  Tesztelni kell asztali- és mobileszközökön is!
- init, remove, reg, destruct, clean, add, set, store, save, delete
  elnevezéseket rendezni!  

# x4.9.0
- A pdf generator és más service-ek teleszemetelik a /tmp mappát,
  cron-ba be kell állítani, hogy törlödjön egy bizonyos idő után!
- Access webapp when offline:
  https://en.wikipedia.org/wiki/Cache_manifest_in_HTML5  

# x5.0.0
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

# x5.0.1
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
; Az inputok saját set event-et kapnak ami kezeli ezt is!
;
; Nyelvek kiválasztása:
; Jobb oldali menüben egy zászló ikon
; Popup window-t nyit meg, benne checkbox-ok, ha egynél több van kiválasztva
; Akkor a flag ikonon egy zöld marker jelenik meg
; Ha nincs kiválasztva min 1, akkor nem enged menteni.









# Ideák

- `@media only screen and (max-width: 50vh) {}`
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