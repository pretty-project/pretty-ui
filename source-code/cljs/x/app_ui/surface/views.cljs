
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.views
    (:require [x.app-components.api     :as components]
              [x.app-core.api           :as a]
              [x.app-ui.renderer        :rename {component renderer}]
              [x.app-ui.surface.helpers :as surface.helpers]))



;; -- Surface layout components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [surface-id]
  (let [view @(a/subscribe [:ui/get-surface-prop surface-id :view])]
       [:div.x-app-surface--element--view [components/content surface-id view]]))

(defn surface-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [surface-id surface-props]
  [:div (surface.helpers/surface-attributes surface-id surface-props)
        [surface-view                       surface-id]])



;; -- Renderer components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :surface {:element               #'surface-element
                      :max-elements-rendered 1
                      :queue-behavior        :push
                      :required?             true
                      :rerender-same?        false}])
