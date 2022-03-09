
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.effects
    (:require [mid-fruits.candy                   :refer [param return]]
              [server-plugins.item-browser.engine :as engine]
              [x.server-core.api                  :as a :refer [r]]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :route-template (string)
  ;   :route-title (keyword)}
  [extension-id item-namespace browser-props]
  (merge {:base-route     (engine/base-route     extension-id item-namespace browser-props)
          :route-template (engine/route-template extension-id item-namespace browser-props)
          :route-title    (param :auto)}
         (param browser-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/init-browser!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:base-route (string)
  ;   :on-load (metamorphic-event)
  ;   :route-template (string)
  ;    Az útvonalnak az ".../:item-id" kifejezésre kell végződnie!
  ;   :route-title (keyword or metamorphic-content)(opt) :auto}
  ;
  ; @usage
  ;  [:item-browser/init-browser! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace browser-props]]
      (let [browser-props (browser-props-prototype extension-id item-namespace browser-props)]
           {:dispatch-n [[:item-browser/reg-transfer-browser-props! extension-id item-namespace browser-props]
                         [:item-browser/add-route!                  extension-id item-namespace browser-props]
                         [:item-browser/add-extended-route!         extension-id item-namespace browser-props]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/reg-transfer-browser-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  (fn [_ [_ extension-id item-namespace browser-props]]
      {:fx [:core/reg-transfer! (engine/transfer-id extension-id item-namespace)
                                {:data-f      (fn [_] (return browser-props))
                                 :target-path [extension-id :item-browser/meta-items]}]}))

(a/reg-event-fx
  :item-browser/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:base-route (string)}
  (fn [_ [_ extension-id item-namespace {:keys [base-route]}]]
      [:router/add-route! (engine/route-id extension-id item-namespace)
                          {:client-event   [:item-browser/load-browser! extension-id item-namespace]
                           :route-template base-route
                           :restricted?    true}]))

(a/reg-event-fx
  :item-browser/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:route-template (string)}
  (fn [_ [_ extension-id item-namespace {:keys [route-template]}]]
      [:router/add-route! (engine/extended-route-id extension-id item-namespace)
                          {:client-event   [:item-browser/load-browser! extension-id item-namespace]
                           :route-template route-template
                           :restricted?    true}]))
