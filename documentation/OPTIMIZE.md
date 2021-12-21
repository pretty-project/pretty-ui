
# Reagent optimalizáció

1. A (let []) függvényben végzett számítások csak egyszer futnak le, a renderelő függvényben végzett
   számítások, viszont a komponens minden újrarenderelésekor lefutnak

```
(defn xxx []
 [:div {:on-mouse-over #(do-something! ...)} "Hello"])
```

Helyett hasznald ezt, mert igy csak egyszer generalodik le az anonymous function:

```
(defn xxx []
 (let [do-something-f #(do-something! ...)]
      [:div {:on-mouse-over do-something-f} "Hello"]))
```



2. A reagent ugy donti el, hogy szukseges-e egy komponens ujrarenderelese, hogy
   osszehasonlitja a komponens parametereit azok valtozas elotti ertekevel, ezert fontos,
   hogy minel kisebb es kevesebb parametert kapjanak a komponensek



# Re-Frame optimalizáció
