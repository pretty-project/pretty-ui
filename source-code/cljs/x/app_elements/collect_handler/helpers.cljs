
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.collect-handler.helpers
    (:require [mid-fruits.vector             :as vector]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]
              [x.app-environment.api         :as environment]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name collectable
;  Olyan input, amelynek a value-path Re-Frame adatbázis útvonalon tárolt értéke
;  egy vektor, amelyben a kiválasztható opciók értékei gyűjthetők.
;  Az egyes opciók kiválasztásakor azokból a {:get-value-f ...} függvénnyel
;  vonja ki azok értékekét.
;  Egymással megegyező értékből egyszerre több nem tárolódik a gyűjteményben!
;
; @name collected?
;  Az input {:collected? true} tulajdonsága jelzi, hogy a value-path ... útvonalon
;  tárolt érték egy nem üres vektor.



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-collect-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (function)
  [input-id option]
  #(a/dispatch [:elements/collect-option! input-id option]))

(defn on-uncollect-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (function)
  [input-id option]
  #(a/dispatch [:elements/uncollect-option! input-id option]))

(defn on-toggle-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (function)
  [input-id option]
  #(a/dispatch [:elements/toggle-option-collection! input-id option]))

(defn input-props->option-collected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-props
  ;  {:collected-value (vector)
  ;   :get-value-f (function)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [{:keys [collected-value get-value-f]} option]
  (if get-value-f (let [value (get-value-f option)]
                       (vector/contains-item? collected-value value))
                  (vector/contains-item? collected-value option)))

(defn collectable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (map)
  [input-id {:keys [] :as input-props}]
  (element/element-attributes input-id input-props))

(defn collectable-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ;  {:data-collected (boolean)
  ;   :disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)}
  [input-id {:keys [disabled?] :as input-props} option]
  (if disabled? {:disabled true}
                {:data-collected (input-props->option-collected? input-props option)
                 :on-click       (on-toggle-function             input-id option)
                 :on-mouse-up   #(environment/blur-element!)}))
