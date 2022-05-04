
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.views
    (:require [reagent.api              :as reagent]
              [x.app-components.api     :as components]
              [x.app-core.api           :as a]
              [x.app-ui.renderer        :rename {component renderer}]
              [x.app-ui.surface.helpers :as surface.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [surface-id]
  (let [content @(a/subscribe [:ui/get-surface-prop surface-id :content])]
       [:div.x-app-surface--element--content [components/content surface-id content]]))

(defn surface-element-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [surface-id]
  [:div (surface.helpers/surface-attributes surface-id)
        [surface-content                    surface-id]])

(defn surface-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  [surface-id]
  (let [on-surface-closed   @(a/subscribe [:ui/get-surface-prop surface-id :on-surface-closed])
        on-surface-rendered @(a/subscribe [:ui/get-surface-prop surface-id :on-surface-rendered])]
       (reagent/lifecycles surface-id
                           {:reagent-render         (fn [] [surface-element-structure surface-id])
                            :component-will-unmount (fn [] (a/dispatch on-surface-closed))
                            :component-did-mount    (fn [] (a/dispatch on-surface-rendered))})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :surface {:element               #'surface-element
                      :max-elements-rendered 1
                      :queue-behavior        :push
                      :required?             true
                      :rerender-same?        false}])
