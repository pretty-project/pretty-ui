
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.connection-handler.subs
    (:require [x.app-core.api :as a]))


 
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
(a/reg-sub :environment/browser-online? browser-online?)

; @usage
;  [:environment/browser-offline?]
(a/reg-sub :environment/browser-offline? browser-offline?)
