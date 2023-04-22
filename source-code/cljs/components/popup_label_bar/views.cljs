
(ns components.popup-label-bar.views
    (:require [components.popup-label-bar.prototypes :as popup-label-bar.prototypes]
              [elements.api                          :as elements]
              [random.api                            :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-label-bar
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:label (map)(opt)
  ;  :primary-button (map)(opt)
  ;  :secondary-button (map)(opt)}
  [_ {:keys [label primary-button secondary-button]}]
  ; If you want to primary-button on-click event to be fired when the ENTER key
  ; is pressed even if a text field is focused and the keypress handler is in
  ; type mode, then set the on-click event as the on-enter event of the field as well.
  [:div {:class :c-popup-label-bar}
        [:div {:class :c-popup-label-bar--body}
              (if secondary-button [elements/button secondary-button]
                                   [:div {:class :c-popup-label-bar--placeholder}])
              (if label            [elements/label label]
                                   [:div {:class :c-popup-label-bar--placeholder}])
              (if primary-button   [elements/button primary-button]
                                   [:div {:class :c-popup-label-bar--placeholder}])]])

(defn component
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ; {:label (map)(opt)
  ;   {:content (metamorphic-content)}
  ;  :primary-button (map)(opt)
  ;   {:label (metamorphic-content)
  ;    :on-click (metamorphic-event)}
  ;  :secondary-button (map)(opt)
  ;   {:label (metamorphic-content)
  ;    :on-click (metamorphic-event)}}
  ;
  ; @usage
  ; [popup-label-bar {...}]
  ;
  ; @usage
  ; [popup-label-bar :my-popup-label-bar {...}]
  ([bar-props]
   [component (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   (let [bar-props (popup-label-bar.prototypes/bar-props-prototype bar-props)]
        [popup-label-bar bar-id bar-props])))
