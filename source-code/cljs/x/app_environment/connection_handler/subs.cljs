
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.connection-handler.subs
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-online?
  ; @usage
  ;  (r environment/browser-online? db)
  ;
  ; @return (boolean)
  [db _]
  (let [browser-online? (get-in db [:environment :connection-handler/meta-items :browser-online?])]
       (boolean browser-online?)))

(defn browser-offline?
  ; @usage
  ;  (r environment/browser-offline? db)
  ;
  ; @return (boolean)
  [db _]
  (let [browser-online? (get-in db [:environment :connection-handler/meta-items :browser-online?])]
       (not browser-online?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/browser-online?]
(r/reg-sub :environment/browser-online? browser-online?)

; @usage
;  [:environment/browser-offline?]
(r/reg-sub :environment/browser-offline? browser-offline?)
