
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.connection-handler.effects
    (:require [re-frame.api :as r]
              [window.api   :as window]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :environment/connection-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [browser-online? (window/browser-online?)]
           {:db          (assoc-in db [:environment :connection-handler/meta-items :browser-online?] browser-online?)
            :dispatch-if [browser-online? [:core/connect-app!]
                                          [:core/disconnect-app!]]})))
