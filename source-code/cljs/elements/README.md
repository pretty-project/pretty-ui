
### ...

###### BUG#4044

Ha egy listában a listaelemek toggle elemet tartalmaznak és ...
... a toggle elem nem kap egyedi azonosítót, különben ugyanaz az azonosító
    ismétlődne az összes listaelem toggle elemében,
... a toggle elem {:hover-color ...} tulajdonsággal rendelkezik,
... az elemek React kulcsként megkapják az azonosítójukat,
... az egyes listaelemekre kattintva olyan változás történik (pl. kijelölés),
    ami miatt az adott listaelem paraméterezése megváltozik,
akkor az egyes listaelemekre kattintva ...
... a megváltozó paraméterek miatt a listaelem újrarenderelődik,
... a listaelem toggle eleme is újrarenderelődik, ami miatt új azonosítót kap,
... a toggle elem az új azonosítója miatt új React kulcsot kap,
... a toggle elem az új React kulcs beállításának pillanatában másik React-elemmé
    változik és a váltás közben Ca. 15ms ideig nem látszódik a {:hover-color ...}
    tulajdonság színe (rövid villanásnak tűnik)

### ...

###### XXX#4004

Az x4.7.6 verzióig egyetlen esetben sem volt rá szükség, hogy egy elem rendelkezzen
DOM azonosítóval.

+ Talán könnyebb a böngészőnek, ha kevesebb az azonosítóval rendelkező elem ...
