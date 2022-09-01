
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.switch.events
    (:require [x.app-core.api                 :as a :refer [r]]
              [x.app-elements.checkbox.events :as checkbox.events]
              [x.app-elements.input.events    :as input.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-switch!
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
(a/reg-event-db :elements.switch/init-switch! init-switch!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.switch/toggle-option! checkbox.events/toggle-option!)
