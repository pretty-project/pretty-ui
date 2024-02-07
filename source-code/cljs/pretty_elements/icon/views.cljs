
(ns pretty-elements.icon.views
    (:require [fruits.random.api               :as random]
              [pretty-elements.engine.api      :as pretty-elements.engine]
              [pretty-elements.icon.attributes :as icon.attributes]
              [pretty-elements.icon.prototypes :as icon.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [reagent.api                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ; {:icon (keyword)(opt)}
  [icon-id {:keys [icon] :as icon-props}]
  [:div (icon.attributes/icon-attributes icon-id icon-props)
        [:i (icon.attributes/icon-body-attributes icon-id icon-props) icon]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  [icon-id icon-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    icon-id icon-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount icon-id icon-props))
                       :reagent-render         (fn [_ icon-props] [icon icon-id icon-props])}))

(defn view
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :icon-size (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [icon {...}]
  ;
  ; @usage
  ; [icon :my-icon {...}]
  ([icon-props]
   [view (random/generate-keyword) icon-props])

  ([icon-id icon-props]
   ; @note (tutorials#parameterizing)
   (fn [_ icon-props]
       (let [icon-props (pretty-presets.engine/apply-preset   icon-id icon-props)
             icon-props (icon.prototypes/icon-props-prototype icon-id icon-props)]
            [view-lifecycles icon-id icon-props]))))
