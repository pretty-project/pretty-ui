
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.events     :as core.events]
              [plugins.item-lister.core.prototypes :as core.prototypes]
              [x.server-core.api                   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/init-lister!
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  ;  {:collection-name (string)
  ;   :handler-key (keyword)
  ;   :item-namespace (keyword)
  ;   :on-route (metamorphic-event)(opt)
  ;   :route-template (string)(opt)
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-lister/init-lister! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id {:keys [route-template] :as lister-props}]]
      (let [lister-props (core.prototypes/lister-props-prototype lister-id lister-props)]
           {:db (r core.events/init-lister! db lister-id lister-props)
            :dispatch-n [[:item-lister/reg-transfer-lister-props! lister-id lister-props]
                         (if route-template [:item-lister/add-base-route! lister-id lister-props])]})))
