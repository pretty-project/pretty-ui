
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.effects
    (:require [plugins.item-lister.core.prototypes :as core.prototypes]
              [x.server-core.api                   :as a]))



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
      (let [lister-props (core.prototypes/lister-props-prototype extension-id item-namespace lister-props)]
           {:dispatch-n [[:item-lister/reg-transfer-lister-props! extension-id item-namespace lister-props]
                         [:item-lister/add-route!                 extension-id item-namespace lister-props]]})))
