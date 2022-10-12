
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.events
    (:require [mid-fruits.vector                        :as vector]
              [re-frame.api                             :as r]
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
(r/reg-event-db :elements/toggle-color-selector-option! toggle-color-selector-option!)
