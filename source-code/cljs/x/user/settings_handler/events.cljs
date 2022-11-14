
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.settings-handler.events
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-user-settings-item!
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @usage
  ;  (r set-user-settings-item! db :my-settings-item "My value")
  ;
  ; @return (map)
  [db [_ item-key item-value]]
  (assoc-in db [:x.user :settings-handler/user-settings item-key] item-value))

(defn set-user-settings!
  ; @param (map) user-settings
  ;
  ; @usage
  ;  (r set-user-settings! db {:my-settings-item "My value"})
  ;
  ; @return (map)
  [db [_ user-settings]]
  (update-in db [:x.user :settings-handler/user-settings] merge user-settings))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.user/set-user-settings-item! :my-settings-item "My value"]
(r/reg-event-db :x.user/set-user-settings-item! set-user-settings-item!)

; @usage
;  [:x.user/set-user-settings! {:my-settings-item "My value"}]
(r/reg-event-db :x.user/set-user-settings! set-user-settings!)
