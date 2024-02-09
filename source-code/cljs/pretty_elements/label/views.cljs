
(ns pretty-elements.label.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-elements.label.attributes :as label.attributes]
              [pretty-elements.label.prototypes :as label.prototypes]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [reagent.api                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (string)(opt)
  ;  :icon (keyword)(opt)}
  ;  :icon-position (keyword)(opt)}
  [label-id {:keys [content icon icon-position] :as label-props}]
  [:div (label.attributes/label-attributes label-id label-props)
        [:div (label.attributes/label-body-attributes label-id label-props)
              (case icon-position :right [:<> (if content [:div (label.attributes/label-content-attributes label-id label-props) content])
                                              (if icon    [:i   (label.attributes/label-icon-attributes    label-id label-props) icon])]
                                         [:<> (if icon    [:i   (label.attributes/label-icon-attributes    label-id label-props) icon])
                                              (if content [:div (label.attributes/label-content-attributes label-id label-props) content])])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  [label-id label-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    label-id label-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount label-id label-props))
                       :reagent-render         (fn [_ label-props] [label label-id label-props])}))

(defn view
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :content-placeholder (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword or integer)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :icon-position (keyword)(opt)
  ;  :icon-size (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :line-height (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-selectable? (boolean)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [label {...}]
  ;
  ; @usage
  ; [label :my-label {...}]
  ([label-props]
   [view (random/generate-keyword) label-props])

  ([label-id label-props]
   ; @note (tutorials#parameterizing)
   (fn [_ label-props]
       (let [label-props (pretty-presets.engine/apply-preset     label-id label-props)
             label-props (label.prototypes/label-props-prototype label-id label-props)]
            [view-lifecycles label-id label-props]))))
