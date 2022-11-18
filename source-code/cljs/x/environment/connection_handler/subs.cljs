
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.connection-handler.subs
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-online?
  ; @usage
  ;  (r browser-online? db)
  ;
  ; @return (boolean)
  [db _]
  (let [browser-online? (get-in db [:x.environment :connection-handler/meta-items :browser-online?])]
       (boolean browser-online?)))

(defn browser-offline?
  ; @usage
  ;  (r browser-offline? db)
  ;
  ; @return (boolean)
  [db _]
  (let [browser-online? (get-in db [:x.environment :connection-handler/meta-items :browser-online?])]
       (not browser-online?)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.environment/browser-online?]
(r/reg-sub :x.environment/browser-online? browser-online?)

; @usage
;  [:x.environment/browser-offline?]
(r/reg-sub :x.environment/browser-offline? browser-offline?)