
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.effects
    (:require [mid-fruits.candy                                  :refer [param]]
              [server-plugins.view-selector.route-handler.engine :as route-handler.engine]
              [x.server-core.api                                 :as a]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ;  {:base-route (string)
  ;   :extended-route (string)
  ;   :route-title (metamorphic-content)}
  [extension-id selector-props]
  (merge {:base-route     (route-handler.engine/base-route     extension-id selector-props)
          :extended-route (route-handler.engine/extended-route extension-id selector-props)
          :route-title    (param extension-id)}
         (param selector-props)))



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
      (let [selector-props (selector-props-prototype extension-id selector-props)]
           {:dispatch-n [[:view-selector/reg-transfer-selector-props! extension-id selector-props]
                         [:view-selector/add-route!                   extension-id selector-props]
                         [:view-selector/add-extended-route!          extension-id selector-props]]})))