
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.core.effects
    (:require [engines.item-viewer.core.events     :as core.events]
              [engines.item-viewer.core.prototypes :as core.prototypes]
              [re-frame.api                        :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-viewer/init-viewer!
  ; @param (keyword) viewer-id
  ; @param (map) viewer-props
  ;  {:base-route (string)(opt)
  ;   :collection-name (string)
  ;   :handler-key (keyword)
  ;   :item-namespace (keyword)
  ;   :on-route (metamorphic-event)(opt)
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-viewer/init-viewer! :my-viewer {...}]
  (fn [{:keys [db]} [_ viewer-id {:keys [base-route] :as viewer-props}]]
      (let [viewer-props (core.prototypes/viewer-props-prototype viewer-id viewer-props)]
           {:db         (r core.events/init-viewer! db viewer-id viewer-props)
            :dispatch-n [[:item-viewer/reg-transfer-viewer-props! viewer-id viewer-props]
                         (if base-route [:item-viewer/add-extended-route! viewer-id viewer-props])]})))
