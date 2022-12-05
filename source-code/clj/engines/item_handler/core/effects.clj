
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.core.effects
    (:require [engines.item-handler.core.events     :as core.events]
              [engines.item-handler.core.prototypes :as core.prototypes]
              [re-frame.api                         :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-handler/init-handler!
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ; {:base-route (string)(opt)
  ;  :collection-name (string)
  ;  :handler-key (keyword)
  ;  :item-namespace (keyword)
  ;  :on-route (metamorphic-event)(opt)
  ;  :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [:item-handler/init-handler! :my-handler {...}]
  (fn [{:keys [db]} [_ handler-id {:keys [base-route] :as handler-props}]]
      (let [handler-props (core.prototypes/handler-props-prototype handler-id handler-props)]
           {:db         (r core.events/init-handler! db handler-id handler-props)
            :dispatch-n [[:item-handler/reg-transfer-handler-props! handler-id handler-props]
                         (if base-route [:item-handler/add-extended-route! handler-id handler-props])
                         (if base-route [:item-handler/add-creator-route!  handler-id handler-props])]})))
