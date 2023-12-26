
# BUG#8493
Ha megváltoznak egy UI elem adatbázisban tárolt tulajdonságai, akkor
a UI elemen kirenderelt tartalom is újra renderelődik, ugyanis a kirenderelt
tartalom egyik felmenő komponense a UI elem wrapper komponense, aminek
a megváltozó tulajdonságok miatt változik az egyik paramétere és ez a teljes
komponens újrarenderelését okozza.
