
(ns components.popup-close-bar.views
    (:require [components.popup-close-bar.prototypes :as popup-close-bar.prototypes]
              [elements.api                          :as elements]
              [random.api                            :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-close-icon-button
  ; @param (keyword) popup-id
  ; @param (map) bar-props
  ; {:on-close (metamorphic-content)}
  [_ {:keys [on-close]}]
  [elements/icon-button {:border-radius :s
                         :hover-color   :highlight
                         :icon          :close
                         :keypress      {:key-code 27}
                         :on-click      on-close}])

(defn- popup-close-bar
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  [elements/horizontal-polarity {:end-content [popup-close-icon-button bar-id bar-props]}])

(defn component
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ; {:on-close (metamorphic-event)}
  ;
  ; @usage
  ; [popup-close-bar {...}]
  ;
  ; @usage
  ; [popup-close-bar :my-popup-close-bar {...}]
  ([bar-props]
   [component (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   (let [] ; bar-props (popup-close-bar.prototypes/bar-props-prototype bar-props)
        [popup-close-bar bar-id bar-props])))
