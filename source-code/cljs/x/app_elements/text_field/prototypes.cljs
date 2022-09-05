
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
  ;   :set-value-f (function)
  ;   :type (keyword)
  ;   :value-path (vector)}
  [field-id field-props]
  (merge {:field-content-f return
          :set-value-f     return
          :type            :text
          :value-path      (input.helpers/default-value-path field-id)
          ; BUG#6782
          ; https://stackoverflow.com/questions/12374442/chrome-ignores-autocomplete-off
          ;
          ; - A Chrome böngésző ...
          ;   ... ignorálja az {:autocomplete "off"} beállítást,
          ;   ... ignorálja az {:autocomplete "new-*"} beállítást,
          ;   ... figyelembe veszi a {:name ...} értékét.
          ;
          ; - Generált {:autofill-name ...} érték használatával a böngésző autofill funkciója
          ;   nem képes megállapítani, mi alapján ajánljon értékeket a mezőhöz, ezért ha nem
          ;   adsz meg értelmes (pl. :phone-number) értéket az :autofill-name tulajdonságnak,
          ;   akkor az egy véletlenszerűen generált értéket fog kapni, amihez az autofill
          ;   funkció nem fog ajánlani értékeket.
          :autofill-name (a/id)}
         (param field-props)))

(defn end-adornments-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:end-adornments (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  [field-id {:keys [end-adornments] :as field-props}]
  (let [field-empty?          (text-field.helpers/field-empty? field-id)
        empty-field-adornment {:disabled? field-empty?
                               :icon      :close
                               :on-click  [:elements.text-field/empty-field! field-id field-props]
                               :tooltip   :empty-field!}]
       (vector/conj-item end-adornments empty-field-adornment)))

(defn hack3031
  [field-id {:keys [emptiable?] :as field-props}]
  ; HACK#3031
  (merge {}
         (param field-props)
         (if emptiable? {:end-adornments (end-adornments-prototype field-id field-props)})))

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
