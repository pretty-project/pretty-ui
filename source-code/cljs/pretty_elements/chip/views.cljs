
(ns pretty-elements.chip.views
    (:require [fruits.random.api                     :as random]
              [fruits.vector.api                     :as vector]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.chip.attributes       :as chip.attributes]
              [pretty-elements.chip.prototypes       :as chip.prototypes]
              [pretty-elements.engine.api                     :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                           :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {}
  [chip-id {:keys [end-adornments href-uri label on-click-f placeholder start-adornments] :as chip-props}]
  [:div (chip.attributes/chip-attributes chip-id chip-props)
        [(cond href-uri :a on-click-f :button :else :div)
         (chip.attributes/chip-body-attributes chip-id chip-props)
         (if (vector/not-empty? start-adornments)
             [adornment-group.views/element {:adornments start-adornments}])
         [:div (chip.attributes/chip-label-attributes chip-id chip-props) [metamorphic-content/compose label placeholder]]
         (if (vector/not-empty? end-adornments)
             [adornment-group.views/element {:adornments end-adornments}])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  [chip-id chip-props]
  ; @note (tutorials#parametering)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    chip-id chip-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount chip-id chip-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   chip-id chip-props %))
                       :reagent-render         (fn [_ chip-props] [chip chip-id chip-props])}))

(defn element
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ; {:border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :class (keyword or keywords in vector)(opt)
  ;  :click-effect (keyword)(opt)
  ;   Default: :opacity (if 'href-uri' or 'on-click-f' is provided)
  ;  :disabled? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :height (keyword, px or string)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :hover-pattern (keyword)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :label (metamorphic-content)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-mouse-over-f (function)(opt)
  ;  :on-right-click-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  :style (map)(opt)
  ;  :text-color (keyword or string)(opt)
  ;   Default: :default
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [chip {...}]
  ;
  ; @usage
  ; [chip :my-chip {...}]
  ([chip-props]
   [element (random/generate-keyword) chip-props])

  ([chip-id chip-props]
   ; @note (tutorials#parametering)
   (fn [_ chip-props]
       (let [chip-props (pretty-presets.engine/apply-preset   chip-props)
             chip-props (chip.prototypes/chip-props-prototype chip-props)]
            [element-lifecycles chip-id chip-props]))))

            ; on-click-timeout?
