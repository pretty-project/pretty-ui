
# Az elemek az azonosítójuk alapján vannak kijelölve
# XXX#8891
  Az x4.7.5 verzióig, a több elemet kezelő pluginok a kijelölt elemek indexeit
  tárolták az elemek azonosítói helyett, mert az indexekkel dolgozni kevesebb
  erőforrást igényel, mint az elemek azonosítóival való számítások.

  Az x4.7.5 verziótól, a több elemet kezelő pluginok a kijelölt elemek azonosítóit
  tárolják, így az item-browser plugin a különböző böngészhető elemek alelemeinek
  kijelöléseit egyszerre képes kezelni.
  Pl.: Egy fájlkezelőben egyszerre több mappában lehetséges fájlokat kijelölni,
       ezért az elemek indexei nem alkalmasak a kijelölések kezelésére.
