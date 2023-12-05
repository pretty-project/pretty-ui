
(ns components.color-picker.views
    (:require [components.color-picker.attributes :as color-picker.attributes]
              [components.color-picker.prototypes :as color-picker.prototypes]
              [components.component.views         :as component.views]
              [pretty-elements.api                       :as pretty-elements]
              [metamorphic-content.api            :as metamorphic-content]
              [random.api                         :as random]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-picker-value
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {:disabled? (boolean)(opt)
  ;  :value-path (Re-Frame path vector)}
  [picker-id {:keys [value-path] :as picker-props}]
  (let [picked-colors @(r/subscribe [:get-item value-path])]
       (letfn [(f0 [picked-colors color]
                   (conj picked-colors [:div (color-picker.attributes/picked-color-attributes picker-id picker-props color)]))]
              (reduce f0 [:<>] picked-colors))))

(defn color-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {:placeholder (metamorphic-content)
  ;  :value-path (Re-Frame path vector)}
  [picker-id {:keys [placeholder value-path] :as picker-props}]
  [:div (color-picker.attributes/picker-attributes picker-id picker-props)
        ; Color picker label
        [component.views/component-label picker-id picker-props]
        ; Color picker body
        [:div (color-picker.attributes/picker-body-attributes picker-id picker-props)
              ; Checks whether any color picked ...
              (let [picked-colors @(r/subscribe [:get-item value-path])]

                   (if (empty? picked-colors)
                       ; If no color picked, it displays the placeholder.
                       [:div (color-picker.attributes/placeholder-attributes picker-id picker-props)
                             (metamorphic-content/compose placeholder)
                             [:i {:data-icon-family :material-symbols-outlined :data-icon-size :m} :palette]]
                       ; If any color picked, it displays the picked colors.
                       [color-picker-value picker-id picker-props]))]])

(defn component
  ; @info
  ; XXX#0709 (source-code/cljs/pretty_elements/color_selector/views.cljs)
  ; The 'color-picker' component is based on the 'color-selector' element.
  ; For more information check out the documentation of the 'color-selector' element.
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; {:click-effect (keyword)(opt)
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
  ;  :hover-effect (keyword)(opt)
  ;   :opacity
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: :choose-color!}
  ;
  ; @usage
  ; [color-picker {...}]
  ;
  ; @usage
  ; [color-picker :my-color-picker {...}]
  ([picker-props]
   [component (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (fn [_ picker-props] ; XXX#0106 (README.md#parametering)
       (let [picker-props (color-picker.prototypes/picker-props-prototype picker-id picker-props)]
            [color-picker picker-id picker-props]))))
