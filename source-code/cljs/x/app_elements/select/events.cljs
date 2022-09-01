
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.events
    (:require [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-select!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  [db [_ select-id select-props]]
  (as-> db % (r engine/use-input-initial-value!   % select-id select-props)
             (r engine/use-input-initial-options! % select-id select-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ select-id select-props option]]
  (as-> db % (r engine/mark-input-as-visited! % select-id)
             (r engine/select-option!         % select-id select-props option)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  [db [_ select-id select-props]]
  (r engine/clear-input-value! db select-id select-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; @param (string) option-value
  ;
  ; @return (map)
  [db [_ select-id select-props option-value]]
  (r engine/add-option! db select-id select-props option-value))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.select/init-select! init-select!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.select/clear-value! clear-value!)
