
- A {width: fit-content} CSS beállítás megoldja, hogy a block szülő elemben lévő
  elementek ne legyenek automatikusan 100% szélesek. Pl. gomboknál! De fel kell
  tenni az összes olyan elemre, amit érint a probléma



- gombok presetjei nem szükségesek

- Használj set-eket! #(1 2 3)
  + (set ...) set függvény!
  - Egy elem egyszer fordulhat elő benne

- Az elementeknél inább element-structure sok helyen, mint element-body a proper elnevezés

- Read this bible: https://developer.mozilla.org/en-US/docs/Web/Performance

- SEO: https://schema.org/docs/gs.html
       https://stackoverflow.com/questions/29928974/what-is-the-purpose-of-meta-itemprop

- Ha több popup van megnyitva, akkor az ENTER és ESC billentyűk csak a legfelsőre hassanak!

- A css class és data-attribute selector-ok elerési ideje köbö egyforma.
  Egyes elemeken pl. buttonokon rengeteg data-attribute van, amit ugy lehet gyorsitani, hogy
  a css fájlba lennének preset-ek! [:div.x-button {:data-preset :my-preset}]
  Így a border-radius, hover-color, color, stb nem 5-6-7 data-attribute használatával
  volna meghatározva, hanem volna 20-30 preset azt csá.
  Ezt majd akkor amikor több fájlra lesz darabolva az elements/style.css

- ha egy button keypress tulajdonságának megadsz egy billentyűt pl 13 / enter és rátenyerelsz az
  adott billentyűre, akkor sokszor egymás utánban megtörténik az button on-click eventje,
  ami full cink, pl fájl letöltésnél hatszor tölti le a fájlt és hasonlok

- Multi-combo-box elemben igy nézzen ki egy placeholder: "green, red, purple"
  Szóval vesszővel lehet elválasztani a tag-eket és a vessző billentyű leütése is viselkedjen
  ugyanugy mint az enter (kiüriti a mezőt és hozzáad egy chip-et)

- A text-field mérete megnő ha elég hosszu a helper alatta

- Text-field nem reagál az ESC billentyűra, amikor emptiable? true (a combo-box reagál)

- Text-field szélessége megváltozik az emptiable? true adornment (és más adornment) ki-bekapcsolásakor

- Input validálás: egy input több validátort is fogadjon több hibaüzenettel és néha több input egymástól
  függ pl. password összehasonlító

- A select popup-on ha elkezdesz beirni valamit, akkor arra reagaljon

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

- Multi field keypress vezérlés

- Ha egy popup label bar label nem fér, ki -> akkor text-overflow: ellipsis

- DRAG-OVERLAY-t berakni!

- Sortable mozgatás picit laggos iOS Chrome-on

- Favicon-ok rendbetétele, home-screen icon méretek használata, apple-touch-icon
  beállítása
  https://css-tricks.com/favicon-quiz/

- aria-label, aria-hidden

- css :in-range selector

- Ha text-field element {:resetable? true}, akkor az ESC billentyű lenyomására
  reseteljen

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
