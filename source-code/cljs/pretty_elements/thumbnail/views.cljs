
(ns pretty-elements.thumbnail.views
    (:require [fruits.css.api                       :as css]
              [fruits.random.api                    :as random]
              [pretty-elements.thumbnail.attributes :as thumbnail.attributes]
              [pretty-elements.thumbnail.prototypes :as thumbnail.prototypes]
              [pretty-elements.engine.api                    :as pretty-elements.engine]
              [pretty-presets.api                   :as pretty-presets]
              [reagent.api                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- thumbnail
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)
  ;  :href (string)(opt)
  ;  :on-click-f (function)(opt)
  ;  :uri (string)(opt)}
  [thumbnail-id {:keys [background-size href on-click-f uri] :as thumbnail-props}]
  [:div (thumbnail.attributes/thumbnail-attributes thumbnail-id thumbnail-props)
        [(cond href :a on-click-f :button :else :div)
         (thumbnail.attributes/thumbnail-body-attributes thumbnail-id thumbnail-props)
         [:i   {:class :pe-thumbnail--icon :data-icon-family :material-symbols-outlined :data-icon-size :s} :image]
         [:div {:class :pe-thumbnail--image :style {:background-image (css/url uri) :background-size background-size}}]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  [thumbnail-id thumbnail-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    thumbnail-id thumbnail-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount thumbnail-id thumbnail-props))
                       :reagent-render         (fn [_ thumbnail-props] [thumbnail thumbnail-id thumbnail-props])}))
                       ; @note (pretty-elements.adornment.views#8097)
                       ; + did update, keypress?

(defn element
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)(opt)
  ;   :contain, :cover
  ;   Default: :contain
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;   Default: {:all :m}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :s
  ;  :helper (metamorphic-content)(opt)
  ;  :href (string)(opt)
  ;  :icon (keyword)(opt)
  ;   Default: :icon
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-mouse-over-f (function)(opt)
  ;  :on-right-click-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self
  ;  :theme (keyword)(opt)
  ;  :uri (string)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :s}
  ;
  ; @usage
  ; [thumbnail {...}]
  ;
  ; @usage
  ; [thumbnail :my-thumbnail {...}]
  ([thumbnail-props]
   [element (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   ; @note (tutorials#parametering)
   (fn [_ thumbnail-props]
       (let [thumbnail-props (pretty-presets/apply-preset                    thumbnail-props)
             thumbnail-props (thumbnail.prototypes/thumbnail-props-prototype thumbnail-props)]
            [element-lifecycles thumbnail-id thumbnail-props]))))
