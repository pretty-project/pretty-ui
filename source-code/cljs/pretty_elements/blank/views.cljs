
(ns pretty-elements.blank.views
    (:require [fruits.random.api                :as random]
              [pretty-elements.blank.attributes :as blank.attributes]
              [pretty-elements.blank.prototypes :as blank.prototypes]
              [pretty-elements.engine.api       :as pretty-elements.engine]
              [pretty-presets.engine.api        :as pretty-presets.engine]
              [reagent.api                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ; {:content (metamorphic-content)(opt)}
  [blank-id {:keys [content] :as blank-props}]
  [:div (blank.attributes/blank-attributes blank-id blank-props)
        [:div (blank.attributes/blank-body-attributes blank-id blank-props)
              (-> content)]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  [blank-id blank-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    blank-id blank-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount blank-id blank-props))
                       :reagent-render         (fn [_ blank-props] [blank blank-id blank-props])}))

(defn view
  ; @description
  ; Simplified element for displaying content.
  ;
  ; @param (keyword)(opt) blank-id
  ; @param (map) blank-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :content-placeholder (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
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
  ; [blank {...}]
  ;
  ; @usage
  ; [blank :my-blank {...}]
  ([blank-props]
   [view (random/generate-keyword) blank-props])

  ([blank-id blank-props]
   ; @note (tutorials#parameterizing)
   (fn [_ blank-props]
       (let [blank-props (pretty-presets.engine/apply-preset     blank-id blank-props)
             blank-props (blank.prototypes/blank-props-prototype blank-id blank-props)]
            [view-lifecycles blank-id blank-props]))))
