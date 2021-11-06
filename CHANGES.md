
# x4.4.4

- BREAKING CHANGE
  x.project-details.edn -> x.project-config.edn

- BREAKING CHANGE
  Az x.app-router.api/set-route! {:route-title ...} tulajdonsága megszűnt.

- BREAKING CHANGE
  Az x.app-ui.api/surface felületeken megszűnt a control-bar, control-sidebar, label-bar

- BREAKING CHANGE
  Az x.app-elements.api/card elemen megszűnt a border-color tulajdonság

- NEW RENDERER
  x.app-ui/header



# x4.4.3

- BREAKING CHANGE
  Az x.app-elements modul elemein ezentúl nem használható a request-id tulajdonság,
  amivel az elemek egy request állapotától függően jelentek meg.

- BREAKING CHANGE
  Az x.app-elements modul elemen ezentúl nem használnak element-container komponenst.



# x4.4.2

- BREAKING CHANGE
  "/db/query" route átnevezve: "/query"

- NEW MODULE
  x.app-layouts

- NEW COMPONENT
  x.app-components.api/infinite-loader

- BREAKING CHANGE
  Az a/reg-handled-fx függvénnyel regisztrált kezelt mellékhatás események handler-function
  függvényeinek paramtérezése megváltozott!
  (fn [[a b c]]) => (fn [a b c])

- NEW NAMESPACE
  mid-fruits.eql  



# x4.4.1

- BREAKING CHANGE
  project-details.edn -> x.project-details.edn



# x4.4.0

- BREAKING CHANGE
  A Mongo-db csatlakozás a monoset része. A kapcsolat beállításai a project-details.edn fájlban.

- BREAKING CHANGE
  A {:color ...} tulajdonság {:border-color ...} tulajdonságra lett átnevezve
  a következő elemekben:
  - x.app-elements.api/box
  - x.app-elements.api/card

- BREAKING CHANGE
  Az x.app-elements.api/select elem számára {:on-select ...} és {:on-popup-closed ...}
  tulajdonságként átadott események az aktuálisan kiválasztott opció értékét kapják meg
  utolsó paraméterként.



# x4.3.9

- BREAKING CHANGE
  Az x.app-elements.api/-field elemek {:label ...} tulajdonságként átadott címkéje relatív
  pozicionálású lett.



# x4.3.8

- NEW PROPERTY
  [x.app-elements.api/box  {:on-click ...}]
  [x.app-elements.api/card {:on-click ...}]

- NEW PROPERTY
  [x.app-elements.api/box  {:ghost-view? ...}]
  [x.app-elements.api/card {:ghost-view? ...}]

- NEW FUNCTIONS
  x.app-core.api/redirect-event-db
  x.app-core.api/redirect-event-fx
  x.app-core.api/redirect-sub

- BREAKING CHANGE
  Az [:x.app-sync/send-request! ...] és [:x.app-sync/send-query! ...] események számára
  {:on-responsed ...}, {:on-success ...} és {:on-failure ...} tulajdonságként átadott
  események nem a request-id értékét, hanem a szerver-válasz értékét kapják utolsó paraméterként!



# x4.3.7

- CHANGE
  A mid-fruits.map névtérben a függvények neveiben az "item" kifejezés "value" kifejezésre
  lett cserélve!

- NEW ELEMENT
  x.app-elements.api/image

- NEW ELEMENT
  x.app-elements.api/expandable

- NEW PROPERTY
  project-details.edn
  {:app-details {:app-codename "App rövid neve" :app-title "App hosszú neve"}}

- NEW PROPERTY
  project-details.edn
  {:seo-details {:og-description "" :og-title ""}}  

- NEW PROPERTY
  [:x.app-environment.keypress-handler/reg-keypress-event! {:prevent-default? ...}]



# x4.3.6

- NEW PROPERTY
  x.app-elements.api/text-field és x.app-elements.api/search-field
  {:modifier #(...)}
  A mezőben megjelenő értékét real-time módosító függvény

- NEW PROPERTY
  [x.app-components/subscriber {:test-f ...}]
  Pl.:
  [x.app-components/subscriber {:test-f #(subscribed-value-valid-for-rendering-the-component? %)}]

- BREAKING CHANGE
  A sortable plugin ezentúl partíció helyett vektorban rendezi az elemeket.
  Az új változat ideiglenesen sortable-2 néven elérhető.   



# x4.3.5

- NEW FUNCTION
  mid-fruits.map/get-ordered-keys



# x4.3.4

- NEW FEATURE
  A developer-mode használata közben megnyitott popup UI-elemeket lehetséges
  minimize-olni.

- NEW PROPERTY
  [:x.app-sync/send-request! {:on-responsed ...}]

- NEW FEATURE
  Az x.server-router ezentúl kezeli a route konfliktusokat.
  Pl.: "/my-route/my-page" és "/my-route/:my-var" route-template-ek egyidejű használata
  is lehetséges.
  A route-ok hozzáadásának sorrendje nem számít.
  A router indulásakor azok megfelelő sorrendbe rendeződnek.

- NEW ELEMENT
  x.app-elements.api/date-field

- NEW FUNCTIONS
  app-fruits.dom/get-selection-start
  app-fruits.dom/get-selection-end
  app-fruits.dom/get-selection-range
  app-fruits.dom/set-selection-start!
  app-fruits.dom/set-selection-end!
  app-fruits.dom/set-selection-range!

- NEW FUNCTIONS
  app-fruits.dom/append-to-form-data!
  app-fruits.dom/merge-to-form-data!
  app-fruits.dom/file-selector->form-data

- NEW FUNCTIONS
  mid-fruits.time/unix-timestamp->*
  UNIX timestamp pretty-print segédfüggvények



# x4.3.3

- NEW PROPERTIES
  project-details.edn
  {:storage-details
   {:max-upload-size 1000000000
    :storage-capacity 15000000000}}

- NEW PROPERTIES
  project-details.edn
  {:site-links
   {:privacy-policy {...}
    :terms-of-service {...}
    :what-cookies-are? {...}}

- BUGFIX
  x.app-components.api/content komponens base-props átadási hiba javítva

- NEW PROPERTY
  x.app-elements.api/* {:stickers {:disabled? ....}}

- NEW-FUNCTIONS
  app-fruits.dom/file->*
  app-fruits.dom/file-selector->*

- NEW FEATURE
  Az (r x.app-dictionary/get-term db ...) függvény kezeli a behelyettesítéséket
  Pl.: (r x.app-dictionary/add-term! db :my-name-is {:en "Hi, my name is %"})
       (r x.app-dictionary/get-term  db :my-name-is {:replacements ["John"]})
       => "Hi, my name is John"

- NEW FUNCTION
  mid-fruits.string/use-replacements

- CHANGE
  [elements.api/label {:horizontal-align ...}]
  Default: :center
  =>
  Default: :left

- CHANGE
  [:x.app-ui/add-popup! {:content-stretched? ...}]
  =>
  [:x.app-ui/add-popup! {:stretched? ...}]

- NEW ELEMENT
  x.app-elements.api/table

- NEW PROPERTY
  [x.app-elements.api/column {:wrap-items? ...}]
  [x.app-elements.api/row {:wrap-items? ...}]

- NEW NAMESPACE
  mid-fruits.geometry

- CHANGE
  mid-fruits.css/value függvény több függvényre lett felbontva

- NEW PROPERTY
  Az [:x.app-ui/add-popup! ...] esemény számára átadott {:autopadding? ...}
  tulajdonság használatával ki- és bekapcsolható a {:layout :boxed} popup UI-elemeken  
  kirenderelt tartalmat körülvevő padding.

- CHANGE
  Az x.app-ui névterekben megszűnt a {:ghost-content ...} és {:request-id ...}
  tulajdonság

- NEW COMPONENT
  x.app-components/listener

- NEW FUNCTIONS
  x.app-db/get-document-item
  x.app-db/update-document-item

- NEW FUNCTIONS
  x.server-environment/get-local-document-item
  x.server-environment/update-local-document-item

- NEW ELEMENT
  x.app-elements.api/multi-combo-box

- NEW FUNCTION
  x.app-db/set-vector-item!



# x4.3.2

- NEW FUNCTION
  mid-fruits.vector/conj-some

- CHANGE
  x.app-elements.api/autocomplete-field átnevezve x.app-elements.api/combo-box

- NEW PROPERTY
  x.app-elements.api/text-field {:min-width ...}

- NEW ELEMENTS
  x.app-elements.api/chip
  x.app-elements.api/chips



# x4.3.1

- NEW ELEMENT
  x.app-elements.api/autocomplete-field

- CHANGE
  Az x.app-elements.api/text-field, x.app-elements.api/search-field, x.app-elements.api/multiline-field
  elemek {:surface {:content ...}} komponens paraméterezése megváltozott.

- CHANGE
  Az x.app-elements.api/select elem ezentúl nem button elemként jelenik meg,
  hanem hagyományos select-button elemként, ami a kiválaszott értékét mutatja.
  Ha button elemként szeretnéd megjeleníteni, akkor a select névterének
  dokumentációjában megtalálod az ide vonatkozó leírást.



# x4.3.0

- CHANGE
  x.app-ui/popups {:control-bar {...}}
  A popup UI elemek control-bar sávja már nem látott el funkciót, ezért megszűnt.
  (A surface UI elemek control-bar sávja továbbra is elérhető és lefele görgetés
   esetén eltűnik, felfele görgetés esetén megjelenik)



# x4.2.9

- NEW PROPERTY
  A kollekciókat partíciókká alakító függvényeket ezentúl lehetséges
  {:keywordize? false} beállítással is használni

- CHANGE
  [x.app-sync.route-handler] -> [x.app-router.route-handler]

- NEW PROPERTIES
  [x.app-elements.api/card      {:context-surface {...}}]
  [x.app-elements.api/directory {:context-surface {...}}]
  [x.app-elements.api/file      {:context-surface {...}}]

- NEW ELEMENT
  x.app-elements.api/multi-field

- NEW FUNCTION
  x.app-db/collection->map

- NEW FUNCTION
  x.app-db/apply-data-items!
  A paraméterként átadott függvényt végrehajtja egy partició összes elemén



# x4.2.8

- NEW FEATURE
  app-menu / developer-tools / database-browser a térképek megtekintése eredeti
  formájukban is lehetséges

- NEW PROPERTY
  [:x.app-sync/send-request! {:modifier ...}],
  [:x.app-sync/send-query!   {:modifier ...}]
  Módosítja a szerver-válasz értéket, annak eltárolása előtt

- NEW PROPERTY
  [:x.app-sync/send-request! {:target-paths {...}}]
  Disztribútolja a szerver-válasz térkép elemeit különböző adatbázis útvonalakra.

- NEW FEATURE
  app-menu / developer-tools / database-browser felismeri Re-Frame subscription-vectort
  és az event-vectort és lehetséges ezekre feliratkozni vagy dispatch-elni!

- NEW PROPERTY
  [:x.app-sync/send-request! {:on-sent ...}]

- NEW EVENT
  [:x.app-sync/freeze-current-route!]

- NEW FUNCTIONS
  x.app-sync/get-freezed-route-*

- CHANGE
  Az x.app-sync/send-request! :idle-timeout alapértéke 2000ms-ről 250ms-re változott

- NEW FUNCTION
  x.app-db/explode-collection
  Szétválogatja a különböző namespace-el rendelkező dokumentumokat az átadott mixed-collection-ből

- CHANGE
  x.app-elements.api/broker MEGSZŰNT!

- CHANGE
  x.app-elements.api/element-list MEGSZŰNT!

- CHANGE
  x.app-elements.api/form MEGSZŰNT!

- CHANGE
  x.app-elements.api/polarity MEGVÁLTOZOTT!

- NEW NAMESPACE
  x.app-sync/query-handler

- NEW FEATURE
  Az x.app-elements modul input elemeinek már nem kötelező a {:value-path [...]}
  tulajdonság megadása



# x4.2.7

- NEW COMPONENT
  x.app-components/value
  Megvalósítja az új metamorphic-value típust

- NEW PROPERTIES
  [:x.app-sync/add-route! {:window-title ...}]
  [:x.app-sync/set-home!  {:window-title ...}]

- NEW PROPERTY
  x.app-elements.api/select {:on-popup-closed ...}

- NEW ELEMENT
  x.app-elements.api/line-diagram

- NEW PROPERTY
  x.app-elements.api/select {:autoclear? true} tulajdonság, a select popup bezárása
  után törli a select-el beállított értéket.
  Pl.: Ha valami action kiválasztására használsz select elemet, akkor a select
  megnyitásakor ne emlékezzen az előzőleg kiválasztott értékre.
  Az on-select esemény megtörténtekor a kiválaszott érték MÉG elérhető a db-ben!

- NEW FEATURE
  Adatvédelmi beállítások
  [:x.app-views.cookie-settings/render!]

- NEW PROPERTIES
  x.app-elements.api/button {:layout :icon-button :height ...} & {:layout :icon-button :width ...}

- CHANGE
  A select elem on-select eseménye az [:x.app-ui/close-upper-popup!] esemény
  helyett az [:x.app-ui/close-popup! ...] esemény használatával zárja be a select
  popup UI elemet

- CHANGE
  Az elements.api/button elem preset-jei ezentúl nem tartalmaznak :on-click előre beállított
  tulajdonságot (pl.: [:x.app-ui/close-upper-popup!], [:x.app-sync/go-to!], stb)



# x4.2.6

- NEW FILE
  CHANGES.md

- Az x.app-elements.api/select elem popup ablakának label-je a és a select button
  label-je külön is megadható
  {:label ...} & {:options-label ...}

- NEW PROPERTY
  [x.app-elements.api/select {:options-label ...}]

- NEW FEATURE
  Az x.app-elements.api/password-field, x.app-elements.api/search-field, x.app-elements.api/text-field
  elemek {:emptiable? true} tulajdonsággal az ESC billentyű lenyomására kiürülnek.

- NEW FEATURE
  Az x.app-elements.api/multiline-field, x.app-elements.api/password-field, x.app-elements.api/search-field,
  x.app-elements.api/text-field elemek {:on-enter ...} tulajdonságaként átadott esemény az ENTER
  billentyű lenyomására megtörténik.

- NEW ELEMENT
  x.app-elements.api/vertical-line

- NEW FUNCTION
  x.mid-core.event-handler/dispatch-cond

- NEW FEATURE
  Minden input elementnek megadható {:default-value ...} és {:initial-value ...} tulajdonság.

- NEW PROPERTY
  Az x.app-elements.api/multiline-field, x.app-elements.api/password-field, x.app-elements.api/search-field,
  x.app-elements.api/text-field elemek {:placeholder ...} tulajdonsága is használható a {:label ...}
  tulajdonság helyett.

- NEW FUNCTIONS
  mid-fruits.vector/move-item
  mid-fruits.vector/move-first-occurence

- NEW-FUNCTION
  x.app-db/move-data-item!
