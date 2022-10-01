
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.events     :as core.events]
              [plugins.item-lister.core.prototypes :as core.prototypes]
              [re-frame.api                        :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :item-lister/init-lister!
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  ;  {:base-route (string)(opt)
  ;   :collection-name (string)
  ;   :handler-key (keyword)
  ;   :item-namespace (keyword)
  ;   :on-route (metamorphic-event)(opt)
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-lister/init-lister! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id {:keys [base-route] :as lister-props}]]
      (let [lister-props (core.prototypes/lister-props-prototype lister-id lister-props)]
           {:db         (r core.events/init-lister! db lister-id lister-props)
            :dispatch-n [[:item-lister/reg-transfer-lister-props! lister-id lister-props]
                         (if base-route [:item-lister/add-base-route! lister-id lister-props])]})))
