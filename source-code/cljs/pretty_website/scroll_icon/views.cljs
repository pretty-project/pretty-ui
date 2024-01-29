
(ns pretty-website.scroll-icon.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.engine.api                     :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-website.scroll-icon.attributes :as scroll-icon.attributes]
              [pretty-website.scroll-icon.prototypes :as scroll-icon.prototypes]
              [pretty-website.scroll-sensor.views    :as scroll-sensor.views]
              [reagent.api                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scroll-icon
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  [icon-id icon-props]
  [:<> [scroll-sensor.views/component (scroll-icon.attributes/sensor-attributes icon-id icon-props)]
       [:div (scroll-icon.attributes/icon-attributes icon-id icon-props)
             [:div (scroll-icon.attributes/icon-body-attributes icon-id icon-props)]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- component-lifecycles
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  [icon-id icon-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    icon-id icon-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount icon-id icon-props))
                       :reagent-render         (fn [_ icon-props] [scroll-icon icon-id icon-props])}))

(defn component
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (string)(opt)
  ;   Default: "#FFFFFF"
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [scroll-icon {...}]
  ;
  ; @usage
  ; [scroll-icon :my-scroll-icon {...}]
  ([icon-props]
   [component (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   ; @note (tutorials#parametering)
   (fn [_ icon-props]
       (let [icon-props (pretty-presets.engine/apply-preset          icon-id icon-props)
             icon-props (scroll-icon.prototypes/icon-props-prototype icon-id icon-props)]
            [component-lifecycles icon-id icon-props]))))
