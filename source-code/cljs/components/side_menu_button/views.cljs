
(ns components.side-menu-button.views
    (:require [components.component.helpers           :as component.helpers]
              [components.side-menu-button.presets    :as side-menu-button.presets]
              [components.side-menu-button.prototypes :as side-menu-button.prototypes]
              [elements.api                           :as elements]
              [random.api                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- side-menu-button-icon
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon (keyword)
  ;  :icon-color (string)
  ;  :icon-family (keyword)}
  [_ {:keys [icon icon-color icon-family]}]
  [elements/icon {:color       icon-color
                  :icon        icon
                  :icon-family icon-family
                  :outdent     {:horizontal :xs :left :s :right :xxs}
                  :size        :s}])

(defn- side-menu-button-label
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:font-weight (keyword)(opt)
  ;  :label (metamorphic-content)}
  [_ {:keys [font-size font-weight label]}]
  [elements/label {:content     label
                   :font-size   font-size
                   :font-weight font-weight
                   :line-height :block
                   :outdent     {:right :xl}}])

(defn- side-menu-button-body
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.c-side-menu-button--body [side-menu-button-icon  button-id button-props]
                                 [side-menu-button-label button-id button-props]])

(defn- side-menu-button-toggle
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)
  ;   Default: false
  ;  :hover-color (keyword)
  ;  :on-click (metamorphic-event)
  ;  :style (map)(opt)}
  [button-id {:keys [disabled? hover-color on-click style] :as button-props}]
  [elements/toggle {:content     [side-menu-button-body button-id button-props]
                    :disabled?   disabled?
                    :on-click    on-click
                    :hover-color hover-color}])

(defn- side-menu-button
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.c-side-menu-button [side-menu-button-toggle button-id button-props]])

(defn component
  ; @param (keyword)(opt) button-id
  ; @param (map) button-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :font-size (keyword)(opt)
  ;   Default: :xs
  ;  :font-weight (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;   Default: :highlight
  ;  :icon (keyword)
  ;  :icon-color (string or keyword)
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-icons-filled
  ;  :label (metamorphic-content)
  ;  :on-click (metamorphic-event)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [side-menu-button {...}]
  ;
  ; @usage
  ; [side-menu-button :my-side-menu-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (component.helpers/apply-preset side-menu-button.presets/BUTTON-PROPS-PRESETS button-props)
         button-props (side-menu-button.prototypes/button-props-prototype button-props)]
        [side-menu-button button-id button-props])))
