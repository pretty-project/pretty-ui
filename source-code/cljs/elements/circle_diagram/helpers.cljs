
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.circle-diagram.helpers
    (:require [elements.element.helpers :as element.helpers]
              [mid-fruits.css           :as css]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-props<-total-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)}
  ;
  ; @example
  ;  (diagram-props<-total-value {:sections [{:value 50} {:value 25} {:value 50}]})
  ;  =>
  ;  {:sections [{:value 25 :sum 0} {:value 25 :sum 25} {:value 50 :sum 50}] :total-value 100}
  ;
  ; @return (map)
  ;  {:sections (maps in vector)
  ;    [{:sum (integer)}]
  ;   :total-value (integer)}
  [{:keys [sections] :as diagram-props}]
  ; A diagram-props térkép :sections vektorában felsorolt szekciók térképeibe írja az aktuális
  ; szekció előtti többi szekció értékeinek összegét, amelyből majd lehetséges kiszámítani
  ; egyes szekciók elforgatásának mértékét.
  (letfn [(f [{:keys [total-value] :as diagram-props} dex {:keys [value]}]
             (-> diagram-props (update   :total-value + value)
                               (assoc-in [:sections dex :sum] (or total-value 0))))]
         (reduce-kv f diagram-props sections)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn diagram-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:diameter (px)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)
  ;    {:height (string)
  ;     :width (string)}}
  [_ {:keys [diameter style]}]
  {:style (merge style {:height (css/px diameter)
                        :width  (css/px diameter)})})

(defn diagram-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [diagram-id diagram-props]
  (merge (element.helpers/element-default-attributes diagram-id diagram-props)
         (element.helpers/element-indent-attributes  diagram-id diagram-props)
         {}))
