
# A {:query-params {...}} térkép ...
# XXX#7061 (cljs/engines/engine-handler/README.md)



# Mire való az engine body komponensének transfer-id tulajdonsága?
# XXX#8173 (cljs/engines/engine-handler/transfer/README.md)



# Ne használd az input mezők initial-value tulajdonságát!
# XXX#7188 (cljs/engines/item-handler/README.md)
  Az engine számára átadott form-element komponensben ne használj olyan input mezőt,
  ami {:initial-value ...} tulajdonsággal rendelkezik, mert ...
  Pl.: Új elem létrehozásakor az input mezők {:initial-value ...} értékei megváltoztatják a dokumentumot ...
       ... és ha a felhasználó a dokumentum változtatása nélkül elhagyja a szerkesztőt, akkor az tévesen
           úgy érzékelné, hogy a dokumentumot a felhasználó változtatta meg és az elhagyás után felajánlaná
           a "Nem mentett változtatások visszaállítása" lehetőségét!
       ... és a "Visszaállítás" gomb felajánlja a felhasználónak az elem eredeti állapotra történő visszaállítását!
