
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.effects
    (:require [plugins.view-selector.core.prototypes :as core.prototypes]
              [x.server-core.api                     :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/init-selector!
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:on-route (metamorphic-event)(opt)
  ;   :route-template (string)(opt)
  ;    Az útvonalnak az ".../:view-id" kifejezésre kell végződnie!
  ;   :route-title (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [:view-selector/init-selector! :my-selector {...}]
  (fn [_ [_ selector-id {:keys [route-template] :as selector-props}]]
      (let [selector-props (core.prototypes/selector-props-prototype selector-id selector-props)]
           {:dispatch-n [[:view-selector/reg-transfer-selector-props! selector-id selector-props]
                         (if route-template [:view-selector/add-base-route!     selector-id selector-props])
                         (if route-template [:view-selector/add-extended-route! selector-id selector-props])]})))
