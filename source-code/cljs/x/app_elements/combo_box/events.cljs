
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.events
    (:require [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.input.events      :as input.events]
              [x.app-elements.text-field.events :as text-field.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-combo-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r input.events/use-initial-options! db field-id field-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn DOWN-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r text-field.events/show-surface! db field-id field-props))

(defn UP-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r text-field.events/show-surface! db field-id field-props))

(defn ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r text-field.events/hide-surface! db field-id field-props))

(defn ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r text-field.events/hide-surface! db field-id field-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.combo-box/init-combo-box! init-combo-box!)
