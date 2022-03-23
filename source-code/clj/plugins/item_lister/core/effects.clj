
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:collection-name (string)
  ;   :handler-key (keyword)
  ;   :on-route (metamorphic-event)(opt)
  ;   :route-template (string)(opt)
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:item-lister/init-lister! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace {:keys [route-template] :as lister-props}]]
      (let [lister-props (core.prototypes/lister-props-prototype extension-id item-namespace lister-props)]
           {:db (r core.events/init-lister! db extension-id item-namespace lister-props)
            :dispatch-n [[:item-lister/reg-transfer-lister-props! extension-id item-namespace lister-props]
                         (if route-template [:item-lister/add-base-route! extension-id item-namespace lister-props])]})))
