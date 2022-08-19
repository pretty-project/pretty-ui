
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.content-handler.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (keyword) content-id
  ;
  ; @return (map)
  [db [_ surface-id content-id]]
  (assoc-in db [:elements :content-handler/data-items surface-id :selected-content-id] content-id))

(defn- remove-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  [db [_ surface-id]]
  (dissoc-in db [:elements :content-handler/data-items surface-id :selected-content-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/set-content! set-content!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/remove-content! remove-content!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/empty-surface! remove-content!)
