
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.event-browser.effects
    (:require [mid-fruits.reader :as reader]
              [x.app-core.api    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/dispatch-current-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [event-id   (get-in db [:developer :event-browser/meta-items :current-event])
            parameters (get-in db [:developer :event-browser/meta-items :parameters])]
           (reader/string->mixed (str "["event-id" "parameters"]")))))
