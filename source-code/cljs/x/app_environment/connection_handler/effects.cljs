
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.connection-handler.effects
    (:require [js-window.api :as js-window]
              [re-frame.api  :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :environment/connection-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [browser-online? (js-window/browser-online?)]
           {:db          (assoc-in db [:environment :connection-handler/meta-items :browser-online?] browser-online?)
            :dispatch-if [browser-online? [:core/connect-app!]
                                          [:core/disconnect-app!]]})))
