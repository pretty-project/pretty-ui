
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.helpers
    (:require [x.app-core.api   :as a]
              [x.app-ui.element :as element]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ;  {:data-minimized (boolean)
  ;   :data-stretch-orientation (boolean)}
  [popup-id popup-props]
  (merge (element/element-attributes :popups popup-id popup-props {:data-nosnippet true})
         (if-let [stretch-orientation @(a/subscribe [:ui/get-popup-prop popup-id :stretch-orientation])]
                 {:data-stretch-orientation stretch-orientation})
         (if-let [minimized? @(a/subscribe [:ui/get-popup-prop popup-id :minimized?])]
                 {:data-minimized minimized?})))

(defn popup-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [popup-id]
  {})
