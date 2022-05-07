
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.events
    (:require [mid-fruits.vector                        :as vector]
              [x.app-core.api                           :as a :refer [r]]
              [x.app-elements.color-selector.prototypes :as color-selector.prototypes]
              [x.app-elements.color-selector.views      :as color-selector.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-color-selector-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:value-path (vector)}
  ; @param (string) option
  [db [_ _ {:keys [value-path]} option]]
  (update-in db value-path vector/toggle-item option))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/toggle-color-selector-option! toggle-color-selector-option!)
