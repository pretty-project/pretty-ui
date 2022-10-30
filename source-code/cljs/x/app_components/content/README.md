
# metamorphic-content
  A (metamorphic-content) típust a content komponens valósítja meg.
  - Értéke lehet szimbólumként átadott komponens:
    Pl. #'my-component
  - Értéke lehet React komponensként átadott komponens:
    Pl. [:<> [my-component] ...]
  - Értéke lehet az app-dictionary szótár egy kifejezésére utaló kulcsszó:
    Pl. :my-term
  - Értéke lehet egy egyszerű string:
    Pl. "My content"
  - Értéke lehet egy hiccup vektor:
    Pl. [:div "My content"]



# base-props
  A content komponensnek {:content ...} tulajdonságként átadott komponens számára utolsó paraméterként
  átadott térkép alapja (az XXX#0001 logika szerint).



# prefix, suffix
  Ha a content komponensnek {:content ...} tulajdonságként az app-dictionary szótár egy kifejezésre
  utaló kulcszó vagy szöveg kerül átadásra, akkor a {:prefix ...} vagy {:suffix ...} tulajdonságként
  átadott string típusú tartalmat prefixumként vagy toldalékaként használja.



# replacements
# XXX#4509
  A content komponensnek {:replacements [...]} tulajdonságként vektorban átadott string típusok,
  a {:content ...} tulajdonságként átadott tartalom jelőlői ("%", "%1", "%2", ...) helyett
  kerülnek behelyettesítésre.



# subscriber
  A feliratkozás visszatérési értékének típusú térkép kell legyen!
