
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.views
    (:require [mid-fruits.candy        :refer [param]]
              [x.app-components.api    :as components]
              [x.app-elements.api      :as elements]
              [x.app-ui.renderer       :rename {component renderer}]
              [x.app-ui.bubbles.engine :as bubbles.engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn primary-button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) button-props
  ;  {:on-click (metamorphic-event)}
  ;
  ; @return (map)
  ;  {:on-click (metamorphic-event)
  ;   :preset (keyword)}
  [bubble-id {:keys [on-click] :as button-props}]
  (merge {:preset :primary-button}
         (param button-props)
         {:on-click {:dispatch-n [on-click [:ui/pop-bubble! bubble-id]]}}))



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
       (if primary-button [elements/button (primary-button-props-prototype bubble-id primary-button)])])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-close-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:user-close? (boolean)(opt)}
  [bubble-id {:keys [user-close?]}]
  (if user-close? [elements/button {:on-click [:ui/pop-bubble! bubble-id]
                                    :preset   :close-icon-button}]))

(defn bubble-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:body (map)}
  [bubble-id {:keys [body]}]
  [:div.x-app-bubbles--element--body [components/content bubble-id body]])

(defn bubble-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  [bubble-id bubble-props]
  [:div (bubbles.engine/bubble-attributes bubble-id bubble-props)
        [bubble-body                      bubble-id bubble-props]
        [bubble-close-button              bubble-id bubble-props]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [renderer :bubbles {:element               #'bubble-element
                      :max-elements-rendered bubbles.engine/MAX-BUBBLES-RENDERED
                      :queue-behavior        :wait
                      :rerender-same?        false}])
