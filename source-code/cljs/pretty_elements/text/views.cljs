
(ns pretty-elements.text.views
    (:require [fruits.hiccup.api               :as hiccup]
              [fruits.random.api               :as random]
              [metamorphic-content.api         :as metamorphic-content]
              [pretty-elements.element.views   :as element.views]
              [pretty-elements.text.attributes :as text.attributes]
              [pretty-elements.text.prototypes :as text.prototypes]
              [pretty-presets.api              :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (metamorphic-content)(opt)
  ;  :on-copy (Re-Frame metamorphic-event)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [text-id {:keys [content on-copy placeholder] :as text-props}]
  [:div (text.attributes/text-attributes text-id text-props)
        [element.views/element-label     text-id text-props]
        [:div (text.attributes/text-body-attributes text-id text-props)
              (if on-copy [:div (text.attributes/copyable-attributes text-id text-props)
                                [:div (text.attributes/content-attributes text-id text-props)
                                      (hiccup/parse-newlines [:<> (metamorphic-content/compose content placeholder)])]]
                          [:<>  [:div (text.attributes/content-attributes text-id text-props)
                                      (hiccup/parse-newlines [:<> (metamorphic-content/compose content placeholder)])]])]])

(defn element
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
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
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;   Default: :text-block
  ;  :max-lines (integer)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-copy (Re-Frame metamorphic-event)(opt)
  ;   This event takes the text content as its last parameter.
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: "\u00A0"
  ;  :preset (keyword)(opt)
  ;  :selectable? (boolean)(opt)
  ;   Default: true
  ;  :style (map)(opt)
  ;  :text-color (keyword or string)(opt)
  ;   Default: :default
  ;  :text-direction (keyword)(opt)
  ;   :normal, :reversed
  ;   Default :normal
  ;  :text-align (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;   Default: :wrap
  ;  :vertical-align (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [text {...}]
  ;
  ; @usage
  ; [text :my-text {...}]
  ([text-props]
   [element (random/generate-keyword) text-props])

  ([text-id text-props]
   ; @note (tutorials#parametering)
   (fn [_ text-props]
       (let [text-props (pretty-presets/apply-preset          text-props)
             text-props (text.prototypes/text-props-prototype text-props)]
            [text text-id text-props]))))
