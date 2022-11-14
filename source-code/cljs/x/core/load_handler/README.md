
# XXX#5607
- Az applikáció elemeinek betöltésekor lehetőség van az egyes elemek aszinkron
  inicializására.



# ...
- Egy folyamat inicializálásának kezdetekor az [:x.core/start-synchron-signal! ...] eseménnyel
  lehetséges jelezni a load-handler számára, hogy addig ne tekintse befejezettnek az applikáció
  betöltését, amíg ugyanez az a folyamat nem indítja el a [:x.core/end-synchron-signal! ...] eseményt.
