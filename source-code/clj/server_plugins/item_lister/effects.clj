
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.effects
    (:require [mid-fruits.candy                  :refer [param return]]
              [server-plugins.item-lister.engine :as engine]
              [x.server-core.api                 :as a :refer [r]]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lister-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) lister-props
  ;
  ; @return (map)
  ;  {:route-template (string)
  ;   :route-title (metamorphic-content)}
  [extension-id item-namespace lister-props]
  (merge {:route-template (engine/route-template extension-id item-namespace lister-props)
          :route-title    (param                 extension-id)}
         (param lister-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/init-lister!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:on-load (metamorphic-event)
  ;   :route-template (string)
  ;   :route-title (metamorphic-content)(opt)
  ;    Default: extension-id}
  ;
  ; @usage
  ;  [:item-lister/init-lister! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace lister-props]]
      (let [lister-props (lister-props-prototype extension-id item-namespace lister-props)]
           {:dispatch-n [[:item-lister/reg-transfer-lister-props! extension-id item-namespace lister-props]
                         [:item-lister/add-route!                 extension-id item-namespace lister-props]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/reg-transfer-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  (fn [_ [_ extension-id item-namespace lister-props]]
      {:fx [:core/reg-transfer! (engine/transfer-id extension-id item-namespace)
                                {:data-f      (fn [_] (return lister-props))
                                 :target-path [extension-id :item-lister/meta-items]}]}))

(a/reg-event-fx
  :item-lister/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:route-template (string)}
  (fn [_ [_ extension-id item-namespace {:keys [route-template]}]]
      [:router/add-route! (engine/route-id extension-id item-namespace)
                          {:client-event   [:item-lister/load-lister! extension-id item-namespace]
                           :route-template route-template
                           :restricted?    true}]))
