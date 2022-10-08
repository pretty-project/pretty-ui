
# A {:query-params {...}} térkép ...
# XXX#7061 (cljs/plugins/plugin-handler/README.md)



# Mire való a plugin body komponensének transfer-id tulajdonsága?
# XXX#8173 (cljs/plugins/plugin-handler/transfer/README.md)



# Ne használd az input mezők initial-value tulajdonságát!
# XXX#7188 (cljs/plugins/item-editor/README.md)
  A plugin számára átadott form-element komponensben ne használj olyan input mezőt,
  ami {:initial-value ...} tulajdonsággal rendelkezik, mert ...
  Pl.: Új elem létrehozásakor az input mezők {:initial-value ...} értékei megváltoztatják a dokumentumot ...
       ... és ha a felhasználó a dokumentum változtatása nélkül elhagyja a szerkesztőt, akkor az tévesen
           úgy érzékelné, hogy a dokumentumot a felhasználó változtatta meg és az elhagyás után felajánlaná
           a "Nem mentett változtatások visszaállítása" lehetőségét!
       ... és a "Visszaállítás" gomb felajánlja a felhasználónak az elem eredeti állapotra történő visszaállítását!
