
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.views
    (:require [mid-fruits.candy            :refer [param]]
              [x.app-components.api        :as components]
              [x.app-core.api              :as a]
              [x.app-elements.api          :as elements]
              [x.app-ui.renderer           :rename {component renderer}]
              [x.app-ui.bubbles.config     :as bubbles.config]
              [x.app-ui.bubbles.helpers    :as bubbles.helpers]
              [x.app-ui.bubbles.prototypes :as bubbles.prototypes]))



;; -- Preset components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn state-changed-bubble-body
  ; @param (keyword) bubble-id
  ; @param (map) body-props
  ;  {:label (metamorphic-content)(opt)
  ;   :primary-button (map)(opt)
  ;    {:label (metamorphic-content)
  ;     :on-click (metamorphic-event)}}
  ;
  ; @usage
  ;  [ui/state-changed-bubble-body :my-bubble {...}]
  [bubble-id {:keys [label primary-button]}]
  [:<> (if label          [elements/label  {:content label}])
       (if primary-button [elements/button (bubbles.prototypes/primary-button-props-prototype bubble-id primary-button)])])



;; -- Bubble components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-close-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  (if-let [user-close? @(a/subscribe [:ui/get-bubble-prop bubble-id :user-close?])]
          [elements/icon-button {:on-click [:ui/pop-bubble! bubble-id]
                                 :preset   :close}]))

(defn bubble-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  (let [body @(a/subscribe [:ui/get-bubble-prop bubble-id :body])]
       [:div.x-app-bubbles--element--body [components/content bubble-id body]]))

(defn bubble-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id bubble-props]
  [:div (bubbles.helpers/bubble-attributes bubble-id bubble-props)
        [bubble-body                       bubble-id]
        [bubble-close-button               bubble-id]])



;; -- Renderer components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :bubbles {:element               #'bubble-element
                      :max-elements-rendered bubbles.config/MAX-BUBBLES-RENDERED
                      :queue-behavior        :wait
                      :rerender-same?        false}])
