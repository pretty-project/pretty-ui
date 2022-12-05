
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
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
  ; {:base-route (string)(opt)
  ;  :collection-name (string)
  ;  :handler-key (keyword)
  ;  :on-route (metamorphic-event)
  ;  :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [:item-browser/init-browser! :my-browser {...}]
  (fn [{:keys [db]} [_ browser-id {:keys [base-route] :as browser-props}]]
      (let [browser-props (core.prototypes/browser-props-prototype browser-id browser-props)]
           {:db         (r core.events/init-browser! db browser-id browser-props)
            :dispatch-n [[:item-browser/reg-transfer-browser-props! browser-id browser-props]
                         (if base-route [:item-browser/add-base-route!     browser-id browser-props])
                         (if base-route [:item-browser/add-extended-route! browser-id browser-props])]})))
