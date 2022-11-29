
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.event-browser.effects
    (:require [re-frame.api :as r]
              [reader.api   :as reader]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer-tools.event-browser/dispatch-current!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [event-id   (get-in db [:developer-tools :event-browser/meta-items :current-event])
            parameters (get-in db [:developer-tools :event-browser/meta-items :parameters])]
           (reader/string->mixed (str "["event-id" "parameters"]")))))
