
(ns components.color-picker.views
    (:require [components.color-picker.attributes :as color-picker.attributes]
              [components.color-picker.prototypes :as color-picker.prototypes]
              [components.component.views         :as component.views]
              [elements.api                       :as elements]
              [random.api                         :as random]
              [re-frame.api                       :as r]
              [x.components.api                   :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-picker-value
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {:disabled? (boolean)(opt)
  ;  :value-path (vector)}
  [picker-id {:keys [value-path] :as picker-props}]
  (let [picked-colors @(r/subscribe [:get-item value-path])]
       (letfn [(f [picked-colors color]
                  (conj picked-colors [:div (color-picker.attributes/picked-color-attributes picker-id picker-props color)]))]
              (reduce f [:<>] picked-colors))))

(defn color-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {:placeholder (metamorphic-content)
  ;  :value-path (vector)}
  [picker-id {:keys [placeholder value-path] :as picker-props}]
  [:div (color-picker.attributes/picker-attributes picker-id picker-props)
        ; Color picker label
        [component.views/component-label picker-id picker-props]
        ; Color picker body
        [:div (color-picker.attributes/picker-body-attributes picker-id picker-props)
              ; Checks whether any color picked ...
              (let [picked-colors @(r/subscribe [:get-item value-path])]
                   (if (empty? picked-colors)
                       ; If no color picked, displays a placeholder
                       [:div (color-picker.attributes/placeholder-attributes picker-id picker-props)
                             (x.components/content picker-id placeholder)
                             [:i {:data-icon-family :material-symbols-outlined :data-icon-size :m} :palette]]
                       ; If any color picked, displays the picked colors
                       [color-picker-value picker-id picker-props]))]])

(defn component
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;   :opacity
  ;   Default: :opacity
  ;  :color-stamp (map)(opt)
  ;   {:border-radius (map)(opt)
  ;    :gap (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :auto
  ;    :height (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;     Default: :l
  ;    :width (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;     Default: :l}
  ;  :disabled? (boolean)(opt)
  ;  :hover-effect (keyword)(opt)
  ;   :opacity
  ;  :indent (map)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :outdent (map)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: :choose-color!
  ;  :style (map)(opt)
  ;  :value-path (vector)}
  ;
  ; @usage
  ; [color-picker {...}]
  ;
  ; @usage
  ; [color-picker :my-color-picker {...}]
  ([picker-props]
   [component (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (color-picker.prototypes/picker-props-prototype picker-props)]
        [color-picker picker-id picker-props])))
