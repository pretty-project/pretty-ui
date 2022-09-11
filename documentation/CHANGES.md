
# 4.7.4
- A [:sync/send-request! ...] esemény {:target-path ...} tulajdonsága megszűnt!
  Használj helyette {:response-f #(...)} függvényt!



# 4.7.3
- Az x.app-elements.api/text-field elem és más text-field elemre épülő elemek
  (pl. combo-box, multi-combo-box, multi-field, password-field, ...) működése
  megváltozott!
  A mező értéke késleltetve íródik a Re-Frame adatbázisba!

- Az x.app-sync.query-handler.* névterek most már pathom.* névtérbe költöztek!
  A [:sync/send-query! ...] esemény ezentúl [:pathom/send-query! ...] néven
  használható!



# 4.7.0
- [:ui/render-surface! ...]
  [:ui/close-surface!  ...]
  [:ui/render-popup!   ...]
  [:ui/close-popup!    ...]
  [:ui/render-bubble!  ...]
  [:ui/close-bubble!   ...]



# x4.6.9

- Az item-lister-ekben használt listaelemek összevonásra kerültek és ezentúl
  az x.app-layouts.api/list-item-a komponensként használhatók.
  Az eddig használt listaelemekhez tartozó css osztályok sem léteznek már!



# x.4.6.8

- Az `item-lister`, `item-editor`, `item-browser` és `view-selector` pluginok beállításához,
  ezentúl újra használhatók az egyes pluginok szerver-oldali `[.../init-...! ...]` eseményei!

- A pluginok útvonalait ezentúl újra a pluginok regisztrálják az útvonalkezelőnél, de most már
  ez a funkció opcionális és beállítható az `{:route-template "..."}` értéke!

- Az item-lister plugin `undo-duplicate-items!` és az item-browser plugin `undo-duplicate-item!`
  mutation függvényeit ezentúl nem szükséges létrehozni!

- A header komponensen megjelenő "Go home" / "Go up" ikon-gomb automatikusan az aktuális útvonalból
  származtatja az útvonalhoz tartozó szülő-útvonalat.
  - Az egyes útvonalak szerver-oldali regisztrálásakor lehetséges ettől eltérő szülő-útvonalat is
    beállítani a `{:route-parent "..."}` tulajdonság használatával.
  - A "Go home" / "Go up" ikon-gombot beállító események eltávolításra kerültek.

- A pluginok ezentól az `extension-id & item-namespace` paraméterezés helyett a `plugin-id` azonosítót
  fogadják.
  Pl.: `[item-lister/body :entities :entity {...}]`
        =>
       `[item-lister/body :entities.entity-lister {...}]`



# x4.6.7

- `[:environment/add-css! ...]` esemény ezentúl szerver-oldalon is használható, ha feltételesen
  szeretnél hozzáadni css fájlokat!

- A kliens-oldali `[:environment/add-external-css! ...]` esemény megszűnt és az `[:environment/add-css! ...]`
  esemény paraméterezése megváltozott!

- Az `app-fruits.dom`     névtér ezentúl `dom.api`     néven elérhető!
- Az `app-fruits.react`   névtér ezentúl `react.api`   néven elérhető!
- Az `app-fruits.reagent` névtér ezentúl `reagent.api` néven elérhető!

- Az item-editor plugin `{:initial-item {...}}` tulajdonságával ezentúl beállítható a szerkesztett
  dokumentum kezdeti állapota



# x4.6.5

- Az összes plugin nevéből kikerült az "app-" és "server-" prefixum
  `app-plugins.item-xxx.api`  =>  `plugins.item-xxx.api`

- Az összes extension nevéből kikerült az "app-" és "server-" prefixum
  `app-extensions.xxx`  =>  `extensions.xxx`

- Az item-editor plugin `(item-editor/form-id ...)` függvénye ezentúl nem elérhető.
  - Az input-ok `{:form-id ...}` tulajdonságát függvény nélkül tudod megadni.
    Pl.: `:entities.entity-editor/form`
    Pl.: `::form`
  - Az `item-editor/body` komponens számára is szükséges átadni ugyanazt a `{:form-id ...}` tulajdonságot,
    ha használni szeretnéd a funkciót!

- Az item-lister, item-editor és item-browser pluginok kliens-oldali body komponensénenek add át
  a `{:handler-key ...}` tulajdonságot, amivel tudják azonosítani, hogy melyik resolver és mutation
  függvényekhez tudnak kapcsolódni
  Pl.: `{:handler-key :entities.entity-lister}` => `:entities.entity-lister/get-items`

- Az `(item-editor/editor-uri ...)` függvény ezentúl nem elérhető, helyette az `(r item-editor/get-item-route ...)`
  subscription függvény használatával készíthetsz útvonalatat az egyes elemkhez.

- A `[:ui/set-route-parent! ...]` esemény ezentúl nem elérhető
  (a applikáció fejlécének navigációs gombja az aktuális útvonal alapján automatikusan beállítja magát)

- Az item-lister plugin `{:new-item-route ...}` tulajdonsága helyett ezentúl `{:new-item-event ...}` tulajdonság használható



# x4.6.4

- Az útvonalak `{:route-parent "..."}` beállítása ezentúl nem elérhető, helyette a kliens-oldali
  `[:ui/set-route-parent! "..."]` esemény használatával lehet beállítani a header navigációs gombjának
  viselkedését!

- A `mongo-db.api/get-all-documents` függvény ezentúl `get-collection` néven érhető el

- Ezentúl nem kötelező az item-lister plugin használatához a `:my-extension` kifejezést hozzáadni a szótárhoz!
- Ezentúl nem kötelező az item-editor plugin használatához a `:edit-my-type` és `:add-my-type`
  kifejezéseket hozzáadni a szótárhoz!

- Az `[:item-lister/init-lister! ...]` esemény nem elérhető ezentúl!
  A plugin beállításait a kliens-oldali reagent komponens számára kell átadnod!
  Az `[:item-lister/init-lister! ...]` esemény meghívása helyett add hozzá az útvonalat!

- Az `[:item-editor/init-editor! ...]` esemény nem elérhető ezentúl!
  A plugin beállításait a kliens-oldali reagent komponens számára kell átadnod!
  Az `[:item-editor/init-editor! ...]` esemény meghívása helyett add hozzá az útvonalat!



# x4.6.3

- Az x.boot-loader névtér ezentúl x.boot-loader.api néven érhető el

- Az item-editor plugin itt felsorolt mutation függvényei ezentúl nem a dokumentumot kapják
  meg a mutation-props térkép helyett, hanem egy térképet, amiben az `:item` kulcsszó alatt
  található a dokumentum
  - `my-extension.my-type-editor/duplicate-item!`   `{...}` -> `{:item {...}}`
  - `my-extension.my-type-editor/undo-delete-item!` `{...}` -> `{:item {...}}`
  - `my-extension.my-type-editor/save-item!`        `{...}` -> `{:item {...}}`

- A szerver-oldali debug eszközöket ezentúl a "/developer-tools" útvonalon lehet elérni

- Az item-lister plugin ezentúl nem tesz a listaelemek köré toggle element-et

- Az item-lister, item-browser, item-editor pluginokat ezentúl CSAK `body` és `header` komponensekre
  felbontva lehet használni



# x4.6.2

- `[:item-lister/inititialize-lister! ...]`     esemény neve ezentúl `[:item-lister/init-lister! ...]`
- `[:item-editor/inititialize-editor! ...]`     esemény neve ezentúl `[:item-editor/init-editor! ...]`
- `[:item-browser/inititialize-browser! ...]`   esemény neve ezentúl `[:item-browser/init-browser! ...]`
- `[:view-selector/inititialize-selector! ...]` esemény neve ezentúl `[:view-selector/init-selector! ...]`

- A `db/apply!` függvény neve ezentúl `db/apply-item!`

- A `[:db/apply! ...]` esemény neve ezentúl `[:db/apply-item! ...]`

- Az `[:environment/add-external-css! "..."]` eseményt ne használd! Helyett az `x.app-config.edn`
  fájlban hívd be az egyes css fájlokat!

- A `[:ui/listen-to-request! ...]` eseményt ne hívd meg! Helyette az egyes request-eknek és query-knek
  add át a `{:display-progress? true}` tulajdonságot!

- Az útvonalak `{:js "..."}` beállítása ezentúl `{:core-js "..."}` névén elérhető

- A `{:cache-control? true}` beállítást sehol nem kell ezentúl alkalmazni (automatikus lett)
  Töröld ki az `x.app-config.edn` fájlból is!



# x4.6.1

- A `[:sync/send-request! ...]` esemény ezentól fogadja a `{:validator-f ...}` függvényt

- A `pathom.validator` névtér függvényei ezentúl nem elérhetők és nem szükséges őket alkalmazni

- A `mid-fruits.validator` névtér függvényei ezentúl a `pathom.api` névtér alól érhetők el

- Az item-lister plugin `{:order-by ...}` tulajdonsága ezentúl nem elérhető

- Az item-lister plugin `{:order-by-options [...]}` tulajdonsága megváltozott



# x4.6.0

- A Re-Frame mellékhatás események paraméterezése és meghívása megváltozott!
  Dokumentáció: `x.mid-core.event-handler/reg-fx`
                `x.mid-core.event-handler/fx`
                `x.mid-core.event-handler/fx-n`

- Az `a/reg-handled-fx` mellékhatás esemény regisztráló függvény ezentúl nem elérhető



# x4.5.9

- ...



# x4.5.8

- Az `x.app/mid/server-core.api/reg-lifecycles` függvény új neve: `reg-lifecycles!` (felkiáltójellel)



# x4.5.7

- A `prototypes.api` névtér újra a mongo-db része lett (circular-dependecy feloldva). Sry.

- A stated elemek `{:disabler [...]}` tulajdonsága ezentúl nem elérhető

- A `[:syny/send-query! ...]` és `[:sync/send-request! ...]` események `{:target-paths ...}` tulajdonsága
  ezenntúl nem elérhető (használj helyette `core/transit-handler`-t!)



# x4.5.6

- A `[components/content ...]` komponens már nem kezeli a `{:content-props ...}` és `{:subscriber ...}`
  tulajdonságokat!



# x4.5.5

- A következő elemeken a `{:layout ...}` tulajdonságot leváltotta a `{:min-height ...}` tulajdonság:
  - label
  - line-diagram

- Az item-editor ezentúl a `[:my-extension :item-editor/data-item ...]` útvonal helyett,
  a `[:my-extension :item-editor/data-items ...]` útvonalat használja!

- Az item-lister plugin ezentúl kezeli a lista elemek `:on-click` eseményét, amit tulajdonságként
  át kell adni neki!

- Az item-lister plugin használatához szükséges létrehozni a `duplicate-my-type-items`
  és `undo-duplicate-my-type-items` mutation függvényeket



# x4.5.4

- Az item-editor plugin `[:item-editor/get-header-props ...]` és `[:item-editor/get-body-props ...]`
  feliratkozásában a `{:synchronizing? ...}` tulajdonság helyett a `{:disabled? ...}` tulajdonság
  segítségével lehet az inputok `{:disabled? ...}` állapotát beállítani

- A mongo-db.pipelines névtér összes függvényének paraméterezése megváltozott!

- A mongo-db field-pattern, filter-pattern, search-pattern, sort-pattern szintaxisa megváltozott!



# x4.5.3

- Az item-editor plugin példányaiban a pathom handler függvények a megváltozott mongo-db névtér
  függvényeit használják!

- A "monoset-environment/x.project-config.edn" fájl ezentúl fel van bontva
  "x.app-config.edn" és "x.server-config.edn" fájlokra.
  - Az "x.site-config.edn" fájl tartalma elérhető a kliens oldali Re-Frame adatbázisban!



# x4.5.2

- A mongo-db adatbázisban tárolt dokumentumok azonsítói ezentúl BSON ObjectId objektumként tárolódnak
  az adatbázisban. Az átálláshoz szükséges az összes kollekciót kiüríteni és a dokumentokat újból
  hozzáadni!

- Az `x.app-components.api/stated` és `x.app-components.api/subscriber` komponensek
  `{:component ...}` tulajdonsága ezentúl `{:render-f ...}` néven használható!
  `[components/subscriber {:component ...}]` -> `[components/subscriber {:render-f ...}]`

- Az item-lister és item-editor plugin kliens-oldali kezelője ezentúl nem az extension
  "render" eseményét hanem a "load" eseményét indítja el az útvonal betöltésekor!
  `[:my-extension/render-my-type-lister! ...]` -> `[:my-extension/load-my-type-lister! ...]`
  `[:my-extension/render-my-type-editor! ...]` -> `[:my-extension/load-my-type-editor! ...]`

- Az `x.app-elements.api/polarity` komponens ezentúl `.../horizontal-polarity` és `.../vertical-polarity`
  komponensekre felbontva használható!



# x4.5.0

- A route-template útvonalakban használt "/:app-home/..." változó szintaxisa megváltozott!
  "/:app-home/my-route" -> "/@app-home/my-route"

- A szerver-oldali `(x.app-core.api/reg-lifecycles {})` függvényben használt életciklusok nevei megváltoztak!
  `{:on-app-* ...}` -> `{:on-server-* ...}`



# x4.4.9

- Sry ...



# x4.4.8

- Sry ...



# x4.4.7

- Sry ...



# x4.4.6

- BREAKING CHANGE
  X Modulok Re-Frame esemény elnevezései megváltoznak:
  Pl.:
  `[:x.app-router/...]` -> `[:router/...]`
  ...

- BREAKING CHANGE
  A route-okat a szerver-oldali `x.server-router.api` modullal lehetséges mostantól hozzáadni

- BREAKING CHANGE
  `x.server-views.api/main` -> `x.server-ui.api/body`



# x4.4.5

- BREAKING CHANGE
  Full refactor, sry!



# x4.4.4

- BREAKING CHANGE
  `x.project-details.edn` -> `x.project-config.edn`

- BREAKING CHANGE
  Az `x.app-router.api/set-route!` `{:route-title ...}` tulajdonsága megszűnt.

- BREAKING CHANGE
  Az `x.app-ui.api/surface` felületeken megszűnt a `control-bar`, `control-sidebar`, `label-bar`

- BREAKING CHANGE
  Az `x.app-elements.api/card` elemen megszűnt a `border-color` tulajdonság

- NEW RENDERER
  `x.app-ui/header`



# x4.4.3

- BREAKING CHANGE
  Az `x.app-elements` modul elemein ezentúl nem használható a request-id tulajdonság,
  amivel az elemek egy request állapotától függően jelentek meg.

- BREAKING CHANGE
  Az `x.app-elements` modul elemen ezentúl nem használnak element-container komponenst.



# x4.4.2

- BREAKING CHANGE
  "/db/query" route átnevezve: "/query"

- NEW MODULE
  `x.app-layouts`

- NEW COMPONENT
  `x.app-components.api/infinite-loader`

- BREAKING CHANGE
  Az `a/reg-handled-fx` függvénnyel regisztrált kezelt mellékhatás események handler-function
  függvényeinek paramtérezése megváltozott!
  `(fn [[a b c]])` => `(fn [a b c])`

- NEW NAMESPACE
  `mid-fruits.eql`



# x4.4.1

- BREAKING CHANGE
  `project-details.edn` -> `x.project-details.edn`



# x4.4.0

- BREAKING CHANGE
  A Mongo-db csatlakozás a monoset része. A kapcsolat beállításai a `project-details.edn` fájlban.

- BREAKING CHANGE
  A `{:color ...}` tulajdonság `{:border-color ...}` tulajdonságra lett átnevezve
  a következő elemekben:
  - `x.app-elements.api/box`
  - `x.app-elements.api/card`

- BREAKING CHANGE
  Az `x.app-elements.api/select` elem számára `{:on-select ...}` és `{:on-popup-closed ...}`
  tulajdonságként átadott események az aktuálisan kiválasztott opció értékét kapják meg
  utolsó paraméterként.



# x4.3.9

- BREAKING CHANGE
  Az `x.app-elements.api/-field` elemek `{:label ...}` tulajdonságként átadott címkéje relatív
  pozicionálású lett.



# x4.3.8

- NEW PROPERTY
  `[x.app-elements.api/box  {:on-click ...}]`
  `[x.app-elements.api/card {:on-click ...}]`

- NEW PROPERTY
  `[x.app-elements.api/box  {:ghost-view? ...}]`
  `[x.app-elements.api/card {:ghost-view? ...}]`

- NEW FUNCTIONS
  `x.app-core.api/redirect-event-db`
  `x.app-core.api/redirect-event-fx`
  `x.app-core.api/redirect-sub`

- BREAKING CHANGE
  Az `[:x.app-sync/send-request! ...]` és `[:x.app-sync/send-query! ...]` események számára
  `{:on-responsed ...}`, `{:on-success ...}` és `{:on-failure ...}` tulajdonságként átadott
  események nem a request-id értékét, hanem a szerver-válasz értékét kapják utolsó paraméterként!



# x4.3.7

- CHANGE
  A `mid-fruits.map` névtérben a függvények neveiben az "item" kifejezés "value" kifejezésre
  lett cserélve!

- NEW ELEMENT
  `x.app-elements.api/image`

- NEW ELEMENT
  `x.app-elements.api/expandable`

- NEW PROPERTY
  `project-details.edn`
  `{:app-details {:app-codename "App rövid neve" :app-title "App hosszú neve"}}`

- NEW PROPERTY
  `project-details.edn`
  `{:seo-details {:og-description "" :og-title ""}}`

- NEW PROPERTY
  `[:x.app-environment.keypress-handler/reg-keypress-event! {:prevent-default? ...}]`



# x4.3.6

- NEW PROPERTY
  `x.app-elements.api/text-field` és `x.app-elements.api/search-field`
  `{:modifier #(...)}`
  A mezőben megjelenő értékét real-time módosító függvény

- NEW PROPERTY
  `[x.app-components/subscriber {:test-f ...}]`
  Pl.:
  `[x.app-components/subscriber {:test-f #(subscribed-value-valid-for-rendering-the-component? %)}]`

- BREAKING CHANGE
  A sortable plugin ezentúl partíció helyett vektorban rendezi az elemeket.
  Az új változat ideiglenesen sortable-2 néven elérhető.   



# x4.3.5

- NEW FUNCTION
  `mid-fruits.map/get-ordered-keys`



# x4.3.4

- NEW FEATURE
  A developer-mode használata közben megnyitott popup UI-elemeket lehetséges minimize-olni.

- NEW PROPERTY
  `[:x.app-sync/send-request! {:on-responsed ...}]`

- NEW FEATURE
  Az `x.server-router` ezentúl kezeli a route konfliktusokat.
  Pl.: "/my-route/my-page" és "/my-route/:my-var" route-template-ek egyidejű használata is lehetséges.
  A route-ok hozzáadásának sorrendje nem számít.
  A router indulásakor azok megfelelő sorrendbe rendeződnek.

- NEW ELEMENT
  `x.app-elements.api/date-field`

- NEW FUNCTIONS
  `app-fruits.dom/get-selection-start`
  `app-fruits.dom/get-selection-end`
  `app-fruits.dom/get-selection-range`
  `app-fruits.dom/set-selection-start!`
  `app-fruits.dom/set-selection-end!`
  `app-fruits.dom/set-selection-range!`

- NEW FUNCTIONS
  `app-fruits.dom/append-to-form-data!`
  `app-fruits.dom/merge-to-form-data!`
  `app-fruits.dom/file-selector->form-data`

- NEW FUNCTIONS
  `mid-fruits.time/unix-timestamp->*`
  UNIX timestamp pretty-print segédfüggvények



# x4.3.3

- NEW PROPERTIES
  `project-details.edn`
  `{:storage-details
    {:max-upload-size 1000000000
     :storage-capacity 15000000000}}`

- NEW PROPERTIES
  `project-details.edn`
  `{:site-links
    {:privacy-policy {...}
     :terms-of-service {...}
     :what-cookies-are? {...}}`

- BUGFIX
  `x.app-components.api/content` komponens base-props átadási hiba javítva

- NEW PROPERTY
  `x.app-elements.api/*` `{:stickers {:disabled? ....}}`

- NEW-FUNCTIONS
  `app-fruits.dom/file->*`
  `app-fruits.dom/file-selector->*`

- NEW FEATURE
  Az `(r x.app-dictionary/get-term db ...)` függvény kezeli a behelyettesítéséket
  Pl.: `(r x.app-dictionary/add-term! db :my-name-is {:en "Hi, my name is %"})`
       `(r x.app-dictionary/get-term  db :my-name-is {:replacements ["John"]})`
       => "Hi, my name is John"

- NEW FUNCTION
  `mid-fruits.string/use-replacements`

- CHANGE
  `[elements.api/label {:horizontal-align ...}]`
  Default: `:center`
  =>
  Default: `:left`

- CHANGE
  `[:x.app-ui/add-popup! {:content-stretched? ...}]`
  =>
  `[:x.app-ui/add-popup! {:stretched? ...}]`

- NEW ELEMENT
  `x.app-elements.api/table`

- NEW PROPERTY
  `[x.app-elements.api/column {:wrap-items? ...}]`
  `[x.app-elements.api/row    {:wrap-items? ...}]`

- NEW NAMESPACE
  `mid-fruits.geometry`

- CHANGE
  `mid-fruits.css/value` függvény több függvényre lett felbontva

- NEW PROPERTY
  Az `[:x.app-ui/add-popup! ...]` esemény számára átadott `{:autopadding? ...}`
  tulajdonság használatával ki- és bekapcsolható a `{:layout :boxed}` popup UI-elemeken  
  kirenderelt tartalmat körülvevő padding.

- CHANGE
  Az `x.app-ui` névterekben megszűnt a `{:ghost-content ...}` és `{:request-id ...}` tulajdonság

- NEW COMPONENT
  `x.app-components/listener`

- NEW FUNCTIONS
  `x.app-db/get-document-item`
  `x.app-db/update-document-item`

- NEW FUNCTIONS
  `x.server-environment/get-local-document-item`
  `x.server-environment/update-local-document-item`

- NEW ELEMENT
  `x.app-elements.api/multi-combo-box`

- NEW FUNCTION
  `x.app-db/set-vector-item!`



# x4.3.2

- NEW FUNCTION
  `mid-fruits.vector/conj-some`

- CHANGE
  `x.app-elements.api/autocomplete-field` átnevezve `x.app-elements.api/combo-box`

- NEW PROPERTY
  `x.app-elements.api/text-field {:min-width ...}`

- NEW ELEMENTS
  `x.app-elements.api/chip`
  `x.app-elements.api/chips`



# x4.3.1

- NEW ELEMENT
  `x.app-elements.api/autocomplete-field`

- CHANGE
  Az `x.app-elements.api/text-field`, `x.app-elements.api/search-field`, `x.app-elements.api/multiline-field`
  elemek `{:surface {:content ...}}` komponens paraméterezése megváltozott.

- CHANGE
  Az `x.app-elements.api/select` elem ezentúl nem button elemként jelenik meg,
  hanem hagyományos select-button elemként, ami a kiválaszott értékét mutatja.
  Ha button elemként szeretnéd megjeleníteni, akkor a select névterének
  dokumentációjában megtalálod az ide vonatkozó leírást.



# x4.3.0

- CHANGE
  `x.app-ui/popups {:control-bar {...}}`
  A popup UI elemek control-bar sávja már nem látott el funkciót, ezért megszűnt.
  (A surface UI elemek control-bar sávja továbbra is elérhető és lefele görgetés
   esetén eltűnik, felfele görgetés esetén megjelenik)



# x4.2.9

- NEW PROPERTY
  A kollekciókat partíciókká alakító függvényeket ezentúl lehetséges
  `{:keywordize? false}` beállítással is használni

- CHANGE
  `[x.app-sync.route-handler]` -> `[x.app-router.route-handler]`

- NEW PROPERTIES
  `[x.app-elements.api/card      {:context-surface {...}}]`
  `[x.app-elements.api/directory {:context-surface {...}}]`
  `[x.app-elements.api/file      {:context-surface {...}}]`

- NEW ELEMENT
  `x.app-elements.api/multi-field`

- NEW FUNCTION
  `x.app-db/collection->map`

- NEW FUNCTION
  `x.app-db/apply-data-items!`
  A paraméterként átadott függvényt végrehajtja egy partició összes elemén



# x4.2.8

- NEW FEATURE
  app-menu / developer-tools / database-browser a térképek megtekintése eredeti
  formájukban is lehetséges

- NEW PROPERTY
  `[:x.app-sync/send-request! {:modifier ...}]`,
  `[:x.app-sync/send-query!   {:modifier ...}]`
  Módosítja a szerver-válasz értéket, annak eltárolása előtt

- NEW PROPERTY
  `[:x.app-sync/send-request! {:target-paths {...}}]`
  Disztribútolja a szerver-válasz térkép elemeit különböző adatbázis útvonalakra.

- NEW FEATURE
  app-menu / developer-tools / database-browser felismeri Re-Frame subscription-vectort
  és az event-vectort és lehetséges ezekre feliratkozni vagy dispatch-elni!

- NEW PROPERTY
  `[:x.app-sync/send-request! {:on-sent ...}]`

- NEW EVENT
  `[:x.app-sync/freeze-current-route!]`

- NEW FUNCTIONS
  `x.app-sync/get-freezed-route-*`

- CHANGE
  Az `x.app-sync/send-request!` `{:idle-timeout ...}` alapértéke 2000ms-ről 250ms-re változott

- NEW FUNCTION
  `x.app-db/explode-collection`
  Szétválogatja a különböző namespace-el rendelkező dokumentumokat az átadott mixed-collection-ből

- CHANGE
  `x.app-elements.api/broker` MEGSZŰNT!

- CHANGE
  `x.app-elements.api/element-list` MEGSZŰNT!

- CHANGE
  `x.app-elements.api/form` MEGSZŰNT!

- CHANGE
  `x.app-elements.api/polarity` MEGVÁLTOZOTT!

- NEW NAMESPACE
  `x.app-sync/query-handler`

- NEW FEATURE
  Az `x.app-elements` modul input elemeinek már nem kötelező a `{:value-path [...]}` tulajdonság megadása



# x4.2.7

- NEW COMPONENT
  `x.app-components/value`
  Megvalósítja az új metamorphic-value típust

- NEW PROPERTIES
  `[:x.app-sync/add-route! {:window-title ...}]`
  `[:x.app-sync/set-home!  {:window-title ...}]`

- NEW PROPERTY
  `[x.app-elements.api/select {:on-popup-closed ...}]`

- NEW ELEMENT
  `x.app-elements.api/line-diagram`

- NEW PROPERTY
  `x.app-elements.api/select` `{:autoclear? true}` tulajdonság, a select popup bezárása
  után törli a select-el beállított értéket.
  Pl.: Ha valami action kiválasztására használsz select elemet, akkor a select
  megnyitásakor ne emlékezzen az előzőleg kiválasztott értékre.
  Az `{:on-select ...}` esemény megtörténtekor a kiválaszott érték MÉG elérhető a db-ben!

- NEW FEATURE
  Adatvédelmi beállítások
  `[:x.app-views.cookie-settings/render!]`

- NEW PROPERTIES
  `x.app-elements.api/button` `{:layout :icon-button :height ...}` & `{:layout :icon-button :width ...}`

- CHANGE
  Az `x.app-elements.api/select` elem `{:on-select ...}` eseménye az `[:x.app-ui/close-upper-popup!]`
  esemény helyett az `[:x.app-ui/close-popup! ...]` esemény használatával zárja be a select popup UI elemet.

- CHANGE
  Az `elements.api/button` elem preset-jei ezentúl nem tartalmaznak `:on-click` előre beállított
  tulajdonságot (pl.: `[:x.app-ui/close-upper-popup!]`, `[:x.app-sync/go-to!]`, stb)



# x4.2.6

- NEW FILE
  `CHANGES.md`

- Az `x.app-elements.api/select` elem popup ablakának label-je a és a select button
  label-je külön is megadható
  `{:label ...}` & `{:options-label ...}`

- NEW PROPERTY
  `[x.app-elements.api/select {:options-label ...}]`

- NEW FEATURE
  Az `x.app-elements.api/password-field`, `x.app-elements.api/search-field`, `x.app-elements.api/text-field`
  elemek `{:emptiable? true}` tulajdonsággal az ESC billentyű lenyomására kiürülnek.

- NEW FEATURE
  Az `x.app-elements.api/multiline-field`, `x.app-elements.api/password-field`, `x.app-elements.api/search-field`,
  `x.app-elements.api/text-field` elemek `{:on-enter ...}` tulajdonságaként átadott esemény az ENTER
  billentyű lenyomására megtörténik.

- NEW ELEMENT
  `x.app-elements.api/vertical-line`

- NEW FUNCTION
  `x.mid-core.event-handler/dispatch-cond`

- NEW FEATURE
  Minden input elementnek megadható `{:default-value ...}` és `{:initial-value ...}` tulajdonság.

- NEW PROPERTY
  Az `x.app-elements.api/multiline-field`, `x.app-elements.api/password-field`, `x.app-elements.api/search-field`,
  `x.app-elements.api/text-field` elemek `{:placeholder ...}` tulajdonsága is használható a `{:label ...}`
  tulajdonság helyett.

- NEW FUNCTIONS
  `mid-fruits.vector/move-item`
  `mid-fruits.vector/move-first-occurence`

- NEW-FUNCTION
  `x.app-db/move-data-item!`
