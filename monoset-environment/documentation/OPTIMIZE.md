
# Reagent optimalizáció

1. A (let []) függvényben végzett számítások csak egyszer futnak le, a renderelő függvényben végzett
   számítások, viszont a komponens minden újrarenderelésekor lefutnak!

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
   hogy minel kisebb es kevesebb parametert kapjanak a komponensek!

3. A let függvényben dereferált atom változása a let függvényben végzett össze számítást újrakalkulálásra kényszeríti!   




# Re-Frame optimalizáció

1. A Re-Frame adatbázis minden változtatásának következménye, az összes feliratkozás
   ismételten megtörténő lefutása.

2. Az egyes feliratkozások az azokra való hivatkozások számával megegyező számban futnak
   le az egyes újra kiértékelések alkalmával.



# CSS optimalizáció

1. Az áttetszőség kiszámítása nagy számításigényű feladat (pl.: opacity, box-shadow, text-shadow, ...)
   Áttetsző tartalom ne mozogjon (pl. görgetés) rögzített tartalom felett, mert a mozgás közben
   a böngészőnek folyamatosan újra kell számítania az áttetszőséget!

2. Az elemek mozgatására használj transform tulajdonságot! Soha ne a pozíciót változtasd!

3. Próbálj minél specifikusabb szelektort használni!
   A szelektorok teljesítményigény szerinti csökkenő sorrendben: * div .class #id
