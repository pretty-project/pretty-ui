
# value
  A (metamorphic-value) típust a get-metamorphic-value subscription
  {:value ...} tulajdonsága valósítja meg.
  - Értéke lehet az app-dictionary szótár egy kifejezésére utaló kulcsszó
    Pl. {:value :my-term}
  - Értéke lehet egy egyszerű string
    Pl. {:value "My value"}



# prefix, suffix
# XXX#4510
  A prefix vagy suffix tulajdonságként átadott string típusú tartalmat prefixumként
  vagy toldalékaként használja.



# replacements
# XXX#4509 (mid-fruits.string)
  A replacements tulajdonságként átadott vektorban felsorolt szám vagy string típusok,
  a value tulajdonságként átadott tartalom jelölői ("%", "%1", "%2", ...) helyett
  kerülnek behelyettesítésre.
