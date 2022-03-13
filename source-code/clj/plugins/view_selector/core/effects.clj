
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.effects
    (:require [plugins.view-selector.core.prototypes :as core.prototypes]
              [x.server-core.api                     :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/init-selector!
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:on-load (metamorphic-event)
  ;   :route-template (string)
  ;    Az útvonalnak az ".../:view-id" kifejezésre kell végződnie!
  ;   :route-title (metamorphic-content)(opt)
  ;    Default: extension-id}
  ;
  ; @usage
  ;  [:view-selector/init-selector! :my-extension {...}]
  (fn [_ [_ extension-id selector-props]]
      (let [selector-props (core.prototypes/selector-props-prototype extension-id selector-props)]
           {:dispatch-n [[:view-selector/reg-transfer-selector-props! extension-id selector-props]
                         [:view-selector/add-route!                   extension-id selector-props]
                         [:view-selector/add-extended-route!          extension-id selector-props]]})))
