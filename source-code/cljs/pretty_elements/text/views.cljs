
(ns pretty-elements.text.views
    (:require [fruits.hiccup.api               :as hiccup]
              [fruits.random.api               :as random]
              [metamorphic-content.api         :as metamorphic-content]
              [pretty-elements.text.attributes :as text.attributes]
              [pretty-elements.text.prototypes :as text.prototypes]
              [pretty-elements.engine.api               :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                     :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (metamorphic-content)(opt)
  ;  :on-copy-f (function)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [text-id {:keys [content on-copy-f placeholder] :as text-props}]
  [:div (text.attributes/text-attributes text-id text-props)
        [:div (text.attributes/text-body-attributes text-id text-props)
              (if on-copy-f [:div (text.attributes/copyable-attributes text-id text-props)
                                  [:div (text.attributes/content-attributes text-id text-props)
                                        (hiccup/parse-newlines [:<> (metamorphic-content/compose content placeholder)])]]
                            [:<>  [:div (text.attributes/content-attributes text-id text-props)
                                        (hiccup/parse-newlines [:<> (metamorphic-content/compose content placeholder)])]])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  [text-id text-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    text-id text-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount text-id text-props))
                       :reagent-render         (fn [_ text-props] [text text-id text-props])}))

(defn view
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;   Default: :cover
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :font-weight (keyword or integer)(opt)
  ;   Default: :normal
  ;  :height (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;   Default: :text-block
  ;  :max-lines (integer)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-copy-f (function)(opt)
  ;   Takes the text content as parameter.
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: "\u00A0"
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :text-align (keyword)(opt)
  ;  :text-color (keyword or string)(opt)
  ;   Default: :default
  ;  :text-direction (keyword)(opt)
  ;   :normal, :reversed
  ;   Default :normal
  ;  :text-overflow (keyword)(opt)
  ;   Default: :wrap
  ;  :text-selectable? (boolean)(opt)
  ;  :theme (keyword)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [text {...}]
  ;
  ; @usage
  ; [text :my-text {...}]
  ([text-props]
   [view (random/generate-keyword) text-props])

  ([text-id text-props]
   ; @note (tutorials#parametering)
   (fn [_ text-props]
       (let [text-props (pretty-presets.engine/apply-preset   text-id text-props)
             text-props (text.prototypes/text-props-prototype text-id text-props)]
            [view-lifecycles text-id text-props]))))
