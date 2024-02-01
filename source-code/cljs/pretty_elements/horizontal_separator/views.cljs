
(ns pretty-elements.horizontal-separator.views
    (:require [fruits.random.api                               :as random]
              [metamorphic-content.api                         :as metamorphic-content]
              [pretty-elements.horizontal-separator.attributes :as horizontal-separator.attributes]
              [pretty-elements.horizontal-separator.prototypes :as horizontal-separator.prototypes]
              [pretty-elements.engine.api                               :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-separator
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  ; {:color (keyword or string)(opt)
  ;  :label (metamorphic-content)(opt)}
  [separator-id {:keys [color label] :as separator-props}]
  [:div (horizontal-separator.attributes/separator-attributes separator-id separator-props)
        [:div (horizontal-separator.attributes/separator-body-attributes separator-id separator-props)
              [:hr {:class :pe-horizontal-separator--line :data-border-color color}]
              (if label [:span {:class :pe-horizontal-separator--label}
                               (metamorphic-content/compose label)])
              [:hr {:class :pe-horizontal-separator--line :data-border-color color}]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) separator-id
  ; @param (map) separator-props
  [separator-id separator-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    separator-id separator-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount separator-id separator-props))
                       :reagent-render         (fn [_ separator-props] [horizontal-separator separator-id separator-props])}))

(defn view
  ; @param (keyword)(opt) separator-id
  ; @param (map) separator-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :label (metamorphic-content)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :auto}
  ;
  ; @usage
  ; [horizontal-separator {...}]
  ;
  ; @usage
  ; [horizontal-separator :my-horizontal-separator {...}]
  ([separator-props]
   [view (random/generate-keyword) separator-props])

  ([separator-id separator-props]
   ; @note (tutorials#parameterizing)
   (fn [_ separator-props]
       (let [separator-props (pretty-presets.engine/apply-preset                        separator-id separator-props)
             separator-props (horizontal-separator.prototypes/separator-props-prototype separator-id separator-props)]
            [view-lifecycles separator-id separator-props]))))
