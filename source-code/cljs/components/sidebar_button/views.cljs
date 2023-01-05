
(ns components.sidebar-button.views
    (:require [components.sidebar-button.prototypes :as sidebar-button.prototypes]
              [elements.api                         :as elements]
              [random.api                           :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-button-icon
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

(defn- sidebar-button-label
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:font-weight (keyword)(opt)
  ;  :label (metamorphic-content)}
  [_ {:keys [font-size font-weight label]}]
  [elements/label {:color       :invert
                   :content     label
                   :font-size   font-size
                   :font-weight font-weight
                   :line-height :block
                   :outdent     {:right :xl}}])

(defn- sidebar-button-body
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.c-sidebar-button--body [sidebar-button-icon  button-id button-props]
                               [sidebar-button-label button-id button-props]])

(defn- sidebar-button-toggle
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)
  ;   Default: false
  ;  :hover-color (keyword)
  ;  :on-click (metamorphic-event)
  ;  :style (map)(opt)}
  [button-id {:keys [disabled? hover-color on-click style] :as button-props}]
  [elements/toggle {:content     [sidebar-button-body button-id button-props]
                    :disabled?   disabled?
                    :on-click    on-click
                    :hover-color hover-color}])

(defn- sidebar-button
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.c-sidebar-button [sidebar-button-toggle button-id button-props]])

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
  ;   Default: :bold
  ;  :hover-color (keyword or string)(opt)
  ;   Default: :invert
  ;  :icon (keyword)
  ;  :icon-color (string or keyword)
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-icons-filled
  ;  :label (metamorphic-content)
  ;  :on-click (metamorphic-event)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [sidebar-button {...}]
  ;
  ; @usage
  ; [sidebar-button :my-sidebar-button {...}]
  ([button-props]
   [component (random/generate-keyword) button-props])

  ([button-id button-props]
   (let [button-props (sidebar-button.prototypes/button-props-prototype button-props)]
        [sidebar-button button-id button-props])))
