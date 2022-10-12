
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.settings-handler.subs
    (:require [mid-fruits.map :as map]
              [re-frame.api   :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-default-user-settings
  ; @usage
  ;  (r user/get-default-user-settings db)
  ;
  ; @return (namespaced map)
  [db _]
  (map/add-namespace (get-in db [:user :settings-handler/default-user-settings])
                     :user-settings))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/get-default-user-settings]
(r/reg-sub :user/get-default-user-settings get-default-user-settings)
