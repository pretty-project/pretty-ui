
(ns pretty-elements.horizontal-line.views
    (:require [fruits.random.api                          :as random]
              [pretty-elements.horizontal-line.attributes :as horizontal-line.attributes]
              [pretty-elements.horizontal-line.prototypes :as horizontal-line.prototypes]
              [pretty-elements.engine.api                          :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                                :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-line
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  [line-id line-props]
  [:div (horizontal-line.attributes/line-attributes line-id line-props)
        [:div (horizontal-line.attributes/line-body-attributes line-id line-props)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  [line-id line-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    line-id line-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount line-id line-props))
                       :reagent-render         (fn [_ line-props] [horizontal-line line-id line-props])}))

(defn view
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   Default: :muted
  ;  :disabled? (boolean)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :strength (px)(opt)
  ;   Default: 1
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :auto}
  ;
  ; indent?
  ;
  ; @usage
  ; [horizontal-line {...}]
  ;
  ; @usage
  ; [horizontal-line :my-horizontal-line {...}]
  ([line-props]
   [view (random/generate-keyword) line-props])

  ([line-id line-props]
   ; @note (tutorials#parameterizing)
   (fn [_ line-props]
       (let [line-props (pretty-presets.engine/apply-preset              line-id line-props)
             line-props (horizontal-line.prototypes/line-props-prototype line-id line-props)]
            [view-lifecycles line-id line-props]))))
