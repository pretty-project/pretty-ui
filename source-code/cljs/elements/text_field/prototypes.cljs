
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text-field.prototypes
    (:require [candy.api                   :refer [param return]]
              [elements.input.helpers      :as input.helpers]
              [elements.text-field.helpers :as text-field.helpers]
              [random.api                  :as random]
              [vector.api                  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :icon-family (keyword)
  ;  :tab-indexed? (boolean)}
  [{:keys [icon] :as adornment-props}]
  (merge (if icon {:icon-family :material-icons-filled})
         {:color        :default
          :tab-indexed? true}
         (param adornment-props)))

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  ;
  ; @return (map)
  ; {:autofill-name (keyword)
  ;  :border-radius (keyword)
  ;  :field-content-f (function)
  ;  :field-value-f (function)
  ;  :type (keyword)
  ;  :value-path (vector)}
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
  (merge {:autofill-name   (random/generate-keyword)
          :border-radius   :s
          :field-content-f return
          :field-value-f   return
          :font-size       :s
          :type            :text
          :value-path      (input.helpers/default-value-path field-id)}
         (param field-props)))

(defn end-adornments-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  [field-id {:keys [emptiable? end-adornments] :as field-props}]
  ; Az empty-field-adornment-props térkép értéke megváltozik, ha a szövegmező
  ; értéke üresről nem üres stringre vált, mert a az empty-field-adornment gomb
  ; {:disabled? true} állapotban van, amíg a szövegmező értéke üres.
  ;
  ; Az a komponens és leszármazottai, amelyikben az end-adornments-prototype függvény
  ; meghívásra kerül, minden esetben újrarenderelődik, amikor a szövegmező értéke
  ; üres és nem üres string között változik.
  (if emptiable? (let [empty-field-adornment-props (text-field.helpers/empty-field-adornment-props field-id field-props)]
                      (vector/conj-item end-adornments empty-field-adornment-props))
                 (return end-adornments)))
