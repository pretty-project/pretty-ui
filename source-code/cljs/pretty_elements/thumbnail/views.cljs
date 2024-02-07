
(ns pretty-elements.thumbnail.views
    (:require [fruits.css.api                       :as css]
              [fruits.random.api                    :as random]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.thumbnail.attributes :as thumbnail.attributes]
              [pretty-elements.thumbnail.prototypes :as thumbnail.prototypes]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [reagent.api                          :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- thumbnail
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ; {:background-size (keyword)
  ;  :uri (string)(opt)}
  [thumbnail-id {:keys [background-size uri] :as thumbnail-props}]
  [:div (thumbnail.attributes/thumbnail-attributes thumbnail-id thumbnail-props)
        [(pretty-elements.engine/clickable-auto-tag      thumbnail-id thumbnail-props)
         (thumbnail.attributes/thumbnail-body-attributes thumbnail-id thumbnail-props)
         [:i   {:class :pe-thumbnail--icon :data-icon-family :material-symbols-outlined :data-icon-size :s} :image]
         [:div {:class :pe-thumbnail--image :style {:background-image (css/url uri) :background-size background-size}}]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  [thumbnail-id thumbnail-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    thumbnail-id thumbnail-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount thumbnail-id thumbnail-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   thumbnail-id thumbnail-props %))
                       :reagent-render         (fn [_ thumbnail-props] [thumbnail thumbnail-id thumbnail-props])}))

(defn view
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
  ;  :cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :s
  ;  :helper (metamorphic-content)(opt)
  ;  :href-target (keyword)(opt)
  ;  :href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;   Default: :icon
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :keypress (map)(opt)
  ;  :on-click-f (function)(opt)
  ;  :on-click-timeout (ms)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :tab-disabled? (boolean)(opt)
  ;  :theme (keyword)(opt)
  ;  :uri (string)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :s}
  ;
  ; + keypress?
  ;
  ; @usage
  ; [thumbnail {...}]
  ;
  ; @usage
  ; [thumbnail :my-thumbnail {...}]
  ([thumbnail-props]
   [view (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   ; @note (tutorials#parameterizing)
   (fn [_ thumbnail-props]
       (let [thumbnail-props (pretty-presets.engine/apply-preset             thumbnail-id thumbnail-props)
             thumbnail-props (thumbnail.prototypes/thumbnail-props-prototype thumbnail-id thumbnail-props)
             thumbnail-props (pretty-elements.engine/element-timeout-props   thumbnail-id thumbnail-props :label)]  ; <- Nincs is label, de elférne egy ugy mint az icon-button alatt
            [view-lifecycles thumbnail-id thumbnail-props]))))
