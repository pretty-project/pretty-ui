
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.list-item-a.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-structure-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :style (map)}
  [_ {:keys [class style]}]
  (cond-> {} class (assoc :class class)
             style (assoc :style style)))
