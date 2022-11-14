
# Something for your mind, your body and your soul!



# Mono-template
- Compile-kor kitörli a /public/js/plugins mappát? @Paul
- Mono-template project-name.css -> site.css és app.css
- Mono-template-ből hiányzik, hogy a /login route az app.js -t inditsa
- MONOTEMPLATE-bol kivenni az mt-logo-.png fileokat
- demo user tenni a monotemplate be is!



# x4.7.0

- az element-ek style tulajdonsága a body komponensekene legyen

- text-field hack5041-et lecserélni a text-editor plugin synchronizer-ére

- elements.api elemek body komponensein legyenek a body-ra jellemző data-attribute-ok
  és a style

- az x ne használja az elements! legyen tőle független

- Használj set-eket! #(1 2 3)
  + (set ...) set függvény!
  - Egy elem egyszer fordulhat elő benne

- A reg-lifecycles! is ugy legyen tabolva mint a reg-event-fx
  (core/reg-lifecycles ::lifecycles
    {...})

- Az xxx-ek tartalmazzák a névtereket is
  XXX#0551 (mid-fruits.map)

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

- Rendet tenni a monoset log fájlokban

- HACK#3031 probléma!

- BUG#4506 vizsgálata, mivel a szöveg emliti az on-type-ended eseményt, ami megszünt
  mivel az on-chane esemény most már az on-type-ended-kor történik meg ezért összevonásra
  kerültek

- Ha több popup van megnyitva, akkor az ENTER és ESC billentyűk csak a legfelsőre hassanak!

- LICENSE fájlt átolvasni!! Milyen licenc van a monoset-ben????

- Az item-listerekben a kereso mezo jobb lenne ha disabled true allapotban lenne
  amig a tobbi elem is abban van, de akkor elvesziti a fokuszt, ha esetleg fokuszalt
  allapotban volt a request elkuldesekor (pl kereses)

- Ha az item-browser-ben kitörölsz egy elemet, kilépsz a pluginból és rákattintasz a törlés
  visszaálíltása gombra, akkor nem tudja visszaállítani az elemet, mert a meta-item-ek már
  nincsenek meg (gondolom ez más pluginnál is probléma)

- Amikor keresel pl item-listerben, akkor kereséskor az útvonalba is bele kell irni
  a keresőszot pl.: "/app/clients/search=xyz"
  Mindenhol igy csinálják és ha ráfrissitesz akkor megmarad a keresőszo

- Az item-lister reset-downloads! függvény {:data-received? false} állapotba lépteti
  a plugin ami miatt a header eltünik az elemek letöltése utánig kereséskor is,
  és emiatt a search-field újra-fókuszál és a mező elejére teszi a kurzort.

- Az item-lister {:memory-mode? true} beállítását ki lehet kerülni!
  - Rákattintasz a listaelemre, ami memory-mode? true állapotba lépteti a listát
  - megnyilik az item-viewer, -> menü -> beállítások -> föoldal -> vissza a listába
    és hoppá!

- Miután a UI renderer események refaktorálása megtörtént szükséges lellenőrizni,
  hogy az item-viewer plugin undo-delete-item folyamata által megjelenített
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

- Jó lenne a plugin-okat leválasztani a mongo-db-ről, hogy univerzálisak legyenek.
  Vagy legalább ne kelljen kollekció/dokument struktura lehessen egy item, bármilyen map, ne kelljen
  dokumentumnak lennie.

- https://www.behance.net/gallery/130899041/Healthcare-management-system-UXUI?tracking_source=search_projects%7Cux
  Item lister new layout, fontos lesz majd, hogy a listázóban a listaelemek sok adatot mutatssanak,
  hogy össze lehessen öket hasonlitani, és legyen egy fejlec az adatok cimeivel (excel-like)

- Az item-lsiter/list-item komponensből valahogy ki kellene választani a designt és visszatenni
  a layouts-ba! De így most gyorsabb, ha pl. a [:item-lister/item-disabled? ...]
  [:item-lister/item-selected? ...] feliratkozások kimetét nem a paraméterként kapják a listaelemek
  Ha sikerült különválasztani, akkor a file-uploaderben sem lesz szükség rá, hogy az item-browser/list-item
  komponens legyen a listaelem, mivel semmi köze az item-browser-hez a fájlfeltöltének!

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

- Az item editor fülei ne csak a változást jelezzék, ha az egyik mező required-warning állapotban van,
  akkor legyen warning badge a fülön

- Fájlfeltöltéskor a line-diagram fél másodpercenként új id-t kap.
  Egyáltalán kell az elemeknek, hogy dom id-ként megkapják az id-t?
  Soha sehol nem volt még megtargetelve!

- request-inspector időrendben a legfirssebb request legyne mindig felül

- dom/form-data van egy kérdéses sor ...
  dom/file-selector detto

- Beállítható temporary-parent, ami a kövi [:x.router/go-xxx! ...] eseménnyel törlődik
  - Így megadhato, hogy a "/models/:model-id/types/:type-id" útvonal vissza gombja
    a "/models/:model-id" útvonalra dobjon
  - A view-selector load-selector eseménye is állítsa be hogy az "/@app-home/extension/:view-id"
    ne az "/@app-home/extension" utvonalra vigyen vissza, hanem az "/@app-home" -ra

- Milyen hatással van a view-selector plugin-ra, hogy hamarabb feliratkozhatsz a selected-view-id
  értékére mint, ahogy a body komponens eltárolja a default-view-id értékét

- A mid névterekbe nem is kell .api fájl csak a mid-core.api van meghivva itt-ott, amit
  levált majd a re-frame.api, utána törölhetők a mid-* .api fájlok
  DE KELL az x-be biztosan kell, a mid térben is egymást használhatják a modulok nem mindig csak
  pure helper fuggvények vannak

- a -handler a modulok nevében már tul hosszu sokszor :(

- sok az :as a :refer [r] require, ahol nem kell az (a/... ...)

- sok a :refer [param return] require ahol nem kell a (param ...)  

- Az item-editor duplikálás -> majd másolat szerkesztése átirányit az uj route-ra de nem
  történik ujratöltés

- Az item-editor elem törlése utáni visszaállítás után már nincsenek ez editor beállításai a db-ben
  ami alapján összerakná a visszaállított elem útvonalát amire irányítania kéne

; - A megfelelő hely:
;   [:plugins :item-browser/data-items :my-browser {...}]
;   [:plugins :item-browser/meta-items :my-browser {...}]
;   (ez ahhoz is kell, hogy amikor az item-browser megkapja a szervertöl, hogy megváltozott az xy
;   kollekció, akkor végig megy a browserer adatain és megnézi, hogy ki van rá feliratkozva)
;   És amugy is ez a természetes helye
; - Az item-editor pedig kaphat {:value-path [...]} tulajdonságot vagy inkább document-path vagy ...



- az elementek min-width tulajdonságara lehet hogy nincs többé szükség, ha a form-ok
  wrapper-jei határozzák meg az inputok szélességét,

- pluginok error-mode-ját teszteni kell

- A szerver kezelje a 404 képernyőt, ne a kliens! Ne kelljen a 404-hez letölteni az appot

- Ha a szerver-oldalon müködik a dispatch-later, akkor leválthatja a dispatch-tick-et
  a boot-loader.clj-ben és aztán a dispatch-tick törölhető

- A ui-renderer az elemek megjelenítésekor több különálló alkalommal ír a re-frame db-be, ami nem jo
  mert sokszor ujrakalkulalodnak a subscription-ök, ha react-transition-el lenne megoldva az animálás,
  akkor nem kellene ennyire felbontani a render-eseményeket

- ha egy button keypress tulajdonságának megadsz egy billentyűt pl 13 / enter és rátenyerelsz az
  adott billentyűre, akkor sokszor egymás utánban megtörténik az button on-click eventje,
  ami full cink, pl fájl letöltésnél hatszor tölti le a fájlt és hasonlok

- x.ui modulban az environment/reveal-element-animated és environment/hide-element-animated
  eljárásokat react-transition vezérlésre cserélni

- Media picker SHIFT billentyűvel átrakja magát az item-lsiter select-mode-ba!

- A view-selector plugin most már nem csak útvonalakkal használható, leváltja a gestures/view-selectort?

ha a notification@monotech.hu rol mennek ki a woermann os es kesobb mas oldalak levelei akkor valahol vezetni kell hogy az smtp adatok esetleges valtozasakor at kell allitano a projektet is!!!!

- Az item-lister pluginban amikor elemeket keresel, akkor ne legyen érzékeny az ékezetekre

- project-details.edn fájl ne tartalmazzon kommenteket, az legyen egy külön fájlban és akkor
  lehet függvénnyel írni a fájlba az app-build értékét automtikusan (vagy legyen máshova írva az app-build?)

- A mongo-db kezelje a permission-öket, a local-db ne kezelje a permission-öket

- {:content :content-props :subscriber} formula kivezetése ...

- (def a [0 1 2 3 4 5])
  (println (str (apply > a)))

- (ns xyz (:require [x.user.api :as x.user]))
  (println (str ::user/primary)) => :x.user.api/primary

- nem mukodik a goog.net.cookies törlés! ez nem meg lett javitva?

- Multi-combobox elembe igy nézzen ki egy placeholder: "green, red, purple"
  Szóval vesszővel lehet elválasztani a tag-eket és a vessző billentyű leütése is viselkedjen
  ugyanugy mint az enter (kiüriti a mezőt és hozzáad egy chip-et)

- A text-field mérete megnő ha elég hosszu a helper alatta

- A timestamp->today? nem veszi figyelmbe az időzonat ezért éjfél és egy között a tegnapit mainak hiszi

- request->added-prop, modified-prop, ... törölhető

- Text-field nem reagál az ESC billentyűra, amikor emptiable? true (a combo-box reagál)

- Text-field szélessége megváltozik az emptiable? true adornment (és más adornment) ki-bekapcsolásakor

- Input validálás egy input több validátort is fogadjon több hibaüzenettel és néha több input egymástól
  függ pl. password összehasonlító

- Az infinite-loader komponensben az intersection-observer objektumbol minden ujrarenderelessel
  leterjon egy uj intersection-observe object? És ha már nincs a komponens a react-faban, akkor
  mi lesz az observer-el?

- A select popup-on ha elkezdesz beirni valamit, akkor arra reagaljon

- Pathom handler-ekbe user-auth-ot vizsgálni

- Fájlkezelőben lecsekkolni, h ne lehessen egy mappát saját magába move-olni vagy (?másolni?, ez télleg cenk?)

- Ha az elemeknél különválasztásra kerül a :color tulajdonság, :color, :background-color, :border-color
  tulajdonságokra akkor megvalosítható az, hogy pl.: a label elemnek :color tulajdonságként string kerül átadásra, akkor az inline-style-ként hozzáadja, hogy {:color "..."}

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

- A load-timeout error felülirja a hibaképernyőt!

- Text-field label
  - https://css-tricks.com/html-inputs-and-labels-a-love-story/
  - Ne legyen a label-ben interaktív elem, csak text!
  - Ne label-lel legyen vezérelve a fókusz, hanem natívan (on-mouse-down sets focus)

- elements.api/autocomplete-surface billentyűzet-vezérlés,
  {:extendable? ... :on-extend tulajdonságok}

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

# x4.6.0
- BUG#0054
- https://www.spar.hu/uzletek/spar-express-szeged-6723-makkoshazi-krt-1-
- A privacy-policy beállításokat állandóan elérhetővé kell tenni a felhasználók
  számára. Ezért kell egy x.ui.views/privacy-settings felület, amelyet
  a bejelentkezett (applikácót használó) felhasználók a beállítások felület
  egyik tab-ja ként használhatnak, míg a vendég felhasználók (weboldalt látogató)
  a footer-en elhelyezett hivatkozásról érhetnek el.
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
- Access webapp when offline:
  https://en.wikipedia.org/wiki/Cache_manifest_in_HTML5  

# x5.0.0
- Ha az applikáció full-screen modban van vagy headless browser-ben akkor
  a felhasználó nem feltétlenül tud frissíteni, ezért szükséges lehet,
  az app-menu-be egy "Restart app" gomb ami reboot-olja az applikációt.
- Ha egy üzenet érkezik, vagy esetleg valamilyen fontos event történt, akkor
  a favicon-on jelenjen meg egy piros pont, és 1000ms-enként váltogassa a
  browser-tab title-t!
- Mobilokon néha hiába tiltod a html-en a scroll amikor elé teszel egy modalt, akkor
  is engedi néha szkrolloni a bodyt! iOS ontouchmove preventDefault
- css :in-range selector

# x5.0.1
- Minden regisztrált felhasználó meghivat új felhasználókat, de az általa
  meghivottak csak annyi jogosultságot kapnak, mint a meghivó személy, igy
  lényegében semmilyen kockázatot nem jelent(?) az, hogy bárki behivhat uj embereket
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
   [:x.environment.dom-handler/get-relative-top ::scroll-signal]])         

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
