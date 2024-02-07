
(ns pretty-elements.crumb.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.crumb.attributes :as crumb.attributes]
              [pretty-elements.crumb.prototypes :as crumb.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [reagent.api                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- crumb
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ; {:label (metamorphic-content)(opt)}
  [crumb-id {:keys [label] :as crumb-props}]
  [:div (crumb.attributes/crumb-attributes crumb-id crumb-props)
        [(pretty-elements.engine/clickable-auto-tag crumb-id crumb-props)
         (crumb.attributes/crumb-body-attributes    crumb-id crumb-props)
         [:div (crumb.attributes/crumb-label-attributes crumb-id crumb-props)
               (-> label)]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  [crumb-id crumb-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    crumb-id crumb-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount crumb-id crumb-props))
                       :reagent-render         (fn [_ crumb-props] [crumb crumb-id crumb-props])}))

(defn view
  ; @description
  ; Simplified button element for breadcrumb style menus.
  ;
  ; @param (keyword)(opt) crumb-id
  ; @param (map) crumb-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :label (metamorphic-content)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :text-color (keyword or string)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-transform (keyword)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [crumb {...}]
  ;
  ; @usage
  ; [crumb :my-crumb {...}]
  ([crumb-props]
   [view (random/generate-keyword) crumb-props])

  ([crumb-id crumb-props]
   ; @note (tutorials#parameterizing)
   (fn [_ crumb-props]
       (let [crumb-props (pretty-presets.engine/apply-preset     crumb-id crumb-props)
             crumb-props (crumb.prototypes/crumb-props-prototype crumb-id crumb-props)]
            [view-lifecycles crumb-id crumb-props]))))
