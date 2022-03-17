
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-a.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:disabled? (boolean)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;   :min-width (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-disabled (boolean)
  ;   :data-horizontal-align (keyword)
  ;   :data-min-width (keyword)}
  [_ {:keys [disabled? horizontal-align min-width]}]
  (cond-> {} (some? min-width)        (assoc :data-min-width        min-width)
             (some? horizontal-align) (assoc :data-horizontal-align horizontal-align)
             (some? disabled?)        (assoc :data-disabled         disabled?)))
