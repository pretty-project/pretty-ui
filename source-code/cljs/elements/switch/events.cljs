
(ns elements.switch.events
    (:require [elements.checkbox.events :as checkbox.events]
              [elements.input.events    :as input.events]
              [re-frame.api             :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn switch-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  [db [_ switch-id switch-props]]
  (as-> db % (r input.events/use-initial-value!   % switch-id switch-props)
             (r input.events/use-initial-options! % switch-id switch-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.switch/switch-did-mount switch-did-mount)
