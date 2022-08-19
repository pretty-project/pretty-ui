
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-structure-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :on-click (metamorphic-event)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :data-clickable (boolean)
  ;   :style (map)}
  [lister-id item-dex {:keys [class on-click style]}]
  (cond-> {:data-clickable (boolean on-click)}
          class (assoc :class class)
          style (assoc :style style)))
