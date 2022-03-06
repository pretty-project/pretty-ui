
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.views
    (:require [x.app-components.api    :as components]
              [x.app-ui.renderer       :rename {component renderer}]
              [x.app-ui.surface.engine :as surface.engine]))



;; -- Surface layout components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:view (map)}
  [surface-id {:keys [view]}]
  [:div.x-app-surface--element--view [components/content surface-id view]])

(defn surface-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  [:div (surface.engine/surface-attributes surface-id surface-props)
        [surface-view                      surface-id surface-props]])



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
