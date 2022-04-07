
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn list-item-structure-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :selected? (boolean)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :data-selected (boolean)
  ;   :style (map)}
  [_ {:keys [class style selected?]}]
  (cond-> {} class             (assoc :class         class)
             style             (assoc :style         style)
             (some? selected?) (assoc :data-selected selected?)))
