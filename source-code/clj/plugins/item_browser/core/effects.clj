
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.effects
    (:require [plugins.item-browser.core.events     :as core.events]
              [plugins.item-browser.core.prototypes :as core.prototypes]
              [x.server-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/init-browser!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:collection-name (string)
  ;   :handler-key (keyword)
  ;   :on-route (metamorphic-event)
  ;   :route-template (string)
  ;    Az útvonalnak az ".../:item-id" kifejezésre kell végződnie!
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-browser/init-browser! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace browser-props]]
      (let [browser-props (core.prototypes/browser-props-prototype extension-id item-namespace browser-props)]
           {:db (r core.events/init-browser! db extension-id item-namespace browser-props)
            :dispatch-n [[:item-browser/reg-transfer-browser-props! extension-id item-namespace browser-props]
                         [:item-browser/add-base-route!             extension-id item-namespace browser-props]
                         [:item-browser/add-extended-route!         extension-id item-namespace browser-props]]})))
