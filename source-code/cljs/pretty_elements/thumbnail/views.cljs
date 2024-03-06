
(ns pretty-elements.thumbnail.views
    (:require [dynamic-props.api                    :as dynamic-props]
              [fruits.random.api                    :as random]
              [lazy-loader.api                      :as lazy-loader]
              [pretty-accessories.api               :as pretty-accessories]
              [pretty-elements.engine.api           :as pretty-elements.engine]
              [pretty-elements.thumbnail.attributes :as thumbnail.attributes]
              [pretty-elements.thumbnail.prototypes :as thumbnail.prototypes]
              [pretty-models.api                    :as pretty-models]
              [pretty-presets.engine.api            :as pretty-presets.engine]
              [pretty-subitems.api                  :as pretty-subitems]
              [reagent.core                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- thumbnail
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:badge (map)(opt)
  ;  :cover (map)(opt)
  ;  :icon (map)(opt)
  ;  :label (map)(opt)
  ;  :loaded? (boolean)(opt)
  ;  :marker (map)(opt)
  ;  :sensor (map)(opt)
  ;  :tooltip (map)(opt)
  ;  ...}
  [id {:keys [badge cover icon label loaded? marker sensor tooltip] :as props}]
  [:div (thumbnail.attributes/outer-attributes id props)
        [(pretty-models/clickable-auto-tag      id props)
         (thumbnail.attributes/inner-attributes id props)
         (when   loaded? [:div (thumbnail.attributes/thumbnail-canvas-attributes id props)])
         (if-not loaded? [lazy-loader/image-sensor   (pretty-subitems/subitem-id id :sensor)  sensor])
         (if-not loaded? [pretty-accessories/icon    (pretty-subitems/subitem-id id :icon)    icon])
         (when   label   [pretty-accessories/label   (pretty-subitems/subitem-id id :label)   label])
         (when   badge   [pretty-accessories/badge   (pretty-subitems/subitem-id id :badge)   badge])
         (when   marker  [pretty-accessories/marker  (pretty-subitems/subitem-id id :marker)  marker])
         (when   cover   [pretty-accessories/cover   (pretty-subitems/subitem-id id :cover)   cover])
         (when   tooltip [pretty-accessories/tooltip (pretty-subitems/subitem-id id :tooltip) tooltip])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   id props %))
                         :reagent-render         (fn [_ props] [thumbnail id props])}))

(defn view
  ; @description
  ; Optionally clickable thumbnail element with built-in lazy loader and animated loading icon.
  ;
  ; @links Implemented accessories
  ; [Badge](pretty-ui/cljs/pretty-accessories/api.html#badge)
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Icon](pretty-ui/cljs/pretty-accessories/api.html#icon)
  ; [Label](pretty-ui/cljs/pretty-accessories/api.html#label)
  ; [Marker](pretty-ui/cljs/pretty-accessories/api.html#marker)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background action properties](pretty-core/cljs/pretty-properties/api.html#background-action-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Background image properties](pretty-core/cljs/pretty-properties/api.html#background-image-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Keypress properties](pretty-core/cljs/pretty-properties/api.html#keypress-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/thumbnail.png)
  ; [thumbnail {:background-uri  "/my-thumbnail.png"
  ;             :badge           {:icon {:icon-name :fullscreen} :position :tr}
  ;             :border-radius   {:all :s}
  ;             :background-size :cover
  ;             :label           {:content "My thumbnail"}
  ;             :outer-height    :l
  ;             :outer-width     :l}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset           id props)
             props (thumbnail.prototypes/props-prototype         id props)
             props (pretty-elements.engine/element-timeout-props id props)
             props (dynamic-props/import-props                   id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
