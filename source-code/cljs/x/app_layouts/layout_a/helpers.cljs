
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
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;   :min-width (keyword)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :data-disabled (boolean)
  ;   :data-horizontal-align (keyword)
  ;   :data-min-width (keyword)
  ;   :style (map)}
  [_ {:keys [class disabled? horizontal-align min-width style]}]
  (cond-> {} class             (assoc :class                 class)
             style             (assoc :style                 style)
             min-width         (assoc :data-min-width        min-width)
             horizontal-align  (assoc :data-horizontal-align horizontal-align)
             (some? disabled?) (assoc :data-disabled         disabled?)))
