
# renderer
A UI renderer egy partíció data-item elemeit sorolja fel egy komponens-sorozat
komponenseinek paraméterként átadva.
A UI renderer az egyes partíciókat rendezett partícióként (ordered-partition)
hozza létre.
Az elemek felsorolása a partíció data-order vektorában rögzített
sorrend szerint történik.



# attributes
Az attributes paraméterként átadott térkép a komponens-sorozatot
körülvevő [:div] elem attribútumait tartalmazhatja.



# renderer destructor
TODO



# renderer initializer
TODO



# element destructor
TODO



# element initializer
TODO



# required?
Abban az esetben, ha a renderer {:required? true} tulajdonsággal rendelkezik,
akkor a renderer React-fából való esetleges lecsatolása rendszerhibának minősül.



# alternate-renderer
Abban az esetben ha a renderer {:required? true} tulajdonsággal rendelkezik
és a renderer {:alternate-renderer ...} tulajdonságaként megadott azonosítóval
rendelkező helyettesítő renderer, elemeket jelenít meg, akkor a renderer
React-fából való esetleges lecsatolása nem minősül rendszerhibának.
Pl. {:alternate-renderer :alternate-partition/elements}



# element-rendered?
Az elem a React-fába csatolva.



# element-visible?
Az elem a React-fába csatolva és nincs megjelölve invisible-element
tulajdonsággal.



# any-element-rendered?
A renderer partíciója tartalmaz legalább egy kirenderelt elemet.



# any-element-visible?
A renderer partíciója tartalmaz legalább egy olyan kirenderelt elemet, amely
nincs megjelölve invisible-element-ként.



# invisible-element
Az element a megszűntetésének kezdetekor invisible-element jelölést kap.
Amíg az element animált eltűnése történik, az element a renderer partíciójában
megtalálható.



# queue-behavior
{:queue-behavior :wait} Ha a partíció elemeinek száma elérte a maximálisan
kirenderelhető elemek számát, akkor az újonnan hozzáadott elemek addig
nem renderelődnek, amíg a kirenderelt elemek száma nem kezd el csökkenni.



{:queue-behavior :push} Ha a partíció elemeinek száma elérte a maximálisan
kirenderelhető elemek számát, akkor minden újabb elem hozzáadása kitörli
a legrégebben hozzáadott elemet.



{:queue-behavior :ignore} Ha a partíció elemeinek száma eléri a maximálisan
kirenderelhető elemek számát, akkor az újabb elemek hozzáadása nem történik meg.



# rerender-same?
TODO



# renderer-reserved?
TODO
