
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.engine
    (:require [x.app-ui.element :as element]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:minimized? (boolean)(opt)
  ;   :stretch-orientation (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-minimized (boolean)
  ;   :data-stretched (boolean)}
  [popup-id {:keys [minimized? stretch-orientation] :as popup-props}]
  (cond-> (element/element-attributes :popups popup-id popup-props {:data-nosnippet true})
          stretch-orientation (assoc :data-stretch-orientation stretch-orientation)
          minimized?          (assoc :data-minimized           minimized?)))

(defn popup-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:autopadding? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-autopadding (boolean)}
  [_ {:keys [autopadding?]}]
  (merge {} (if autopadding? {:data-autopadding true})))
