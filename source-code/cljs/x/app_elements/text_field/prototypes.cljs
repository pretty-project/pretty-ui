
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.prototypes
    (:require [mid-fruits.candy                  :refer [param return]]
              [mid-fruits.vector                 :as vector]
              [x.app-core.api                    :as a]
              [x.app-elements.input.helpers      :as input.helpers]
              [x.app-elements.text-field.helpers :as text-field.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:icon-family (keyword)
  ;   :tab-indexed? (boolean)}
  [{:keys [icon] :as adornment-props}]
  (merge (if icon {:icon-family :material-icons-filled})
         {:tab-indexed? true}
         (param adornment-props)))

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  ;  {:autofill-name (keyword)
  ;   :field-content-f (function)
  ;   :field-value-f (function)
  ;   :type (keyword)
  ;   :value-path (vector)}
  [field-id field-props]
  ; BUG#6782
  ; https://stackoverflow.com/questions/12374442/chrome-ignores-autocomplete-off
  ;
  ; A Chrome böngésző ...
  ; ... ignorálja az {:autocomplete "off"} beállítást,
  ; ... ignorálja az {:autocomplete "new-*"} beállítást,
  ; ... figyelembe veszi a {:name ...} értékét.
  ;
  ; Generált {:autofill-name ...} érték használatával a böngésző autofill funkciója
  ; nem képes megállapítani, mi alapján ajánljon értékeket a mezőhöz, ezért ha nem
  ; adsz meg értelmes (pl. :phone-number) értéket az :autofill-name tulajdonságnak,
  ; akkor az egy véletlenszerűen generált értéket fog kapni, amihez az autofill
  ; funkció nem fog ajánlani értékeket.
  (merge  {:autofill-name   (a/id)
           :field-content-f return
           :field-value-f   return
           :type            :text
           :value-path      (input.helpers/default-value-path field-id)}
          (param field-props)))

(defn end-adornments-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:emptiable? (boolean)(opt)
  ;   :end-adornments (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  [field-id {:keys [emptiable? end-adornments] :as field-props}]
  ; HACK#3031
  ; Ha a prototípus függvényben kerül beállításra bármilyen Re-Frame esemény, ami paraméterként
  ; kapja meg field-props térképet, akkor a prototípus függvényt két függvényre szükséges
  ; felosztani, úgy, hogy az első függvényben beállításra kerüljenek a field-props térkép ú
  ; tulajdonságainak alapértelmezett értékei és a második függvényben pedig beállításra kerülhetnek
  ; a Re-Frame események, amik már úgy kapják meg paraméterként a field-props térképet, hogy abban
  ; beállításra kerültek az alapértelmezett értékek.
  ;
  ; Ha a field-props térképet az elem React-fába csatolódásakor a Re-Frame adatbázisba írná,
  ; ahonnan a Re-Frame események az elem azonosítójának használatával kiolvashatnák mindig az aktuális,
  ; állapotában, akkor nem lenne szükség erre a megoldásra.
  ; Viszont ha az elemek a Re-Frame adatbázisba írnák tulajdonságaikat tartalmazó térképeket,
  ; akkor az túl sok Re-Frame írással járna, aminek túl nagy lenne a kapacitás-igénye.
  ;
  ; Pl. Az end-adornments vektorba kerülő empty-field-adornment-props térképben az on-click esemény
  ;     számára átadott field-props térképnek mindenképpen tartalmaznia kell a value-path tulajdonságot!
  ;     Azért van két lépésre felosztva a field-props-prototype függvény, hogy ha a prototípusban kerül
  ;     beállításra az alapértelmezett value-path érték, akkor a második függvényben (end-adornments-prototype)
  ;     a field-props térkép már tartalmazni fogja a value-path tulajdonságot.
  (if emptiable? ; If the field is emptiable ...
                 (let [empty-field-adornment-props (text-field.helpers/empty-field-adornment-props field-id field-props)
                       end-adornments              (vector/conj-item end-adornments empty-field-adornment-props)]
                      (assoc field-props :end-adornments end-adornments))
                 ; If the field is NOT emptiable ...
                 (return field-props)))
