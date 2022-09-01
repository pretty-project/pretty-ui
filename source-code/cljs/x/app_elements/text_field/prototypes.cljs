
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.prototypes
    (:require [mid-fruits.candy                      :refer [param]]
              [x.app-core.api                        :as a]
              [x.app-elements.input.helpers          :as input.helpers]
              [x.app-elements.target-handler.helpers :as target-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:autofill? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:name (keyword)
  ;   :target-id (keyword)
  ;   :type (keyword)
  ;   :value-path (vector)}
  [field-id {:keys [autofill?] :as field-props}]
  (merge {:target-id  (target-handler.helpers/element-id->target-id field-id)
          :type       :text
          :value-path (input.helpers/default-value-path field-id)}
          ; BUG#6782 https://stackoverflow.com/questions/12374442/chrome-ignores-autocomplete-off
          ; - A Chrome böngésző ...
          ;   ... ignorálja az {:autocomplete "off"} beállítást,
          ;   ... ignorálja az {:autocomplete "new-*"} beállítást,
          ;   ... figyelembe veszi a {:name ...} értékét.
          ;
          ; - Véletlenszerű {:name ...} érték használatával az autofill nem képes megállapítani,
          ;   mi alapján ajánljon értékeket a mezőhöz.
          ;
          ; - A mező {:name ...} tulajdonságát lehetséges paraméterként is beállítani, mert a
          ;   a több helyen felhasznált mezők nem kaphatnak egyedi azonosítót, és generált
          ;   azonosítóval nem műkdödik az autofill!
         {:name (if autofill? field-id (a/id))}
         (param field-props)))
;

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
