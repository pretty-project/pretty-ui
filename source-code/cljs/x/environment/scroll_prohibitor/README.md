
# scroll-prohibitor
- Letiltja az oldalon való görgetést.

- A tiltások saját azonosítóval rendelkeznek és egyszerre több tiltás is lehet érvényben.



# XXX#7650
- A következő beállítások teszik lehetővé az egyes böngészőkben a görgetés letiltását:

- Opera browser 76.0 (MacOS 10.15.7)
  [:html {:style {:overflow "hidden"}}] or [:body {:style {:overflow "hidden"}}]

- Mozilla Firefox 88.0 (MacOS 10.15.7)
  [:html {:style {:overflow "hidden"}}] or [:body {:style {:overflow "hidden"}}]

- Safari 13.1 (MacOS 10.15.7)
  [:html {:style {:overflow "hidden"}}]

- Google Chrome 90.0 (MacOS 10.15.7)
  [:html {:style {:overflow "hidden"}}] or [:body {:style {:overflow "hidden"}}]

- Google Chrome for mobile 86.0 (iOS 14.3, iPhone 6s)
  [:body {:style {:position "fixed"}}]
  WARNING! Lehúzással nem lehetséges frissíteni az oldalt, ha a görgetés le van tiltva!

- Google Chrome for mobile 90.0 (Google Android ?, Samsung S8+)
  TODO ...

- Safari ? (iOS 14.3, iPhone 6s)
  TODO ...



# XXX#7659
  Az app inicializálásakor, amikor még látható a töltőképernyő, de az app-container
  elemen már megjelent olyan tartalom ami 100 vh-nál magasabb, akkor felvillan a scrollbar.
  Ennek elkerülése végett a html elemen, inline-style-ként legyen rögzítve az "overflow: hidden" tulajdonság.



# WARNING!
  Google Chrome for mobile 90.0 (Google Android ?, Samsung S8+)
  A @dnd-kit/sortable elemei hibásan érzékelik scroll-y értéket,
  ha a [:html {:style {:overflow-y "scroll"}}] érték be van állítva.
  A görgetés tiltásának feloldása után ezért az {:overflow-y "hidden"} értéket törölni szükséges,
  {:overflow-y "scroll"} értékre állítás helyett.
