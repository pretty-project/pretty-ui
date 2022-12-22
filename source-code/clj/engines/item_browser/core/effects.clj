
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.core.effects
    (:require [engines.item-browser.core.events     :as core.events]
              [engines.item-browser.core.prototypes :as core.prototypes]
              [re-frame.api                         :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/init-browser!
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ; {:collection-name (string)
  ;  :handler-key (keyword)}
  ;
  ; @usage
  ; [:item-browser/init-browser! :my-browser {...}]
  (fn [{:keys [db]} [_ browser-id {:keys [base-route] :as browser-props}]]
      (let [browser-props (core.prototypes/browser-props-prototype browser-id browser-props)]
           {:db       (r core.events/init-browser! db browser-id browser-props)
            :dispatch [:item-browser/reg-transfer-browser-props! browser-id browser-props]})))
