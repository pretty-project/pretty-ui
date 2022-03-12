
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.engine.effects
    (:require [mid-fruits.candy                    :refer [param]]
              [plugins.item-browser.routes.helpers :as routes.helpers]
              [x.server-core.api                   :as a]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)
  ;   :route-title (keyword)}
  [extension-id item-namespace browser-props]
  (merge {:base-route     (routes.helpers/base-route     extension-id item-namespace browser-props)
          :extended-route (routes.helpers/extended-route extension-id item-namespace browser-props)
          :route-title    (param :auto)}
         (param browser-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/init-browser!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:on-load (metamorphic-event)
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
